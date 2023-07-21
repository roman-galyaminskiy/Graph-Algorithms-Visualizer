package visualizer.ui.enums;

public enum Algorithm {
    DFS("Depth-First Search"),
    BFS("Breadth-First Search"),
    Dijkstra("Dijkstra's Algorithm"),
    Prim("Prim's Algorithm");

    public String getText() {
        return text;
    }

    String text;

    Algorithm(String text) {
        this.text = text;
    }
}
