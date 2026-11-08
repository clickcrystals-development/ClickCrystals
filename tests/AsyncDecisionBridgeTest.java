import io.github.itzispyder.clickcrystals.scripting.runtime.AsyncDecisionBridge;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
public final class AsyncDecisionBridgeTest {
    public static void main(String[] args) throws Exception {
        var queue=new ConcurrentLinkedQueue<Runnable>();
        var result=new AtomicReference<String>();
        var error=new AtomicReference<Throwable>();
        try(var bridge=new AsyncDecisionBridge(queue::add)) {
            bridge.submit("world-snapshot",s->"plan:"+s,result::set,error::set);
            for(int i=0;i<200&&queue.isEmpty();i++)Thread.sleep(5);
            if(queue.isEmpty())throw new AssertionError("worker did not produce callback");
            queue.poll().run();
            if(!"plan:world-snapshot".equals(result.get())||error.get()!=null)
                throw new AssertionError("result dispatch failed");
            bridge.submit("stale",s->"should-not-apply",result::set,error::set);
            bridge.cancelAll();
            Thread.sleep(20);
            for(Runnable cb;(cb=queue.poll())!=null;)cb.run();
            if(!"plan:world-snapshot".equals(result.get()))
                throw new AssertionError("cancelAll allowed stale callback");
        }
        System.out.println("AsyncDecisionBridgeTest: PASS");
    }
}
