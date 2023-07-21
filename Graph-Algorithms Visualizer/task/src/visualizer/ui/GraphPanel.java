package visualizer.ui;

import visualizer.graph.Edge;
import visualizer.graph.Graph;
import visualizer.graph.Vertex;
import visualizer.ui.enums.Algorithm;
import visualizer.ui.enums.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static visualizer.ApplicationRunner.*;
import static visualizer.ui.enums.Algorithm.*;

public class GraphPanel extends JPanel {

    private Graph graph = new Graph();

    private Map<Vertex, VertexPanel> vertexToPanelMapping = new HashMap<>();

    private Map<Edge, EdgePanel> edgeToPanelMapping = new HashMap<>();

    private Map<Algorithm, BiFunction<GraphPanel, VertexPanel, String>> algorithms = new HashMap<>();

    private VertexPanel from, to;

    private Mode mode = Mode.ADD_VERTEX;

    private Algorithm algorithm;

    public GraphPanel() {
        super();
        // setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLayout(null);
        setName("Graph");
        setBackground(Color.BLACK);
        addMouseListener(new GraphMouseAdapter());
        algorithms.put(BFS, visualizer.algos.BFS::run);
        algorithms.put(DFS, visualizer.algos.DFS::run);
        algorithms.put(Dijkstra, visualizer.algos.Dijkstra::run);
        algorithms.put(Prim, visualizer.algos.Prim::run);
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public VertexPanel getVertexPanel(Vertex vertex) {
        return vertexToPanelMapping.get(vertex);
    }

    public EdgePanel getEdgePanel(Edge edge) {
        return edgeToPanelMapping.get(edge);
    }

    public Graph getGraph() {
        return graph;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    private void addVertexHandler(String vertexId, int x, int y) {
        Vertex vertex = new Vertex(vertexId);
        graph.addVertex(vertex);
        vertexToPanelMapping.put(vertex, new VertexPanel(vertex, x, y));
        this.add(vertexToPanelMapping.get(vertex));
    }

    public void addEdgeHandler(VertexPanel vertexPanel) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(GraphPanel.this);

        if (from == null) {
            from = vertexPanel;
            from.select();
            LOGGER.info("from is set");
        } else {
            to = vertexPanel;
            to.select();
            LOGGER.info("to is set");

            String weight = "";
            do {
                weight = JOptionPane.showInputDialog(parentFrame, "Enter weight", "Enter weight", JOptionPane.QUESTION_MESSAGE);
                if (!Objects.isNull(weight)) try {
                    Integer.parseInt(weight);
                } catch (NumberFormatException e) {
                    weight = "";
                }
            } while (!Objects.isNull(weight) && weight.isEmpty());

            if (!Objects.isNull(weight)) {
                connectTwoVertices(Integer.parseInt(weight));
            }

            from.idle();
            to.idle();
            from = to = null;
        }
    }

    private void connectTwoVertices(int weight) {
        Edge edge = new Edge(from.getVertex(), to.getVertex(), weight);
        Edge inverseEdge = new Edge(to.getVertex(), from.getVertex(), weight);
        graph.addEdge(edge);
        graph.addEdge(inverseEdge);

        VertexPanel from = getVertexPanel(edge.getFrom());
        VertexPanel to = getVertexPanel(edge.getTo());

        int x = Math.min(from.getX(), to.getX());
        int y = Math.min(from.getY(), to.getY());
        int width = Math.max(from.getX(), to.getX()) - x + VERTEX_CIRCLE_DIAMETER;
        int height = Math.max(from.getY(), to.getY()) - y + VERTEX_CIRCLE_DIAMETER;

        EdgePanel edgePanel = new EdgePanel(edge, x, y, width, height, false);
        add(edgePanel.getLabel());
        add(edgePanel);

        EdgePanel inverseEdgePanel = new EdgePanel(inverseEdge, x, y, width, height, true);
        add(inverseEdgePanel);

        edgeToPanelMapping.put(edge, edgePanel);
        edgeToPanelMapping.put(inverseEdge, inverseEdgePanel);

        repaint();
    }

    public void removeVertexHandler(Vertex vertex) {
        VertexPanel vertexPanel = vertexToPanelMapping.get(vertex);

        removeIncomingEdges(vertexPanel.getVertex());
        removeOutcomingEdges(vertexPanel.getVertex());

        remove(vertexPanel);

        vertexToPanelMapping.remove(vertex);
        repaint();
    }

    private void removeIncomingEdges(Vertex vertex) {
        var adjacentVertices = graph.getAdjacentEdges(vertex).stream().map(Edge::getTo).collect(Collectors.toSet());

        // remove the related components
        for (var adjacentVertex : adjacentVertices) {
            graph.getAdjacentEdges(adjacentVertex).stream().filter(edge -> edge.getTo().equals(vertex)).forEach(this::removeEdgeComponents);
            // remove edges from the graph
            graph.getAdjacentEdges(adjacentVertex).removeIf(edge -> edge.getTo().equals(vertex));
        }
    }

    private void removeOutcomingEdges(Vertex vertex) {
        // remove the related components
        graph.getAdjacentEdges(vertex).forEach(this::removeEdgeComponents);
        // remove edges from the graph
        graph.getAdjacentEdges(vertex).clear();
    }

    private Edge findInverseEdge(Edge edge) {
        return graph.getAdjacentEdges(edge.getTo()).stream().filter(inverseEdge -> inverseEdge.getTo() == edge.getFrom()).findAny().orElseThrow(IllegalStateException::new);
    }

    public void removeEdgeHandler(EdgePanel edgePanel) {
        // remove inverse edge components
        removeEdgeComponents(findInverseEdge(edgePanel.getEdge()));

        // remove inverse edge
        graph.getAdjacentEdges(edgePanel.getEdge().getTo()).removeIf(inverseEdge -> inverseEdge.getTo() == edgePanel.getEdge().getFrom());

        // remove edge components
        removeEdgeComponents(edgePanel.getEdge());

        // remove edge
        graph.getAdjacentEdges(edgePanel.getEdge().getFrom()).remove(edgePanel.getEdge());

        repaint();
    }

    private void removeEdgeComponents(Edge edge) {
        if (!edgeToPanelMapping.containsKey(edge)) {
            throw new IllegalStateException();
        }

        EdgePanel edgePanel = edgeToPanelMapping.get(edge);

        if (edgePanel.getLabel() != null) {
            remove(edgePanel.getLabel());
        }

        remove(edgePanel);
        edgeToPanelMapping.remove(edge);
    }

    public String runAlgorithm(VertexPanel vertexPanel) {
        String result = algorithms.get(algorithm).apply(this, vertexPanel);// .accept(this, vertexPanel);
        LOGGER.info(result);
        algorithm = null;
        return result;
    }

    public void reset() {
        graph = new Graph();
        vertexToPanelMapping = new HashMap<>();
        edgeToPanelMapping = new HashMap<>();
        from = to = null;
        mode = Mode.ADD_VERTEX;
        algorithm = null;
    }

    // public void resetAlgorithm() {
    //     algorithm = null;
    // }

    class GraphMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            LOGGER.info(mouseEvent.getComponent().getName() + " x:" + mouseEvent.getX() + " y:" + mouseEvent.getY());
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(GraphPanel.this);
            if (mode == Mode.ADD_VERTEX) {
                LOGGER.info("mouseEvent.getX()= " + mouseEvent.getX() + " mouseEvent.getY()=" + mouseEvent.getY());
                String vertexId = "";

                do {
                    vertexId = JOptionPane.showInputDialog(parentFrame, "Enter the Vertex ID (Should be 1 char)", "Vertex", JOptionPane.QUESTION_MESSAGE);

                    if (!Objects.isNull(vertexId) && vertexId.length() > 1) {
                        vertexId = "";
                    }
                } while (vertexId.equals(""));

                if (!Objects.isNull(vertexId)) {
                    addVertexHandler(vertexId, mouseEvent.getX(), mouseEvent.getY());
                    repaint();
                }
            }
        }
    }

    // serialization

    public void setState(GraphState state) {
        vertexToPanelMapping.forEach((k, v) -> {
            v.setState(state.vertices.get(k.getId()));
        });
    }

    public GraphState getState() {
        return new GraphState(this);
    }

    public static class GraphState extends JPanel {
        private final Map<String, VertexPanel.VertexState> vertices = new HashMap<>();

        private GraphState(GraphPanel graphPanel) {
            graphPanel.vertexToPanelMapping.forEach((k, v) -> vertices.put(k.getId(), v.getState()));
        }
    }
}
