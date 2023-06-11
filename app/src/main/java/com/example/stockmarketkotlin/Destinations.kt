package com.example.stockmarketkotlin

interface Destinations {
    val route: String
    val icon: Int
    val title: String
}

object Home: Destinations {
    override val route = "Home"
    override val icon = R.drawable.ic_home
    override val title = "Home"
}