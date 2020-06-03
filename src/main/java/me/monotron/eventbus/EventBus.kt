package me.monotron.eventbus

class EventBus<T : Event> {

    val delegates: MutableList<(T) -> Unit> = mutableListOf()

    operator fun plusAssign(delegate: (T) -> Unit) {
        delegates.add(delegate)
    }

    fun triggerEvent(event: T, sender: Any? = null) {

        val tempEvent: T = event
        tempEvent.sender = sender

        delegates.forEach { delegate ->
            delegate(tempEvent)
        }
    }

    fun removeAllDelegates() {
        delegates.clear()
    }
}