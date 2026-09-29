import java.util.*;

public class GraphGenerator {
    private static final Random RANDOM = new Random();

    public static Graph<String> randomErdosRenyi(int n, double p, boolean directed, double maxWeight) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("p must be in [0.0, 1.0]");
        }

        Graph<String> graph = directed ? new DirectedGraph<>() : new UndirectedGraph<>();

        for (int i = 1; i <= n; i++) {
            graph.addVertex("V" + i);
        }

        List<String> vertices = graph.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            int startJ = directed ? 0 : i + 1;
            for (int j = startJ; j < vertices.size(); j++) {
                if (i != j && RANDOM.nextDouble() < p) {
                    double weight = 1.0 + RANDOM.nextDouble() * (maxWeight - 1.0);
                    weight = Math.round(weight * 100.0) / 100.0;
                    graph.addEdge(vertices.get(i), vertices.get(j), weight);
                }
            }
        }
        return graph;
    }
}