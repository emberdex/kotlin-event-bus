package me.monotron.eventbus

class ConditionalEventBus<T : Event> {

    val delegates: MutableList<ConditionalDelegate<T>> = mutableListOf()

    fun addConditionalDelegate(predicate: (T) -> Boolean = { true },
                               delegate: (T) -> Unit) {
        delegates.add(ConditionalDelegate(predicate, delegate))
    }

    operator fun plusAssign(delegate: (T) -> Unit) {
        addConditionalDelegate({ true }, delegate)
    }

    fun triggerEvent(event: T, sender: Any? = null) {

        val tempEvent: T = event
        tempEvent.sender = sender

        delegates.forEach {
            if (it.predicate(tempEvent)) {
                it.delegate(tempEvent)
            }
        }
    }

    fun removeAllDelegates() {
        delegates.clear()
    }
}