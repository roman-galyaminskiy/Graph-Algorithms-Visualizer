package visualizer.ui.enums;

public enum Mode {
    ADD_VERTEX("Add a Vertex"),
    ADD_EDGE("Add an Edge"),
    REMOVE_VERTEX("Remove a Vertex"),
    REMOVE_EDGE("Remove an Edge"),
    NONE("None");

    public String getText() {
        return text;
    }

    String text;

    Mode(String text) {
        this.text = text;
    }
}
