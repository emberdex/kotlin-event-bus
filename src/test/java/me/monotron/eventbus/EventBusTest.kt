package me.monotron.eventbus

import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor

internal class EventBusTest {

    object TestEvent : Event()

    lateinit var sut: EventBus<TestEvent>

    @BeforeEach
    fun beforeEach() {
        sut = EventBus()
    }

    @Test
    fun `operator overload plusAssign adds delegate to list`() {
        val delegate: (TestEvent) -> Unit = {}

        sut += delegate

        assert(sut.delegates.contains(delegate))
    }

    @Test
    fun `event trigger calls all delegates`() {
        val delegate: (TestEvent) -> Unit = spy()

        for (i in 1..10) {
            sut += delegate
        }

        sut.triggerEvent(TestEvent)

        verify(delegate, times(10)).invoke(TestEvent)
    }

    @Test
    fun `event trigger with null sender does not set sender field in event`() {
        val delegate: (TestEvent) -> Unit = spy()
        sut += delegate

        val eventCaptor: KArgumentCaptor<TestEvent> = argumentCaptor()

        sut.triggerEvent(TestEvent)

        verify(delegate).invoke(eventCaptor.capture())

        assertNull(eventCaptor.firstValue.sender)
    }

    @Test
    fun `event trigger with sender sets sender field in event`() {
        val delegate: (TestEvent) -> Unit = spy()
        sut += delegate

        val eventCaptor: KArgumentCaptor<TestEvent> = argumentCaptor()

        sut.triggerEvent(TestEvent, "Test Sender")

        verify(delegate).invoke(eventCaptor.capture())

        assertEquals("Test Sender", eventCaptor.firstValue.sender)
    }

    @Test
    fun `remove all delegates method removes delegates`() {
        val delegate: (TestEvent) -> Unit = {}
        sut += delegate

        sut.removeAllDelegates()

        assert(sut.delegates.isEmpty())
    }
}