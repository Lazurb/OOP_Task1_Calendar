import java.util.*;

public class BfsAlgorithm<T> implements GraphAlgorithm<T, List<T>> {

    @Override
    public List<T> execute(Graph<T> graph, T start) {
        if (!graph.getVertices().contains(start)) {
            return Collections.emptyList();
        }

        Set<T> visited = new HashSet<>();
        Queue<T> queue = new ArrayDeque<>();
        List<T> order = new ArrayList<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            T current = queue.poll();
            order.add(current);

            for (T neighbor : graph.getNeighbors(current).keySet()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return order;
    }
}