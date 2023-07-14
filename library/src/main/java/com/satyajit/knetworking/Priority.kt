package com.satyajit.knetworking

sealed class Priority {
    /**
     * Lowest priority level. Used for prefetches of data.
     */
    object LOW : Priority()

    /**
     * Medium priority level. Used for warming of data that might soon get visible.
     */
    object MEDIUM : Priority()

    /**
     * Highest priority level. Used for data that are currently visible on screen.
     */
    object HIGH : Priority()

    /**
     * Highest priority level. Used for data that are required instantly(mainly for emergency).
     */
    object IMMEDIATE : Priority()
}
