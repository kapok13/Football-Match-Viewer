package com.vb.footballmatchviewer.ui.main.mvi

import com.vb.footballmatchviewer.ui.model.Fixture

sealed class MainMvi {
    data class State(
        val loading: Boolean = true,
        val fixtures: List<Fixture>? = null,
        val sortedAscending: Boolean = true
    )

    sealed class Intent {
        object Load : Intent()
        object Refresh : Intent()
        data class SortFixturesByTimestamp(val isAscending: Boolean) : Intent()
    }
}
