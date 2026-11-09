import io.github.itzispyder.clickcrystals.scripting.runtime.TickTaskQueue;
import java.util.concurrent.atomic.AtomicInteger;
public final class TickTaskQueueTest {
    private static void ok(boolean b,String message) {if(!b)throw new AssertionError(message);}
    public static void main(String[] ignored) {
        TickTaskQueue q=new TickTaskQueue();
        AtomicInteger count=new AtomicInteger();
        q.schedule("once",3,0,count::incrementAndGet);
        q.tick();q.tick(); ok(count.get()==0,"too early");
        q.tick();ok(count.get()==1,"due tick");
        q.tick();ok(q.size()==0,"single task gone");
        q.schedule("repeat",1,2,count::incrementAndGet);
        q.tick();ok(count.get()==2,"first repeat");
        q.tick();ok(count.get()==2,"repeat too early");
        q.tick();ok(count.get()==3,"second repeat");
        ok(q.cancel("repeat"),"cancel");
        q.tick();q.tick();ok(count.get()==3,"cancelled repeats");
        q.schedule("replace",1,0,()->count.addAndGet(20));
        q.schedule("replace",1,0,()->count.addAndGet(5));
        q.tick();ok(count.get()==8,"replaced task should execute once");
        q.schedule("self",1,1,()->{q.cancel("self");count.incrementAndGet();});
        q.tick();q.tick();ok(count.get()==9,"self-cancel");
        q.schedule("reconnect",1,1,count::incrementAndGet);
        q.cancelAll();q.tick();ok(count.get()==9,"world cancel");
        boolean rejected=false;
        try{q.schedule("broken",0,0,()->{});}catch(IllegalArgumentException ex){rejected=true;}
        ok(rejected,"invalid delay rejected");
        for(int i=0;i<64;i++)q.schedule("job"+i,1,1,()->{});
        rejected=false;try{q.schedule("overflow",1,0,()->{});}catch(IllegalStateException ex){rejected=true;}
        ok(rejected,"task limit enforced");

        var reported = new java.util.concurrent.atomic.AtomicReference<String>();
        TickTaskQueue noisy = new TickTaskQueue((name, error) ->
                reported.set(name + ":" + error.getMessage()));
        noisy.schedule("broken_repeat", 1, 1, () -> {
            throw new IllegalArgumentException("bad input");
        });
        noisy.tick();
        ok("broken_repeat:bad input".equals(reported.get()),
                "task failures must reach the diagnostic callback");
        ok(noisy.size() == 0, "failing recurring task must be removed");
        noisy.tick();
        ok(noisy.size() == 0, "failing task was incorrectly rescheduled");

        q.cancelAll();
        AtomicInteger throttled = new AtomicInteger();
        for (int i=0; i<64; i++) q.schedule("throttle" + i, 1, 0, throttled::incrementAndGet);
        q.tick();
        ok(throttled.get() == TickTaskQueue.MAX_EXECUTIONS_PER_TICK,
                "per-tick work budget exceeded");
        q.tick();
        ok(throttled.get() == 64 && q.size() == 0,
                "remaining due tasks should execute on the next tick");
        System.out.println("TickTaskQueueTest: PASS");
    }
}
