import io.github.itzispyder.clickcrystals.scripting.runtime.nav.IncrementalPathfinder;
import io.github.itzispyder.clickcrystals.scripting.runtime.nav.IncrementalPathfinder.Node;
import io.github.itzispyder.clickcrystals.scripting.runtime.nav.IncrementalPathfinder.Edge;
import java.util.*;
public final class IncrementalPathfinderTest {
    private static void ok(boolean x){if(!x)throw new AssertionError();}
    public static void main(String[] ignored){
        Node a=new Node(0,64,0),b=new Node(4,64,4);
        var pf=new IncrementalPathfinder(a,n->n.equals(b),n->{
            List<Edge> steps=new ArrayList<>();
            for(int[] v:new int[][]{{1,0},{-1,0},{0,1},{0,-1}}){
                int x=n.x()+v[0],z=n.z()+v[1];
                if(x<0||z<0||x>4||z>4||(x==2&&z<4))continue;
                steps.add(new Edge(new Node(x,64,z),1));
            }
            return steps;
        },n->Math.abs(b.x()-n.x())+Math.abs(b.z()-n.z()),200);
        for(int i=0;i<200&&pf.status()==IncrementalPathfinder.Status.RUNNING;i++)pf.advance(2);
        ok(pf.status()==IncrementalPathfinder.Status.FOUND);
        ok(pf.route().get(0).equals(a)&&pf.route().get(pf.route().size()-1).equals(b));
        ok(pf.route().stream().noneMatch(n->n.x()==2&&n.z()<4));
        var cancelled=new IncrementalPathfinder(a,n->false,n->List.of(),n->0.0,10);
        cancelled.cancel();
        ok(cancelled.advance(1)==IncrementalPathfinder.Status.CANCELLED);
        System.out.println("IncrementalPathfinderTest: PASS");
    }
}
