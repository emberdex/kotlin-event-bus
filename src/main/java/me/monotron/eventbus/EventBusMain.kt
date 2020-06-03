package me.monotron.eventbus

class EventBusMain {

    companion object {

        private var eventBus: EventBus<TestEvent> = EventBus()

        @JvmStatic
        fun main(args: Array<String>) {

            eventBus += {
                println("data = ${it.someExtraData}")
            }

            eventBus.triggerEvent(TestEvent("Test Data"), this)
        }
    }
}