package io.github.itzispyder.clickcrystals.scripting.runtime.nav;
import java.util.*;
import java.util.function.*;
/** Bounded, incremental A* for Minecraft adapter extensions. No direct game input. */
public final class IncrementalPathfinder {
    public record Node(int x,int y,int z) {}
    public record Edge(Node to,double cost) {
        public Edge {
            if(to==null||!Double.isFinite(cost)||cost<=0)
                throw new IllegalArgumentException("Finite positive edge costs required");
        }
    }
    @FunctionalInterface public interface Neighbors { Iterable<Edge> of(Node node); }
    public enum Status { RUNNING,FOUND,FAILED,CANCELLED }
    private record Candidate(Node node,double cost,double estimate,long order){}
    private final Neighbors neighbors;
    private final Predicate<Node> goal;
    private final ToDoubleFunction<Node> heuristic;
    private final PriorityQueue<Candidate> open;
    private final Map<Node,Double> best=new HashMap<>();
    private final Map<Node,Node> parents=new HashMap<>();
    private final Node start;
    private final int explorationLimit;
    private long order;
    private int expanded;
    private Status status=Status.RUNNING;
    private List<Node> route=List.of();

    public IncrementalPathfinder(Node start,Predicate<Node> goal,Neighbors neighbors,
                                 ToDoubleFunction<Node> heuristic,int explorationLimit){
        if(start==null||goal==null||neighbors==null||heuristic==null)
            throw new IllegalArgumentException("Path arguments cannot be null");
        if(explorationLimit<1||explorationLimit>200_000)
            throw new IllegalArgumentException("Budget must be 1..200000");
        this.start=start;this.goal=goal;this.neighbors=neighbors;this.heuristic=heuristic;
        this.explorationLimit=explorationLimit;
        this.open=new PriorityQueue<>(Comparator.comparingDouble(Candidate::estimate).thenComparingLong(Candidate::order));
        best.put(start,0.0);
        open.add(new Candidate(start,0.0,heuristic(start),order++));
    }

    private double heuristic(Node node){
        double h=heuristic.applyAsDouble(node);
        if(!Double.isFinite(h)||h<0)throw new IllegalArgumentException("Invalid A* heuristic");
        return h;
    }

    /** Call on the game thread, with at most 4096 expansions per game tick. */
    public Status advance(int budget){
        if(budget<1||budget>4096)throw new IllegalArgumentException("Tick budget must be 1..4096");
        if(status!=Status.RUNNING)return status;
        for(int i=0;i<budget;i++){
            Candidate curr=open.poll();
            while(curr!=null&&curr.cost()>best.getOrDefault(curr.node(),Double.POSITIVE_INFINITY))
                curr=open.poll();
            if(curr==null){status=Status.FAILED;return status;}
            if(goal.test(curr.node())){
                route=reconstruct(curr.node());status=Status.FOUND;return status;
            }
            if(++expanded>=explorationLimit){status=Status.FAILED;return status;}
            Iterable<Edge> list=neighbors.of(curr.node());
            if(list==null)continue;
            for(Edge edge:list){
                if(edge==null)continue;
                double next=curr.cost()+edge.cost();
                if(!Double.isFinite(next)||next>=best.getOrDefault(edge.to(),Double.POSITIVE_INFINITY))continue;
                best.put(edge.to(),next);
                parents.put(edge.to(),curr.node());
                open.add(new Candidate(edge.to(),next,next+heuristic(edge.to()),order++));
            }
        }
        return status;
    }

    private List<Node> reconstruct(Node end){
        List<Node> reverse=new ArrayList<>();
        Node node=end;
        for(int i=0;node!=null&&i<=explorationLimit+1;i++){
            reverse.add(node);
            if(node.equals(start))break;
            node=parents.get(node);
        }
        if(!reverse.get(reverse.size()-1).equals(start))
            throw new IllegalStateException("Path predecessor chain broken");
        Collections.reverse(reverse);
        return List.copyOf(reverse);
    }
    public void cancel(){if(status==Status.RUNNING)status=Status.CANCELLED;open.clear();}
    public Status status(){return status;}
    public List<Node> route(){return route;}
    public int expandedNodes(){return expanded;}
}
