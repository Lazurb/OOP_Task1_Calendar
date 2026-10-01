import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("  Демонстрация работы библиотеки для работы с графами");
        System.out.println("1. Ручное создание неориентированного графа:");
        Graph<String> manualGraph = new UndirectedGraph<>();

        manualGraph.addVertex("A");
        manualGraph.addVertex("B");
        manualGraph.addVertex("C");
        manualGraph.addVertex("D");

        manualGraph.addEdge("A", "B", 5.0);
        manualGraph.addEdge("A", "C", 3.0);
        manualGraph.addEdge("B", "D", 2.0);
        manualGraph.addEdge("C", "D", 4.0);

        System.out.println(manualGraph);

        System.out.println("2. Запуск алгоритмов на ручном графе (старт из вершины 'A'):");

        GraphAlgorithm<String, List<String>> bfs = new BfsAlgorithm<>();
        System.out.println("   - Поиск в ширину (BFS): " + bfs.execute(manualGraph, "A"));

        GraphAlgorithm<String, List<String>> dfs = new DfsAlgorithm<>();
        System.out.println("   - Поиск в глубину (DFS): " + dfs.execute(manualGraph, "A"));

        GraphAlgorithm<String, Map<String, Double>> dijkstra = new DijkstraAlgorithm<>();
        Map<String, Double> shortestPaths = dijkstra.execute(manualGraph, "A");
        System.out.println("   - Кратчайшие пути из 'A' (алгоритм Дейкстры):");
        for (Map.Entry<String, Double> entry : shortestPaths.entrySet()) {
            String dist = entry.getValue().equals(Double.POSITIVE_INFINITY) ? "недостижима" : String.valueOf(entry.getValue());
            System.out.println("     До вершины " + entry.getKey() + ": " + dist);
        }

        System.out.println("3. Генерация случайного графа (модель Эрдёша-Реньи):");
        System.out.println("   Параметры: 6 вершин, вероятность ребра = 0.4, неориентированный, макс. вес = 10");

        Graph<String> randomGraph = GraphGenerator.randomErdosRenyi(6, 0.4, false, 10.0);
        System.out.println(randomGraph);

        System.out.println("4. Поиск компонент связности в сгенерированном графе:");
        GraphFullAlgorithm<String, List<List<String>>> componentsAlg = new ConnectedComponentsAlgorithm<>();
        List<List<String>> components = componentsAlg.execute(randomGraph);

        for (int i = 0; i < components.size(); i++) {
            System.out.println("   Компонента связности " + (i + 1) + ": " + components.get(i));
        }

        System.out.println("  Демонстрация успешно завершена");

    }
}