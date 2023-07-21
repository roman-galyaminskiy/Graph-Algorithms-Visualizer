package visualizer.ui;

import visualizer.MainFrame;
import visualizer.graph.Vertex;
import visualizer.ui.enums.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static visualizer.ApplicationRunner.LOGGER;
import static visualizer.ApplicationRunner.VERTEX_CIRCLE_DIAMETER;

public class VertexPanel extends JPanel {

    private final Vertex vertex;

    private final int x;

    private final int y;

    private State state;
    private int diameter  = VERTEX_CIRCLE_DIAMETER;
    private JLabel label;

    public VertexPanel(Vertex vertex, int x, int y) {
        super();

        this.x = x;
        this.y = y;
        this.vertex = vertex;
        this.state = State.IDLE;
        setLocation(x - diameter/2, y - diameter/2);
        setName("Vertex " + vertex.getId());
        setLayout(null);
        setOpaque(false);
        setSize(diameter, diameter);
        LOGGER.info("Vertex bounds: x1:" + (x - diameter/2) + " y1:" + (y - diameter/2) + " x2:" + (x + diameter/2) + " y2:" + (y + diameter/2));
        label = new JLabel(String.valueOf(vertex.getId()));
        label.setText(vertex.getId());
        label.setName("VertexLabel " + vertex.getId());
        label.setBounds(0, 0, diameter, diameter);
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        add(label);
        addMouseListener(new VertexMouseAdapter());
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void idle() {
        state = State.IDLE;
        setBackground(state.getColor());
        repaint();
    }

    public void select() {
        state = State.SELECTED;
        setBackground(state.getColor());
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(state.getColor());
        g.fillOval(0, 0, diameter, diameter);
        label.paint(g);
    }

    public boolean isVisited() {
        return state == State.VISITED;
    }

    public void visit() {
        state = State.VISITED;
        setBackground(state.getColor());
        repaint();
    }

    class VertexMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            MainFrame parentFrame = (MainFrame) SwingUtilities.getWindowAncestor(VertexPanel.this);
            LOGGER.info(mouseEvent.getComponent().getName() + " x:" + mouseEvent.getX() + " y:" + mouseEvent.getY());
            GraphPanel graphPanel = (GraphPanel) VertexPanel.this.getParent();
            LOGGER.info(graphPanel.getMode().getText());

            switch (graphPanel.getMode()) {
                case REMOVE_VERTEX -> {
                    graphPanel.removeVertexHandler(VertexPanel.this.vertex);
                }
                case ADD_EDGE -> {
                    graphPanel.addEdgeHandler(VertexPanel.this);
                }
            }

            if (graphPanel.getAlgorithm() != null) {
                parentFrame.updateAlgorithmLabelText("Please wait...");

                ActionListener listener = new ActionListener(){
                    public void actionPerformed(ActionEvent event){
                        parentFrame.updateAlgorithmLabelText(graphPanel.runAlgorithm(VertexPanel.this));
                    }
                };
                Timer timer = new Timer(1000, listener);
                timer.setRepeats(false);
                timer.start();

            }
        }
    }

    // for serialization

    public VertexState getState() {
        return new VertexState(this);
    }

    public void setState(VertexState state) {
        this.state = state.state;
        getVertex().setDistance(state.distance);
        getVertex().setPrev(state.prev);
    }

    static class VertexState {
        private final State state;

        private final Integer distance;

        private final Vertex prev;

        private VertexState(VertexPanel vertexPanel) {
            this.state = vertexPanel.state;
            this.distance = vertexPanel.getVertex().getDistance();
            this.prev = vertexPanel.getVertex().getPrev();
        }

        @Override
        public String toString() {
            return "VertexState visited=" + state + " distance=" + distance + "prev=" + prev;
        }
    }
}
