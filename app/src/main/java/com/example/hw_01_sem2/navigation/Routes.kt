package com.example.hw_01_sem2.navigation

object Routes {
    const val SEARCH = "search"
    const val DETAIL = "detail/{organizationJson}"

    fun detailRoute(organizationJson: String): String {
        return "detail/${java.net.URLEncoder.encode(organizationJson, "UTF-8")}"
    }
}