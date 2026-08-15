package io.github.itzispyder.clickcrystals.util.misc.scheduler;

import io.github.itzispyder.clickcrystals.util.misc.Randomizer;

import java.util.ArrayDeque;
import java.util.Queue;

public class SchedulerChain {

    private final Queue<Node> chain;
    private final Randomizer randomizer;
    private final Scheduler scheduler;

    public SchedulerChain(Scheduler scheduler) {
        this.chain = new ArrayDeque<>();
        this.randomizer = new Randomizer();
        this.scheduler = scheduler;
    }

    private static class Node {
        private final long wait;
        private final Runnable task;

        public Node(long wait, Runnable task) {
            this.wait = wait;
            this.task = task;
        }
    }

    public SchedulerChain thenRun(Runnable task) {
        chain.add(new Node(0, task));
        return this;
    }

    public SchedulerChain thenWait(long delay) {
        chain.add(new Node(delay, null));
        return this;
    }

    public SchedulerChain thenRepeat(Runnable task, long period, int times) {
        if (times <= 0)
            return this;

        thenRun(task);
        for (int i = 1; i < times; i++) {
            thenWait(period);
            thenRun(task);
        }
        return this;
    }

    public SchedulerChain thenWaitRandom(int min, int max) {
        return thenWait(randomizer.getRandomInt(min, max));
    }

    public SchedulerChain thenWaitRandom(int max) {
        return thenWait(randomizer.getRandomInt(max));
    }

    public void startChain() {
        runNext();
    }

    private void runNext() {
        if (chain.isEmpty())
            return;

        Node node = chain.poll();

        if (node.wait > 0)
            scheduler.runDelayedTask(self -> executeNode(node), node.wait);
        else
            executeNode(node);
    }

    private void executeNode(Node node) {
        if (node.task != null) {
            try {
                node.task.run();
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("exception while running scheduler task");
            }
        }
        runNext();
    }
}