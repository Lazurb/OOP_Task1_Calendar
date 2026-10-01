import java.util.*;

public class DijkstraAlgorithm<T> implements GraphAlgorithm<T, Map<T, Double>> {

    @Override
    public Map<T, Double> execute(Graph<T> graph, T start) {
        Map<T, Double> distances = new HashMap<>();

        for (T vertex : graph.getVertices()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }

        distances.put(start, 0.0);

        PriorityQueue<Map.Entry<T, Double>> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );

        priorityQueue.add(new AbstractMap.SimpleEntry<>(start, 0.0));

        while (!priorityQueue.isEmpty()) {
            Map.Entry<T, Double> currentEntry = priorityQueue.poll();
            T currentVertex = currentEntry.getKey();
            double currentDistance = currentEntry.getValue();

            if (currentDistance > distances.get(currentVertex)) {
                continue;
            }

            for (Map.Entry<T, Double> edge : graph.getNeighbors(currentVertex).entrySet()) {
                T neighbor = edge.getKey();
                double weight = edge.getValue();
                double newDistance = currentDistance + weight;

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    priorityQueue.add(new AbstractMap.SimpleEntry<>(neighbor, newDistance));
                }
            }
        }

        return distances;
    }
}