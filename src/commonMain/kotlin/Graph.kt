package ghvw.graph

import kotlinx.collections.immutable.*

// G = (V, E)
data class Graph<A>(
    val vertices: PersistentSet<A>,
    val edges: PersistentSet<Edge<A>>
)


fun <A> unweightedEdgeSetOf(vararg pairs: Pair<A, A>): PersistentSet<Edge<A>> =
    pairs
        .map { pair -> UnweightedEdge(pair.first, pair.second) }
        .toPersistentSet()


typealias AdjacencyMap<A> = PersistentMap<A, PersistentSet<Edge<A>>>

typealias U<A> = UnweightedEdge<A>

// this could probably use a Lens
fun <A> Graph<A>.toAdjacencyMap(): AdjacencyMap<A> {
    val map =
        this.vertices
            .fold(persistentMapOf<A, PersistentSet<Edge<A>>>()) { persistentMap, vertex ->
                persistentMap.put(vertex, persistentSetOf<Edge<A>>())
            }

    return this
        .edges
        .fold(map) { persistentMap, edge ->
            val to = persistentMap[edge.to()]
            val from = persistentMap[edge.from()]
            if (to != null && from != null) {
                persistentMap
                    .put(edge.to(), to.add(edge))
                    .put(edge.from(), from.add(edge))
            } else {
                persistentMap
            }
        }
}


// this could probably use a Lens
fun <A> Graph<A>.toDirectedAdjacencyMap(): AdjacencyMap<A> {
    val map =
        this
            .vertices
            .fold(persistentMapOf<A, PersistentSet<Edge<A>>>()) { persistentMap, vertex ->
                persistentMap.put(vertex, persistentSetOf<Edge<A>>())
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


data class TraversalState<A>(
    val visited: MutableSet<A>,
    val memory: Conjable<Pair<A, Edge<A>>>
)


fun <A> traverse(state: TraversalState<A>): (AdjacencyMap<A>) -> Sequence<Pair<A, Edge<A>>> = { map ->
    generateSequence(state, { s ->
        s.memory.peek()?.let {
            map[it.first]?.let { edges ->
                edges
                    .fold(s.copy(memory = s.memory.pop())) { result, edge ->
                        val (vertexFrom, _) = it
                        val vertex = edge.other(vertexFrom)
                        if (result.visited.contains(vertex)) {
                            result
                        } else {
                            result.visited.add(vertex)
                            TraversalState(result.visited, result.memory.conj(vertex to edge))
                        }
                    }
            }
        }
    }).mapNotNull { s -> s.memory.peek() }
}


fun <A> AdjacencyMap<A>.depthFirstTraverseFrom(vertex: A): Sequence<Pair<A, Edge<A>>> =
    this[vertex]
        ?.fold(Pair(mutableSetOf(vertex), mutableListOf<Pair<A, Edge<A>>>())) { pair, edge ->
            val other = edge.other(vertex)
            pair.first.add(other)
            pair.second.add(Pair(other, edge))
            pair
        }.let {
            traverse(
                TraversalState(
                    it?.first ?: mutableSetOf(),
                    Stack(it?.second ?: mutableListOf())
                ))(this)
        }


fun <A> AdjacencyMap<A>.breadthFirstTraverseFrom(vertex: A): Sequence<Pair<A, Edge<A>>> =
    this[vertex]
        ?.fold(Pair(mutableSetOf(vertex), mutableListOf<Pair<A, Edge<A>>>())) { pair, edge ->
            val other = edge.other(vertex)
            pair.first.add(other)
            pair.second.add(Pair(other, edge))
            pair
        }.let {
            traverse(
                TraversalState(
                    it?.first ?: mutableSetOf(),
                    Queue(ArrayDeque(it?.second ?: listOf()))
                ))(this)
        }


//fun <A> AdjacencyMap<A>.shortestPathsTo(vertex: A): Map<A, A> =
//    this
//        .breadthFirstTraverseFrom(vertex)
//        .fold(mutableMapOf()) { v ->
//
//        }
//