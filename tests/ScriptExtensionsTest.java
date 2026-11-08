import io.github.itzispyder.clickcrystals.scripting.runtime.ScriptExtensions;
import java.util.List;
public final class ScriptExtensionsTest {
    static void yes(boolean b) { if(!b)throw new AssertionError(); }
    public static void main(String[] argv) {
        ScriptExtensions.Context c=new ScriptExtensions.Context() {
            public String readGame(String metric){return "world";}
            public String readState(String key){return "42";}
            public void writeState(String key,String val){}
        };
        ScriptExtensions.registerSensor("example:state",ctx->ctx.readState("x"));
        ScriptExtensions.registerAction("example:walk",(ctx,args)->{
            yes(args.size()==2 && ctx.readGame("dimension").equals("world"));
        });
        yes(ScriptExtensions.has("example:state"));
        yes(ScriptExtensions.has("example:walk"));
        yes(!ScriptExtensions.has("missing:action"));
        yes(ScriptExtensions.read("example:state",c).equals("42"));
        ScriptExtensions.call("example:walk",c,List.of("1","2"));
        boolean rejected=false;
        try{ScriptExtensions.registerSensor("example:state",ctx->"x");}
        catch(IllegalStateException ex){rejected=true;}
        yes(rejected);
        System.out.println("ScriptExtensionsTest: PASS");
    }
}
