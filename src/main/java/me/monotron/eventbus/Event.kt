package me.monotron.eventbus

import java.time.Clock
import java.time.LocalDateTime

open class Event(var sender: Any? = null, val clock: Clock = Clock.systemUTC()) {

    var timeTriggered: LocalDateTime = LocalDateTime.now(clock)
}
