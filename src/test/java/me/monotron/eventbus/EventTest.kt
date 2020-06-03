package me.monotron.eventbus

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class EventTest {

    lateinit var clock: Clock

    @BeforeEach
    fun beforeEach() {
        clock = Clock.fixed(Instant.EPOCH, ZoneId.systemDefault())
    }

    @Test
    fun `event trigger timestamp is initialised when event constructed`() {
        val event = Event(clock = clock)

        assertEquals(LocalDateTime.now(clock), event.timeTriggered)
    }

    @Test
    fun `event sender is null when not passed in constructor`() {
        val event = Event()

        assertNull(event.sender)
    }

    @Test
    fun `event sender is set when passed in constructor`() {
        val event = Event(sender = "Test")

        assertEquals("Test", event.sender)
    }
}