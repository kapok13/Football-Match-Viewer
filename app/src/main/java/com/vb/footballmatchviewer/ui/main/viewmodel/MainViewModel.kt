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
                }
            }
        }
        sendIntent(MainMvi.Intent.Load)
        startAutoRefresh()
    }

    private fun loadFixtures() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            launch { refreshFromNetwork() }

            try {
                footballDatabaseRepository.getFixtures().collect { cachedMatches ->
                    _state.update {
                        it.copy(
                            fixtures = cachedMatches.map { it.toUiFixtures() },
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
