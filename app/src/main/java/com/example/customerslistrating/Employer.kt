package com.example.customerslistrating


class Employer {
    lateinit var id : String
    lateinit var name : String
    lateinit var site : String
    var rating : Float = 0F

    constructor(id: String, name: String, site: String, rating: Float) {
        this.id = id
        this.name = name
        this.site = site
        this.rating = rating
    }

    constructor()


}