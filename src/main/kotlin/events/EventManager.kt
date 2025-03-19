package com.aznos.events

import com.aznos.Bullet
import java.util.concurrent.ConcurrentHashMap

/**
 * The EventManager class is responsible for managing the listeners and firing events
 *
 * @property listeners A map of event types to their listeners
 */
object EventManager {
    private val listeners = ConcurrentHashMap<Class<out Event>, MutableList<EventListener<out Event>>>()

    /**
     * Registers a listener for a specific event type
     *
     * @param eventType The type of event to listen for
     * @param listener The listener to register
     */
    @Suppress("unused")
    fun <T : Event> register(eventType: Class<T>, listener: EventListener<T>) {
        listeners.computeIfAbsent(eventType) {
            mutableListOf()
        }.add(listener)
    }

    /**
     * Fires an event
     *
     * @param event The event to fire
     */
    fun <T : Event> fire(event: T) {
        listeners[event::class.java]?.forEach { listener ->
            @Suppress("UNCHECKED_CAST")
            (listener as EventListener<T>).onEvent(event)
        }
    }
}