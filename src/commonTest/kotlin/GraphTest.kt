import ghvw.graph.Graph
import ghvw.graph.UnweightedEdge
import ghvw.graph.toAdjacencyMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import kotlin.test.*

class GraphTest {
    private val graph = Graph(
        persistentSetOf(5, 7, 9, 10, 20, 21, 11, 30),
        persistentSetOf(
            UnweightedEdge(5, 7),
            UnweightedEdge(5, 9),
            UnweightedEdge(5, 10),
            UnweightedEdge(7, 20),
            UnweightedEdge(7, 21),
            UnweightedEdge(9, 11),
            UnweightedEdge(10, 11),
            UnweightedEdge(11, 30)
        )
    )

    @Test
    fun graphToAdjacencyMap_SuccessfullyCreatesAdjMap() {
        val map = graph.toAdjacencyMap()

        assertEquals(persistentMapOf(5 to persistentSetOf(9, 7)), map)
    }
}
