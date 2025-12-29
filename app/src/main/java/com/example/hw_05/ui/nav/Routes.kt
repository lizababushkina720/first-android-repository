package com.example.hw_05.ui.nav

object Routes {
    const val AUTH = "auth"
    const val REGISTER = "register"
    const val PETS = "pets"
    const val ADD_PET = "add_pet"
    const val PROFILE = "profile"

    const val RECOVERY_WITH_ID = "recovery/{id}"
    fun recoveryRoute(id: Long) = "recovery/$id"

    const val REGISTER_OK = "register_ok"

    const val ADD_PET_OK = "add_pet_ok"





}
