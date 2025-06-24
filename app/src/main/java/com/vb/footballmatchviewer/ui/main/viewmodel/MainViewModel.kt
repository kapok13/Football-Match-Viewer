package com.vb.footballmatchviewer.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.footballmatchviewer.data.local.repository.FootballDatabaseRepository
import com.vb.footballmatchviewer.data.remote.repository.FootballNetworkRepository
import com.vb.footballmatchviewer.data.mapper.toFootballEntity
import com.vb.footballmatchviewer.data.mapper.toUiFixtures
import com.vb.footballmatchviewer.ui.main.mvi.MainMvi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val footballDatabaseRepository: FootballDatabaseRepository,
    private val footballNetworkRepository: FootballNetworkRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MainMvi.State())
    val state = _state.asStateFlow()
    private val intent = MutableSharedFlow<MainMvi.Intent>()

    init {
        viewModelScope.launch {
            intent.collect {
                when (it) {
                    is MainMvi.Intent.Load -> loadFixtures()
                    is MainMvi.Intent.Refresh -> refreshFromNetwork()
                    is MainMvi.Intent.SortFixturesByTimestamp -> sortFixturesOrder(it.isAscending)
                }
            }
        }
        sendIntent(MainMvi.Intent.Load)
        startAutoRefresh()
    }

    private fun sortFixturesOrder(isAscending: Boolean) {
        _state.update {
            it.copy(
                sortedAscending = isAscending
            )
        }
        loadFixtures(true)
    }

    private fun loadFixtures(sorting: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            if (sorting.not()) {
                launch { refreshFromNetwork() }
            }

            try {
                footballDatabaseRepository.getFixtures().collect { cachedMatches ->
                    val sortedMatches = cachedMatches
                        .map { it.toUiFixtures() }
                        .let { matches ->
                            if (state.value.sortedAscending == false) {
                                matches.sortedByDescending { it.timestamp }
                            } else {
                                matches.sortedBy { it.timestamp }
                            }
                        }
                    _state.update {
                        it.copy(
                            fixtures = sortedMatches,
                            loading = false
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun refreshFromNetwork() {
        try {
            val freshFixtures = footballNetworkRepository.getFixtures()
            val entities = freshFixtures.map { response ->
                response.toFootballEntity()
            }
            footballDatabaseRepository.saveFixtures(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (isActive) {
                delay(5 * 60 * 1000L)
                refreshFromNetwork()
            }
        }
    }

    fun sendIntent(currentIntent: MainMvi.Intent) {
        viewModelScope.launch { intent.emit(currentIntent) }
    }
}
