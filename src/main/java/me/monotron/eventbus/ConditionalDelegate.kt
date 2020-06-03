package me.monotron.eventbus

data class ConditionalDelegate<T>(
    val predicate: (T) -> Boolean,
    val delegate: (T) -> Unit
)