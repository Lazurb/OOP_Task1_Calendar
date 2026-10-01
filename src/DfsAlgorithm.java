import java.util.*;

public class DfsAlgorithm<T> implements GraphAlgorithm<T, List<T>> {

    @Override
    public List<T> execute(Graph<T> graph, T start) {
        if (!graph.getVertices().contains(start)) {
            return Collections.emptyList();
        }

        Set<T> visited = new HashSet<>();
        List<T> visitOrder = new ArrayList<>();

        dfsTraverse(graph, start, visited, visitOrder);

        return visitOrder;
    }

    private void dfsTraverse(Graph<T> graph, T current, Set<T> visited, List<T> visitOrder) {
        visited.add(current);
        visitOrder.add(current);

        for (T neighbor : graph.getNeighbors(current).keySet()) {
            if (!visited.contains(neighbor)) {
                dfsTraverse(graph, neighbor, visited, visitOrder);
            }
        }
    }
}