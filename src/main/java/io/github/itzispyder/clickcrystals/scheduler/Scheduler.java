package io.github.itzispyder.clickcrystals.scheduler;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.util.misc.Randomizer;

import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scheduler {

    public static final int INFINITE_ITERATIONS = -1;
    private final ConcurrentLinkedQueue<Task> tasks;
    private Thread worker;
    private boolean started;

    public Scheduler() {
        this.started = false;
        this.tasks = new ConcurrentLinkedQueue<>();
        this.init();
    }

    public int count() {
        return tasks.size();
    }

    /**
     * Once a scheduler is started, it cannot be stopped nor can it be started again!
     */
    protected void init() {
        if (started) {
            throw new IllegalArgumentException("Scheduler is already started!");
        }

        started = true;
        worker = new Thread(() -> {
            while (true) {
                try {
                    for (Task task : tasks) {
                        try {
                            task.tick();
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    Thread.sleep(1);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }, "System Scheduler");
        worker.start();
    }

    public Thread getWorker() {
        return worker;
    }

    public synchronized void runDelayedTask(Runnable runnable, long delay) {
        Task task = new Task(runnable, delay);
        task.activate();
        tasks.add(task);
    }

    public synchronized void runRepeatingTask(Runnable runnable, long delay, long period) {
        this.runRepeatingTask(runnable, delay, period, INFINITE_ITERATIONS);
    }

    public synchronized void runRepeatingTask(Runnable runnable, long delay, long period, int iterations) {
        Task task = new Task(runnable, delay, period, iterations);
        task.activate();
        tasks.add(task);
    }

    public synchronized TaskChain runChainTask() {
        return new TaskChain();
    }

    public class TaskChain {
        private final Stack<TaskPair> schedule;
        private final Randomizer randomizer;

        private TaskChain() {
            this.schedule = new Stack<>();
            this.randomizer = new Randomizer();
        }

        public TaskChain thenRun(Runnable task) {
            if (!schedule.isEmpty() && schedule.peek().task == null) {
                schedule.peek().task = new Task(task);
            }
            else {
                schedule.push(new TaskPair(0, new Task(task)));
            }
            return this;
        }

        public TaskChain thenRepeat(Runnable task, long period, int times) {
            if (!schedule.isEmpty() && schedule.peek().task == null) {
                schedule.peek().task = new Task(task, 0, period, times);
            }
            else {
                schedule.push(new TaskPair(0, new Task(task, 0, period, times)));
            }
            return this;
        }

        public TaskChain thenWait(long millis) {
            if (!schedule.isEmpty() && schedule.peek().task == null) {
                schedule.peek().wait = millis;
            }
            else {
                schedule.push(new TaskPair(millis, null));
            }
            return this;
        }

        public TaskChain thenWaitRandom(int min, int max) {
            return thenWait(randomizer.getRandomInt(min, max));
        }

        public TaskChain thenWaitRandom(int max) {
            return thenWait(randomizer.getRandomInt(max));
        }

        public void startChain() {
            runDelayedTask(() -> {
                CompletableFuture.runAsync(() -> {
                    for (TaskPair pair : schedule) {
                        pair.task.delay = pair.wait;
                        pair.task.unsafeRawExecute();
                    }
                });
            }, 0);
        }

        private class TaskPair {
            public long wait;
            public Task task;

            public TaskPair(long wait, Task task) {
                this.wait = wait;
                this.task = task;
            }
        }
    }

    private class Task implements Cancellable {
        private final Runnable task;
        private final long period;
        private long delay;
        private final int iterations;
        private int iterated;
        private long millisTimer;
        private boolean cancelled;
        private final AtomicBoolean active, activating;

        public Task(Runnable task) {
            this(task, 0, 0, 1);
        }

        public Task(Runnable task, long delay) {
            this(task, delay, 0, 1);
        }

        public Task(Runnable task, long delay, long period, int iterations) {
            this.task = task;
            this.cancelled = false;
            this.delay = delay;
            this.period = period;
            this.millisTimer = 0L;
            this.active = new AtomicBoolean(false);
            this.activating = new AtomicBoolean(false);
            this.iterations = iterations;
            this.iterated = 0;
        }

        public void activate() {
            if (!active.get() && !activating.get()) {
                activating.set(true);
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(delay);
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    active.set(true);
                    activating.set(false);
                });
            }
        }

        public synchronized void tick() {
            if (!active.get()) {
                return;
            }
            if (cancelled || iterations == 0) {
                tasks.remove(this);
                return;
            }

            if (millisTimer++ >= period) {
                if (iterations == INFINITE_ITERATIONS) {
                    millisTimer = 0;
                    task.run();
                    return;
                }

                if (iterated++ >= iterations) {
                    tasks.remove(this);
                }
                else {
                    millisTimer = 0;
                    task.run();
                }
            }
        }

        public synchronized void unsafeRawExecute() {
            try {
                Thread.sleep(delay);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            while (iterated < iterations && !cancelled) {
                task.run();
                iterated++;
                try {
                    Thread.sleep(period);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            tasks.remove(this);
        }

        @Override
        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }
    }
}
