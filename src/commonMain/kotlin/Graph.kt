import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf

// G = (V, E)
data class Graph<A>(
    val vertices: PersistentSet<A>,
    val edges: PersistentSet<Edge<A>>
)

typealias AdjacencyMap<A> = PersistentMap<A, PersistentSet<Edge<A>>>

fun <A> Graph<A>.toAdjacencyMap(): AdjacencyMap<A> {
    val map =
        this.vertices
            .fold(AjdacencyMap<A>()) { (map, vertex) ->

            }
}
