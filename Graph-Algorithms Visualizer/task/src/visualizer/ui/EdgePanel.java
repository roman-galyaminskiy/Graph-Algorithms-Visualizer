package visualizer.ui;

import visualizer.graph.Edge;
import visualizer.ui.enums.Mode;
import visualizer.ui.enums.State;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static visualizer.ApplicationRunner.*;
import static visualizer.ApplicationRunner.EDGE_BOX_SIZE;

public class EdgePanel extends JPanel {

    private final Edge edge;
    private State state = State.IDLE;
    private JLabel label;
    private Point topLeftCorner;
    private Point bottomRightCorner;

    @Override
    public String toString() {
        return "Edge <" + edge.getFrom().getId() + " -> " + edge.getTo().getId() + ">";
    }

    public EdgePanel(Edge edge, int x, int y, int width, int height, boolean isInverse) {
        this.topLeftCorner = new Point(x, y);
        this.bottomRightCorner = new Point(x + width, y + height);
        this.edge = edge;
        setLayout(null);
        setName(toString());
        LOGGER.info(toString());
        setBackground(state.getColor());
        addMouseListener(new EdgeMouseAdapter());
        setLocation(x + (width - EDGE_BOX_SIZE)/2, y + (height - EDGE_BOX_SIZE)/2);
        setSize(EDGE_BOX_SIZE, EDGE_BOX_SIZE);

        if (!isInverse) {
            label = new JLabel(edge.getWeight().toString());
            label.setName("EdgeLabel <" + edge.getFrom().getId() + " -> " + edge.getTo().getId() + ">");
            label.setForeground(Color.RED);
            label.setBounds(this.getBounds());
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
        }
    }

    public JLabel getLabel() {
        return label;
    }

    public Edge getEdge() {
        return edge;
    }

    // public void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    //     if (g instanceof Graphics2D g2d) {
    //         g2d.setStroke(new BasicStroke(10));
    //     }
    //     g.setColor(state.getColor());
    //     g.drawLine(0, 0, 10, 10);
    // }

    class EdgeMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            LOGGER.info(mouseEvent.getComponent().getName() + " x:" + mouseEvent.getX() + " y:" + mouseEvent.getY());
            GraphPanel graphPanel = (GraphPanel) EdgePanel.this.getParent();
            if (graphPanel.getMode() == Mode.REMOVE_EDGE) {
                graphPanel.removeEdgeHandler(EdgePanel.this);
            }
        }
    }
}
