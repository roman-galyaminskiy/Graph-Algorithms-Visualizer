package visualizer.graph;

public class Vertex {

    private final String id;
    private Integer distance;
    private Vertex prev;

    public Vertex(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Vertex getPrev() {
        return prev;
    }

    public void setPrev(Vertex prev) {
        this.prev = prev;
    }
}
