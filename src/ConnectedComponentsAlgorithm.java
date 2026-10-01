import java.util.*;

public class ConnectedComponentsAlgorithm<T> implements GraphFullAlgorithm<T, List<List<T>>> {

    @Override
    public List<List<T>> execute(Graph<T> graph) {
        List<List<T>> components = new ArrayList<>();
        Set<T> visited = new HashSet<>();

        for (T vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                List<T> component = new ArrayList<>();
                findComponent(graph, vertex, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private void findComponent(Graph<T> graph, T start, Set<T> visited, List<T> component) {
        Queue<T> queue = new ArrayDeque<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            T current = queue.poll();
            component.add(current);

            for (T neighbor : graph.getNeighbors(current).keySet()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }

            for (T vertex : graph.getVertices()) {
                if (!visited.contains(vertex) && graph.hasEdge(vertex, current)) {
                    visited.add(vertex);
                    queue.add(vertex);
                }
            }
        }
    }
}