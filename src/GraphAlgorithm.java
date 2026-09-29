public interface GraphAlgorithm<T, R> {
    R execute(Graph<T> graph, T start);
}
