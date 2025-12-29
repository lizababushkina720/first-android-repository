package com.example.hw_05.data.prefs


class SessionStore(
    private val prefsProvider: PrefsProvider = PrefsProvider
) {
    private val sp get() = prefsProvider.get()

    fun setCurrentUserId(userId: Long) {
        sp.edit()
            .putLong(KEY_CURRENT_USER_ID, userId)
            .apply()
    }

    fun getCurrentUserId(): Long? {
        val id = sp.getLong(KEY_CURRENT_USER_ID, NO_ID)
        return if (id == NO_ID) null else id
    }

    fun clear() {
        sp.edit()
            .remove(KEY_CURRENT_USER_ID)
            .apply()
    }

    private companion object {
        const val KEY_CURRENT_USER_ID = "current_user_id"
        const val NO_ID = -1L
    }
}
