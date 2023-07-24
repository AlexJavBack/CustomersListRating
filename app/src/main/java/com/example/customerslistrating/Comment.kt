package com.example.customerslistrating

class Comment {
    var employerName : String
    var authorName : String
    var text: String
    var stars : Float = 0f

    constructor(employerName: String, authorName: String, text: String, stars: Float) {
        this.employerName = employerName
        this.authorName = authorName
        this.text = text
        this.stars = stars
    }
}