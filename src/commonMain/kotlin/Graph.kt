package ghvw.graph

import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentHashSetOf

// G = (V, E)
data class Graph<A>(
    val vertices: PersistentSet<A>,
    val edges: PersistentSet<Edge<A>>
)

typealias AdjacencyMap<A> = PersistentMap<A, PersistentSet<Edge<A>>>

// this could probably use a Lens
fun <A> Graph<A>.toAdjacencyMap(): AdjacencyMap<A> {
    val map =
        this.vertices
            .fold(persistentHashMapOf<A, PersistentSet<Edge<A>>>()) { persistentMap, vertex ->
                return persistentMap.put(vertex, persistentHashSetOf<Edge<A>>())
            }

    return this
        .edges
        .fold(map) { persistentMap, edge ->
            val to = persistentMap[edge.to()]
            val from = persistentMap[edge.from()]
            if (to != null && from != null) {
                persistentMap.put(edge.to(), to.add(edge)).put(edge.from(), from.add(edge))
            } else {
                persistentMap
            }
        }
}


// this could probably use a Lens
fun <A> Graph<A>.toDirectedAdjacencyMap(): AdjacencyMap<A> {
    val map =
        this.vertices
            .fold(persistentHashMapOf<A, PersistentSet<Edge<A>>>()) { persistentMap, vertex ->
                return persistentMap.put(vertex, persistentHashSetOf<Edge<A>>())
            }

    return this
        .edges
        .fold(map) { persistentMap, edge ->
            val to = persistentMap[edge.to()]
            if (to != null) {
                persistentMap.put(edge.to(), to.add(edge))
            } else {
                persistentMap
            }
        }
}