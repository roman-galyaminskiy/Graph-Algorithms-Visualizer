package visualizer;

import java.util.logging.Logger;

public class ApplicationRunner {
    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 600;
    public static final int VERTEX_CIRCLE_DIAMETER = 50;

    public static final int EDGE_BOX_SIZE = 20;

    static final int EDGE_LINE_THICKNESS = 5;
    public static final Logger LOGGER = Logger.getLogger(ApplicationRunner.class.getName());

    static MainFrame frame;

    public static void main(String[] args) {
        frame = new MainFrame();
    }
}
