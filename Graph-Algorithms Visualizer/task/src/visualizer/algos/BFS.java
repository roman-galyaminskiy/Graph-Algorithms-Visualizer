package visualizer.algos;

import visualizer.graph.Graph;
import visualizer.ui.GraphPanel;
import visualizer.ui.VertexPanel;

import java.util.*;

import static visualizer.ApplicationRunner.LOGGER;

public class BFS {

    // private final GraphPanel graphPanel;

    // timer = new Timer(speed, this);
    // timer.setInitialDelay(pause);
    // timer.start();

    public static String run(GraphPanel graphPanel, VertexPanel startingVertex) {
        StringJoiner joiner = new StringJoiner(" -> ");

        Deque<VertexPanel> deque = new ArrayDeque<>();
        deque.push(startingVertex);

        while (!deque.isEmpty()) {
            VertexPanel vertex = deque.pop();
            LOGGER.info(vertex.toString());

            if (!vertex.isVisited()) {
                joiner.add(vertex.getVertex().getId());
                vertex.visit();

                Graph graph = graphPanel.getGraph();
                if (graph.getAdjacentEdges(vertex.getVertex()) != null) {
                    List<VertexPanel> adjacentVertices = graph.getAdjacentEdges(vertex.getVertex()).stream()
                            .map(graphPanel::getEdgePanel)
                            .sorted(Comparator.comparingInt(edgePanel -> edgePanel.getEdge().getWeight()))
                            .map(edgePanel -> graphPanel.getVertexPanel(edgePanel.getEdge().getTo())).toList();
                    LOGGER.info(adjacentVertices.toString());
                    deque.addAll(adjacentVertices);
                }
            }
            graphPanel.repaint();
        }

        return "BFS : " + joiner;
    }
}
