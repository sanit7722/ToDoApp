package com.sanit.todo.Model



class Notes {

    var title: String? = null
    var subtitle: String? = null

    constructor() {

    }

    constructor(title: String, subtitle: String) {
        this.title = title
        this.subtitle = subtitle
    }
}
