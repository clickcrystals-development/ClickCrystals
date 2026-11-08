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
        System.out.println("TickTaskQueueTest: PASS");
    }
}
