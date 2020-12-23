package com.example.gestaofinanceira.Models

import java.time.LocalDate
import java.util.*

data class Finances (
    val description: String?,
    val value: Double,
    val type: String,
    val date: Date
): Comparable<Finances>{
    constructor() : this("", 0.0, "", Date())

    override fun compareTo(other: Finances): Int {
        if (date == null || other.date == null) {
            return 0;
        }
        if(date.equals(other.date)) {
            if (type.equals(other.type)) {
                return 0
            } else if (type.equals("DESPESA") && other.type.equals("RECEITA")) {
                return -1
            } else {
                return 1
            }
        }
        return date.compareTo(other.date);
    }
}