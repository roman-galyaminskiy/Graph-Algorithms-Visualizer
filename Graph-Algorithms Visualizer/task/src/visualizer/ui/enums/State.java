package visualizer.ui.enums;

import java.awt.*;

import static java.awt.Color.*;

public enum State {

    IDLE(WHITE), SELECTED(YELLOW), VISITED(GREEN);

    private Color color;

    State(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
