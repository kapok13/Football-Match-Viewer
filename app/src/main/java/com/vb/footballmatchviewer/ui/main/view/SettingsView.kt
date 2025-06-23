package com.vb.footballmatchviewer.ui.main.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vb.footballmatchviewer.R
import com.vb.footballmatchviewer.data.local.model.SettingsEntity
import com.vb.footballmatchviewer.ui.main.mvi.SettingsMvi
import com.vb.footballmatchviewer.ui.main.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

const val SETTINGS_VIEW_ROUTE = "SETTINGS_VIEW"

@Composable
fun SettingsView(viewmodel: SettingsViewModel = koinViewModel(), innerPaddings: PaddingValues) {
    val uiState by viewmodel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = innerPaddings.calculateTopPadding(), horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.dark_theme), style = MaterialTheme.typography.titleSmall)
            Switch(
                checked = uiState.isDarkTheme,
                onCheckedChange = { isChecked ->
                    viewmodel.sendIntent(
                        SettingsMvi.Intent.Save(
                            SettingsEntity(
                                id = 0,
                                isDarkTheme = isChecked
                            )
                        )
                    )
                }
            )
        }
    }
}

