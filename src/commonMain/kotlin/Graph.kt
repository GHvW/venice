package ghvw.graph

import kotlinx.collections.immutable.*

// G = (V, E)
data class Graph<A>(
    val vertices: PersistentSet<A>,
    val edges: PersistentSet<Edge<A>>
)

data class UnweightedGraph<A>(
    val vertices: PersistentSet<A>,
    val edges: PersistentSet<UnweightedEdge<A>>
)

data class WeightedGraph<A>(
    val vertices: PersistentSet<A>,
    val edges: PersistentSet<WeightedEdge<A>>
)


fun <A> unweightedEdgeSetOf(vararg pairs: Pair<A, A>): PersistentSet<UnweightedEdge<A>> =
    pairs
        .map { pair -> UnweightedEdge(pair.first, pair.second) }
        .toPersistentSet()


typealias AdjacencyMap<A> = PersistentMap<A, PersistentSet<Edge<A>>>

typealias U<A> = UnweightedEdge<A>

// this could probably use a Lens
fun <A> UnweightedGraph<A>.toAdjacencyMap(): AdjacencyMap<A> {
    val map =
        this.vertices
            .fold(persistentMapOf<A, PersistentSet<Edge<A>>>()) { persistentMap, vertex ->
                persistentMap.put(vertex, persistentSetOf())
            }

    return this
        .edges
        .fold(map) { persistentMap, edge ->
            val from = persistentMap[edge.from()]
            val to = persistentMap[edge.to()]
            if (from != null && to != null) {
                persistentMap
                    .put(edge.from(), from.add(edge))
                    .put(edge.to(), to.add(UnweightedEdge(edge.to(), edge.from())))
            } else {
                persistentMap
            }
        }
}


// this could probably use a Lens
fun <A> UnweightedGraph<A>.toDirectedAdjacencyMap(): AdjacencyMap<A> {
    val map =
        this
            .vertices
            .fold(persistentMapOf<A, PersistentSet<Edge<A>>>()) { persistentMap, vertex ->
                persistentMap.put(vertex, persistentSetOf())
            }

    return this
        .edges
        .fold(map) { persistentMap, edge ->
            persistentMap[edge.from()]
                ?.let {
                    persistentMap.put(edge.from(), it.add(edge))
                }
                ?: persistentMap
        }
}


data class TraversalState<A>(
    val visited: MutableSet<A>,
    val memory: Conjable<Edge<A>>
)

// TODO - would it be better to treat everything as a directed edge?
// and get away with a sequence of edges instead of a sequence of Pair<vertex, edge> ?
// it would make double the edges in an undirected graph but get rid of the Pairs
// right now, traverse has unnecessary detail for a directed graph traverse. no need to figure out
// to and from because it only goes one way.
fun <A> traverse(state: TraversalState<A>): (AdjacencyMap<A>) -> Sequence<Edge<A>> = { map ->
    generateSequence(state, { s ->
        s.memory.peek()?.let {
            map[it.to()]?.let { edges ->
                edges
                    .fold(s.copy(memory = s.memory.pop())) { result, edge ->
//                        val (vertexFrom, _) = it
                        val vertex = edge.to()
                        if (result.visited.contains(vertex)) {
                            result
                        } else {
                            result.visited.add(vertex)
                            TraversalState(result.visited, result.memory.conj(edge))
                        }
                    }
            }
        }
    }).mapNotNull { s -> s.memory.peek() }
}


fun <A> AdjacencyMap<A>.depthFirstTraverseFrom(vertex: A): Sequence<Edge<A>> =
        traverse(
            TraversalState(
                mutableSetOf(vertex),
                Stack(mutableListOf(U(vertex, vertex))) // Unweighted Edge as the start?
            ))(this)


fun <A> AdjacencyMap<A>.breadthFirstTraverseFrom(vertex: A): Sequence<Edge<A>> =
    traverse(
        TraversalState(
            mutableSetOf(vertex),
            Queue(ArrayDeque(listOf(U(vertex, vertex))))
        ))(this)


fun <A> AdjacencyMap<A>.shortestPathsTo(vertex: A): Map<A, A> =
    this
        .breadthFirstTraverseFrom(vertex)
        .fold(mutableMapOf()) { map, edge ->
            map[edge.to()] = edge.from()
            map
        }

fun <A> shortestPath(paths: Map<A, A>, from: A): Sequence<A> =
    generateSequence(from) { vertex ->
        paths[vertex]?.let {
            if (it != vertex) {
                it
            } else {
                null
            }
        }
    }
