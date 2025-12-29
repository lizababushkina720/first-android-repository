package com.example.hw_05.data.prefs


import com.example.hw_05.Keys

class SortStore(
    private val prefsProvider: PrefsProvider = PrefsProvider
) {
    private val sp get() = prefsProvider.get()

    fun setSort(value: String) {
        sp.edit()
            .putString(KEY_SORT, value)
            .apply()
    }

    fun getSort(): String {
        return sp.getString(KEY_SORT, Keys.SORT_NEWEST) ?: Keys.SORT_NEWEST
    }

    private companion object {
        const val KEY_SORT = "pets_sort"
    }
}
