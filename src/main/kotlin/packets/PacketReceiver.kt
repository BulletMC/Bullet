package com.aznos.packets

/**
 * Annotation used to mark methods as packet receivers
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PacketReceiver
