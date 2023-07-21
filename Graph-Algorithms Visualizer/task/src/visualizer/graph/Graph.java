package visualizer.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

    private final Map<Vertex, Set<Edge>> adjacencyList = new HashMap<>();

    public Set<Vertex> getVertices() {
        return adjacencyList.keySet();
    }

    public Set<Edge> getAdjacentEdges(Vertex vertex) {
        return adjacencyList.get(vertex);
    }

    public void addVertex(Vertex vertex) {
        adjacencyList.put(vertex, new HashSet<>());
    }

    public void addEdge(Edge edge) {
        adjacencyList.merge(edge.getFrom(), new HashSet<>(Set.of(edge)), (old, neu) -> {
            old.addAll(neu);
            return old;
        });
    }

    public void removeEdge(Edge edge) {
        adjacencyList.get(edge.getFrom()).remove(edge);
    }
}
