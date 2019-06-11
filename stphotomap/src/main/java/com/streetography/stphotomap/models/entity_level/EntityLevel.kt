package com.streetography.stphotomap.models.entity_level

enum class EntityLevel(val value: String) {
    location("location"),
    block("block"),
    neighborhood("neighborhood"),
    city("city"),
    county("county"),
    state("state"),
    country("country"),
    unknown("");

    companion object {
        fun fromString(i: String): EntityLevel {
            for (b in EntityLevel.values()) {
                if (b.value == i) {
                    return b
                }
            }
            return unknown
        }
    }
}