package me.monotron.eventbus

import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ConditionalEventBusTest {

    object TestEvent : Event()

    lateinit var sut: ConditionalEventBus<TestEvent>

    @BeforeEach
    fun beforeEach() {
        sut = ConditionalEventBus()
    }

    @Test
    fun `add conditional delegate function with no predicate adds delegate which always executes`() {
        val delegate: (TestEvent) -> Unit = {}

        sut.addConditionalDelegate(delegate = delegate)

        assertTrue(sut.delegates[0].predicate(TestEvent))
    }

    @Test
    fun `add conditional delegate function with specified predicate adds delegate which conditionally executes`() {
        val delegate: (TestEvent) -> Unit = {}

        sut.addConditionalDelegate(predicate = { true },  delegate = delegate)
        sut.addConditionalDelegate(predicate = { false }, delegate = delegate)

        assertTrue(sut.delegates[0].predicate(TestEvent))
        assertFalse(sut.delegates[1].predicate(TestEvent))
    }

    @Test
    fun `plus assign operator adds delegate which always executes`() {
        sut += {}

        assertTrue(sut.delegates[0].predicate(TestEvent))
    }

    @Test
    fun `trigger event method triggers delegates whose predicates evaluate to true`() {
        val delegate: (TestEvent) -> Unit = spy()
        sut += delegate

        sut.triggerEvent(TestEvent)

        verify(delegate).invoke(eq(TestEvent))
    }

    @Test
    fun `trigger event method does not trigger delegates whose predicates evaluate to false`() {
        val delegate: (TestEvent) -> Unit = spy()
        sut.addConditionalDelegate(predicate = { false }, delegate = delegate)

        sut.triggerEvent(TestEvent)

        verify(delegate, never()).invoke(eq(TestEvent))
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

        sut.triggerEvent(TestEvent, sender = "Test Event")

        verify(delegate).invoke(eventCaptor.capture())

        assertEquals("Test Event", eventCaptor.firstValue.sender)
    }

    @Test
    fun `remove all delegates method removes delegates`() {
        val delegate: (TestEvent) -> Unit = {}
        sut += delegate

        sut.removeAllDelegates()

        assert(sut.delegates.isEmpty())
    }
}