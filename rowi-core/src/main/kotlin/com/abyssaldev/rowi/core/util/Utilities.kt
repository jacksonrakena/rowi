package com.abyssaldev.rowi.core.util

inline fun <reified T> List<Annotation>.getAnnotation(): T? {
    return this.filterIsInstance<T>().firstOrNull()
}

inline fun <reified T> List<Annotation>.getAnnotations(): List<T> {
    return this.filterIsInstance<T>().toList()
}

inline fun <T> tryAndIgnoreExceptions(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {

    }