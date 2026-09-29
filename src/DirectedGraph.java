public class DirectedGraph<T> extends Graph<T> {

    @Override
    public void addEdge(T u, T v, double weight) {
        addVertex(u);
        addVertex(v);
        adjacencyList.get(u).put(v, weight);
    }

    @Override
    public void removeEdge(T u, T v) {
        if (adjacencyList.containsKey(u)) {
            adjacencyList.get(u).remove(v);
        }
    }

    @Override
    public String getTypeName() {
        return "Ориентированный";
    }

    @Override
    protected String getEdgeSymbol() {
        return "->";
    }
}