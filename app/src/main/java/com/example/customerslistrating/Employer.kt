package com.example.customerslistrating

class Employer {
    var id : String
    var name : String
    var site : String
    var rating : Float = 0F
    var comment : String

    constructor(id: String, name: String, site: String, rating: Float, comment: String) {
        this.id = id
        this.name = name
        this.site = site
        this.rating = rating
        this.comment = comment
    }
}