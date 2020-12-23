package com.example.gestaofinanceira.Models

data class User(
    var email: String,
    var password: String,
    var name: String,
    var phone: String,
    var financeActivity: MutableList<Finances>
){ //: Serializable
    constructor() : this("", "", "", "", emptyArray<Finances>().toMutableList())
}