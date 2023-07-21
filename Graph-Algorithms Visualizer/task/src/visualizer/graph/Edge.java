package visualizer.graph;

public class Edge {
    private final Vertex from;
    private final Vertex to;
    private final Integer weight;

    public Edge(Vertex from, Vertex to, Integer weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge <" + from.getId() + " -> " + to.getId() + ">";
    }

}
