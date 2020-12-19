package ghvw.graph

interface Edge<A> {
    fun to(): A
    fun from(): A
    fun other(item: A): A
}


data class UnweightedEdge<A>(
    val v: A,
    val w: A) : Edge<A> {

    override fun to(): A =
        this.v

    override fun from(): A =
        this.w

    override fun other(item: A): A =
        if (item == this.v) {
            this.w
        } else {
            this.v
        }
}


data class WeightedEdge<A>(
    val edge: UnweightedEdge<A>,
    val weight: Int) : Edge<A> {

    override fun to(): A =
        this.edge.v

    override fun from(): A =
        this.edge.w

    override fun other(item: A): A =
        if (item == this.edge.v) {
            this.edge.w
        } else {
            this.edge.w
        }
}
