sealed class Edge<out A>


data class UnweightedEdge<A>(
    val v: A,
    val w: A) : Edge<A>()


data class WeightedEdge<A>(
    val edge: UnweightedEdge<A>,
    val weight: Int) : Edge<A>()


fun <A> Edge<A>.to(): A = when(this) {
    is UnweightedEdge -> this.v
    is WeightedEdge -> this.edge.v
}


fun <A> Edge<A>.from(): A = when(this) {
    is UnweightedEdge -> this.w
    is WeightedEdge -> this.edge.w
}


fun <A> Edge<A>.other(v: A): A = when(this) {
    is UnweightedEdge ->
        if (v == this.v) this.w
        else this.v
    is WeightedEdge ->
        if (v == this.edge.v) this.edge.w
        else this.edge.v
}

