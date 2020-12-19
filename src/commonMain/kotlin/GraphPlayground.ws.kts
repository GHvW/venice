import ghvw.graph.Graph
import ghvw.graph.UnweightedEdge
import kotlinx.collections.immutable.persistentSetOf

val graph = Graph(
    persistentSetOf(1, 2, 3),
    persistentSetOf(
        UnweightedEdge(1, 2),
        UnweightedEdge(2, 3))
)

graph