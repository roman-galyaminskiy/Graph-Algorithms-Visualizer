package visualizer.algos;

import visualizer.graph.Graph;
import visualizer.graph.Vertex;
import visualizer.ui.GraphPanel;
import visualizer.ui.VertexPanel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class Dijkstra {

    public static String run(GraphPanel graphPanel, VertexPanel startingVertex) {
        List<Vertex> result = new ArrayList<>();

        Graph graph = graphPanel.getGraph();

        IndexedMinPriorityQueue priorityQueue =
                new IndexedMinPriorityQueue(graph.getVertices().size());

        for (var vertex : graph.getVertices()) {
            if (vertex == startingVertex.getVertex()) {
                vertex.setDistance(0);
            } else {
                vertex.setDistance(Integer.MAX_VALUE);
            }
            priorityQueue.push(vertex);
        }

        while (!priorityQueue.isEmpty()) {
            var closestToStart = priorityQueue.pop();
            if (closestToStart != startingVertex.getVertex()) {
                result.add(closestToStart);
            }
            graphPanel.getVertexPanel(closestToStart).visit();

            for (var edge : graph.getAdjacentEdges(closestToStart)) {
                var adjacentVertex = edge.getTo();
                int newDistance = closestToStart.getDistance() + edge.getWeight();
                if (adjacentVertex.getDistance().compareTo(newDistance) > 0) {
                    adjacentVertex.setDistance(newDistance);
                    adjacentVertex.setPrev(closestToStart);
                    priorityQueue.decreaseDistance(adjacentVertex.getId(), adjacentVertex.getDistance());
                }
            }
        }

        graphPanel.repaint();
        StringJoiner joiner = new StringJoiner(", ");
        result.sort(Comparator.comparing(Vertex::getId));
        for (Vertex vertex : result) {
            joiner.add(vertex.getId() + "=" + vertex.getDistance());
        }
        return joiner.toString();
    }
}
