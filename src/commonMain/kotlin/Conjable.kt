package ghvw.graph

// TODO - if you can, make Conjable "Conjable<F<A>>" at some point
// Arrow's Kinds might be an option,
// where Conjable is Conjable<F> and we use fun <A> Kind<F, A>.conj(item: A): Kind<F, A> or something
interface Conjable<A> {
    fun conj(item: A): Conjable<A>
    fun peek(): A?
    fun pop(): Conjable<A>
}

// TODO - keep going cheap or just use immutable List?
class Stack<A>(private val coll: MutableList<A>) : Conjable<A> {

    override fun conj(item: A): Conjable<A> {
        this.coll.add(item)
        return Stack(this.coll)
    }

    override fun peek(): A? =
        this.coll.lastOrNull()

    override fun pop(): Conjable<A> =
        if (this.coll.isEmpty()) {
            this
        } else {
            this.coll.removeLast()
            Stack(this.coll)
        }
}


// TODO - keep going cheap or just use immutable List?
class Queue<A>(private val coll: ArrayDeque<A>) : Conjable<A> {

    override fun conj(item: A): Conjable<A> {
        this.coll.add(item)
        return Queue(this.coll)
    }

    override fun peek(): A? =
        this.coll.firstOrNull()

    override fun pop(): Conjable<A> =
        if (this.coll.isEmpty()) {
            this
        } else {
            this.coll.removeFirst()
            Queue(this.coll)
        }
}
//
//fun <A> conj(stack: MutableList<A>, item: A): MutableList<A> {
//    stack.add(item)
//    return stack
//}
//
//fun <A> conj(queue: ArrayDeque<A>, item: A): ArrayDeque<A> {
//    queue.add(item)
//    return queue
//}