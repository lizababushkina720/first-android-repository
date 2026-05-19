package com.example.hw_01_sem2.navigation

object Routes {
    const val SEARCH = "search"

    fun detailRoute(inn: String): String {
        return "detail/$inn"
    }
    const val ARG_ORGANIZATION_INN = "organizationInn"

    const val DETAIL = "detail/{$ARG_ORGANIZATION_INN}"

}