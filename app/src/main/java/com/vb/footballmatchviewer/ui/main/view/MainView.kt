package com.vb.footballmatchviewer.ui.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.vb.footballmatchviewer.R
import com.vb.footballmatchviewer.ui.main.mvi.MainMvi
import com.vb.footballmatchviewer.ui.main.viewmodel.MainViewModel
import com.vb.footballmatchviewer.ui.model.Fixture
import com.vb.footballmatchviewer.utils.toDayTimeFormat
import org.koin.androidx.compose.koinViewModel

const val MAIN_VIEW_ROUTE = "MAIN_VIEW"

@Composable
fun MainView(viewmodel: MainViewModel = koinViewModel(), innerPaddings: PaddingValues) {
    val uiState by viewmodel.state.collectAsState()
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.loading) {
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round,
                strokeWidth = 4.dp,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(innerPaddings)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box {
                        IconButton(onClick = { sortMenuExpanded = true },  modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.outline_filter_list_24),
                                contentDescription = stringResource(R.string.sort),
                                modifier = Modifier.size(32.dp),
                            )
                        }

                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.by_ascending)) },
                                onClick = {
                                    sortMenuExpanded = false
                                    if (uiState.sortedAscending == false) {
                                        viewmodel.sendIntent(MainMvi.Intent.SortFixturesByTimestamp(true))
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.by_descending)) },
                                onClick = {
                                    sortMenuExpanded = false
                                    if (uiState.sortedAscending) {
                                        viewmodel.sendIntent(MainMvi.Intent.SortFixturesByTimestamp(false))
                                    }
                                }
                            )
                        }
                    }
                }
                SwipeRefresh(
                    state = SwipeRefreshState(uiState.loading),
                    onRefresh = { viewmodel.sendIntent(MainMvi.Intent.Refresh) },
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(uiState.fixtures.orEmpty()) { fixture ->
                            FixtureItem(fixture)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FixtureItem(fixture: Fixture) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TeamColumn(name = fixture.team1_name, logo = fixture.team1_logo, Modifier.weight(1f))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (fixture.home != null && fixture.away != null) {
                Text(text = "${fixture.home} : ${fixture.away}")
            } else {
                Text(text = fixture.timestamp.toDayTimeFormat())
            }
        }

        TeamColumn(name = fixture.team2_name, logo = fixture.team2_logo, Modifier.weight(1f))
    }
}

@Composable
fun TeamColumn(name: String, logo: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = logo),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
