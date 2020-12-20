import ghvw.graph.*
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import kotlin.test.*

typealias U<A> = UnweightedEdge<A>

class GraphTest {
    private val graph = UnweightedGraph(
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
                U(7, 5)
            ),
            9 to persistentSetOf(
                U(9, 5),
                U(9, 11),
            ),
            10 to persistentSetOf(
                U(10, 5),
                U(10, 11)
            ),
            11 to persistentSetOf(
                U(11, 9),
                U(11, 10),
                U(11, 30),
            ),
            20 to persistentSetOf(
                U(20, 7)
            ),
            21 to persistentSetOf(
                U(21, 7),
                U(21, 30)
            ),
            30 to persistentSetOf(
                U(30, 11),
                U(30, 21),
            ),
        )

        assertEquals(expected.count(), result.count())
        assertEquals(expected, result)
    }

    @Test
    fun toDirectedAdjacencyMap_CorrectlyConstructsAdjMap() {
        val result = graph.toDirectedAdjacencyMap()

        val expected = persistentMapOf(
            5 to persistentSetOf(
                U(5, 7),
                U(5, 9),
                U(5, 10)
            ),
            7 to persistentSetOf(
                U(7, 20),
                U(7, 21),
            ),
            9 to persistentSetOf(
                U(9, 11),
            ),
            10 to persistentSetOf(
                U(10, 11)
            ),
            11 to persistentSetOf(
                U(11, 30),
            ),
            20 to persistentSetOf(),
            21 to persistentSetOf(),
            30 to persistentSetOf(),
        )

//        assertEquals(expected, result)
        assertEquals(expected.count(), result.count())
    }

    @Test
    fun depthFirstTraverseTest() { // soft test
        val result =
            graph
                .toAdjacencyMap()
                .depthFirstTraverseFrom(5)
                .toList()

//        assertEquals(listOf(U(5, 10), U(5, 12)), result)
        assertEquals(8, result.count())
    }

    @Test
    fun breadthFirstTraverseTest() { // soft test
        val result =
            graph
                .toAdjacencyMap()
                .breadthFirstTraverseFrom(5)
                .toList()

//        assertEquals(listOf(U(5, 10), U(10, 12)), result)
        assertEquals(8, result.count())
    }

    @Test
    fun correctShortestPathFrom21To5() {
        val result =
            graph
                .toAdjacencyMap()
                .shortestPathsTo(5)
                .let { shortestPath(it, 21) }
                .toList()

        val expected = listOf(21, 7, 5)

        assertEquals(expected, result)
    }
}
