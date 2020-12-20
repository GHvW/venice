import ghvw.graph.*
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import kotlin.test.*

typealias U<A> = UnweightedEdge<A>

class GraphTest {
    private val graph = Graph(
        persistentSetOf(5, 7, 9, 10, 20, 21, 11, 30),
        unweightedEdgeSetOf(
            5 to 7,
            5 to 9,
            5 to 10,
            7 to 20,
            7 to 21,
            9 to 11,
            10 to 11,
            11 to 30,
            21 to 30,
        )
    )

    @Test
    fun graphToAdjacencyMap_SuccessfullyCreatesAdjMap() {
        val result = graph.toAdjacencyMap()

        val expected = persistentMapOf(
            5 to persistentSetOf(
                U(5, 7),
                U(5, 9),
                U(5, 10)
            ),
            7 to persistentSetOf(
                U(7, 20),
                U(7, 21),
                U(5, 7)
            ),
            9 to persistentSetOf(
                U(5, 9),
                U(9, 11),
            ),
            10 to persistentSetOf(
                U(5, 10),
                U(10, 11)
            ),
            11 to persistentSetOf(
                U(9, 11),
                U(10, 11),
                U(11, 30),
            ),
            20 to persistentSetOf(
                U(7, 20)
            ),
            21 to persistentSetOf(
                U(7, 21),
                U(21, 30)
            ),
            30 to persistentSetOf(
                U(11, 30),
                U(21, 30),
            ),

        )

        assertEquals(expected.count(), result.count())
        assertEquals(expected, result)
    }

    @Test
    fun depthFirstTraverseTest() {
        val result =
            graph
                .toAdjacencyMap()
                .depthFirstTraverseFrom(5)
                .toList()

        assertEquals(listOf(5, 10, 12), result)
    }

    @Test
    fun breadthFirstTraverseTest() {
        val result =
            graph
                .toAdjacencyMap()
                .breadthFirstTraverseFrom(5)
                .toList()

        assertEquals(listOf(5, 10, 12), result)
    }
}
