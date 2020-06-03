# Kotlin Event Bus

This repository contains a simple implementation of an event bus. It also allows conditional events, which only fire when a given predicate evaluates to true.

## Usage

The `EventBus` class can produce a stream of events of a given type (which must be `Event` or a subclass of `Event`):

```kt
val myEventBus: EventBus<TestEvent> = EventBus()
```

To add a delegate to an event (a method which is called when the event is triggered), you can use the plus assign operator (the triggered event is sent to all delegates):

```kt
myEventBus += { event ->
    // Place your event logic here.
}
```

Then, to trigger the event from elsewhere, use the `triggerEvent` method:

```kt
myEventBus.triggerEvent(event) // where event is the generic type specified earlier
```

The event value is automatically tagged with the time it was triggered (`LocalDateTime.now()`), and can also contain an instance of the sender object if you want (specified by the `sender` argument on `triggerEvent`):

```kt
myEventBus.triggerEvent(event, sender = this)
```

If the sender is specified, the event object passed to your delegates will contain an instance of the sender.

The `ConditionalEventBus` class can be used to contain delegates that execute conditionally:

```kt
val myConditionalEventBus: ConditionalEventBus<TestEvent> = ConditionalEventBus()
```

This event is triggered in the same way as a regular event bus, but you can use the `addConditionalDelegate()` method to add a conditionally-executed delegate:

```kt
myConditionalEventBus.addConditionalDelegate(predicate = { /* event logic goes here */ }, delegate = {})
```

(You can also use the plus assign syntax from before to add a delegate which always executes.)

## Testing

`mvn test` will run all unit tests contained within the library.