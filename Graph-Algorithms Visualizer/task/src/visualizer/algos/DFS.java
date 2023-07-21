package visualizer.algos;

import visualizer.graph.Edge;
import visualizer.graph.Graph;
import visualizer.ui.EdgePanel;
import visualizer.ui.GraphPanel;
import visualizer.ui.VertexPanel;

import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class DFS {

    public static String run(GraphPanel graphPanel, VertexPanel startingVertex) {
        StringJoiner joiner = new StringJoiner(" -> ");

        recursive(graphPanel, startingVertex, joiner);

        return "DFS : " + joiner;
    }

    private static void recursive(GraphPanel graphPanel, VertexPanel from, StringJoiner joiner) {
        from.visit();
        joiner.add(from.getVertex().getId());

        Graph graph = graphPanel.getGraph();
        if (graph.getAdjacentEdges(from.getVertex()) != null) {
            List<EdgePanel> sortedEdges = graph.getAdjacentEdges(from.getVertex()).stream()
                    .sorted(Comparator.comparingInt(Edge::getWeight))
                    .map(graphPanel::getEdgePanel)
                    .toList();

            for (var edge : sortedEdges) {
                if (!graphPanel.getVertexPanel(edge.getEdge().getTo()).isVisited()) {
                    recursive(graphPanel, graphPanel.getVertexPanel(edge.getEdge().getTo()), joiner);
                }
            }
        }
    }
}
