package visualizer.algos;

import visualizer.graph.Vertex;
import visualizer.ui.VertexPanel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IndexedMinPriorityQueue {

    private final int capacity;
    private int size = 0;
    private final Vertex[] heap;
    private final Map<String, Integer> map;

    private Comparator<Vertex> comparator = Comparator.comparing(Vertex::getDistance).reversed();

    public IndexedMinPriorityQueue(int capacity) {
        this.capacity = capacity;
        this.heap = new Vertex[capacity];
        this.map = new HashMap<>(capacity);
    }

    private void swap(int from, int to) {
        // System.out.printf("%d<->%d\n",storage[from], storage[to]);
        var buf = heap[from];
        map.put(heap[to].getId(), from);
        heap[from] = heap[to];
        map.put(buf.getId(), to);
        heap[to] = buf;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * (i + 1);
    }

    private void siftUp(int i) {
        if (heap[parent(i)] != null) {
            if (comparator.compare(heap[parent(i)], heap[i]) < 0) {
                swap(i, parent(i));
                siftUp(parent(i));
            }
        }
    }

    private void siftDown(int i) {
        int largest = i;
        if (left(i) < size && heap[left(i)] != null) {
            largest = comparator.compare(heap[left(i)], heap[largest]) > 0 ? left(i) : largest;
        }
        if (right(i) < size && heap[right(i)] != null) {
            largest = comparator.compare(heap[right(i)], heap[largest]) > 0 ? right(i) : largest;
        }
        if (i != largest) {
            swap(i, largest);
            siftDown(largest);
        }
    }

    public Vertex peek() {
        return heap.length == 0 ? null : heap[0];
    }

    public void push(Vertex object) {
        if (Objects.isNull(object)) {
            throw new NullPointerException();
        }

        if (size == capacity) {
            throw new IndexOutOfBoundsException();
        }

        heap[size] = object;
        map.put(object.getId(), size);
        siftUp(size);
        size++;
    }

    public Vertex pop() {
        if (size == 0) {
            return null;
        }

        Vertex head = heap[0];
        size--;
        swap(0, size);
        siftDown(0);

        return head;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        if (size > 0) {
            builder.append(heap[0]);
            for (int i = 1; i < size; i++) {
                builder.append(" ").append(heap[i]);
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void decreaseDistance(String id, Integer distance) {
        Integer vertexIndex = map.get(id);
        heap[vertexIndex].setDistance(distance);
        siftUp(vertexIndex);
    }
}
