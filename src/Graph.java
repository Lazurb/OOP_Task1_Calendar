import java.util.*;

public abstract class Graph<T> {
    protected final Map<T, Map<T, Double>> adjacencyList = new LinkedHashMap<>();

    public void addVertex(T vertex) {
        adjacencyList.putIfAbsent(vertex, new LinkedHashMap<>());
    }

    public abstract void addEdge(T u, T v, double weight);

    public abstract void removeEdge(T u, T v);

    public List<T> getVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    public Map<T, Double> getNeighbors(T vertex) {
        return adjacencyList.getOrDefault(vertex, Collections.emptyMap());
    }

    public boolean hasEdge(T u, T v) {
        return adjacencyList.containsKey(u) && adjacencyList.get(u).containsKey(v);
    }

    public abstract String getTypeName();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Граф (").append(getTypeName()).append("), вершин: ")
                .append(getVertexCount()).append("\n");

        for (Map.Entry<T, Map<T, Double>> entry : adjacencyList.entrySet()) {
            T u = entry.getKey();
            Map<T, Double> neighbors = entry.getValue();
            sb.append("  [").append(u).append("] ");
            if (neighbors.isEmpty()) {
                sb.append("(изолированная)");
            } else {
                List<String> edges = new ArrayList<>();
                for (Map.Entry<T, Double> e : neighbors.entrySet()) {
                    edges.add(getEdgeSymbol() + e.getKey() + "(" + e.getValue() + ")");
                }
                sb.append(String.join(", ", edges));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    protected abstract String getEdgeSymbol();
}
