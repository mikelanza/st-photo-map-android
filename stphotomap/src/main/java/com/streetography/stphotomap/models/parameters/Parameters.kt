package com.streetography.stphotomap.models.parameters

public typealias KeyValue = Pair<String, String>

public class Parameters {
    public class Keys {
        companion object {
            public val basemap = "basemap"
            public val shadow = "shadow"
            public val sort = "sort"
            public val tileSize = "tileSize"
            public val pinOptimize = "pinoptimize"
            public val sessionId = "sessionID"
            public val bbox = "bbox"
            public val userId = "userId"
            public val collectionId = "collectionId"
            public val client = "client"
        }
    }
}