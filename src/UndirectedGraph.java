public class UndirectedGraph<T> extends Graph<T> {

    @Override
    public void addEdge(T u, T v, double weight) {
        addVertex(u);
        addVertex(v);
        adjacencyList.get(u).put(v, weight);
        adjacencyList.get(v).put(u, weight);
    }

    @Override
    public void removeEdge(T u, T v) {
        if (adjacencyList.containsKey(u)) {
            adjacencyList.get(u).remove(v);
        }
        if (adjacencyList.containsKey(v)) {
            adjacencyList.get(v).remove(u);
        }
    }

    @Override
    public String getTypeName() {
        return "Неориентированный";
    }

    @Override
    protected String getEdgeSymbol() {
        return "-";
    }
}