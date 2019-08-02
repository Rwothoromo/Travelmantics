package com.example.travelmantics.models

import java.io.Serializable

data class TravelDeal(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var price: String? = null,
    var imageUrl: String? = null
) : Serializable