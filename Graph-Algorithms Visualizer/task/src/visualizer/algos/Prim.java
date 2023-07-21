package visualizer.algos;

import visualizer.graph.Graph;
import visualizer.graph.Vertex;
import visualizer.ui.GraphPanel;
import visualizer.ui.VertexPanel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class Prim {

    public static String run(GraphPanel graphPanel, VertexPanel startingVertex) {
        List<String> result = new ArrayList<>();

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
                // result.add(closestToStart.getId().compareTo(closestToStart.getPrev().getId()) < 0
                //         ? closestToStart.getId() + "=" + closestToStart.getPrev().getId()
                //         : closestToStart.getPrev().getId() + "=" + closestToStart.getId());
                result.add(closestToStart.getId() + "=" + closestToStart.getPrev().getId());
            }
            graphPanel.getVertexPanel(closestToStart).visit();

            for (var edge : graph.getAdjacentEdges(closestToStart)) {
                var adjacentVertex = edge.getTo();
                if (!graphPanel.getVertexPanel(adjacentVertex).isVisited()) {
                    int newDistance = edge.getWeight();
                    if (adjacentVertex.getDistance().compareTo(newDistance) > 0) {
                        adjacentVertex.setDistance(newDistance);
                        adjacentVertex.setPrev(closestToStart);
                        priorityQueue.decreaseDistance(adjacentVertex.getId(), adjacentVertex.getDistance());
                    }
                }
            }
        }

        graphPanel.repaint();
        StringJoiner joiner = new StringJoiner(", ");
        result.sort(Comparator.naturalOrder());
        for (var vertex : result) {
            joiner.add(vertex);
        }
        return joiner.toString();
    }
}
