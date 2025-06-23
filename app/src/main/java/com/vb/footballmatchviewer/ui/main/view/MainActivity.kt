package com.vb.footballmatchviewer.ui.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.vb.footballmatchviewer.R
import com.vb.footballmatchviewer.ui.main.viewmodel.SettingsViewModel
import com.vb.footballmatchviewer.ui.theme.FootballMatchViewerTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinAndroidContext {
                MainScreen()
            }
        }
    }

    fun createMainGraph(navController: NavController, innerPadding: PaddingValues) =
        navController.createGraph(startDestination = MAIN_VIEW_ROUTE) {
            composable(MAIN_VIEW_ROUTE) {
                MainView(innerPaddings = innerPadding)
            }
            composable(SETTINGS_VIEW_ROUTE) {
                SettingsView(innerPaddings = innerPadding)
            }
            composable(POLICY_VIEW_ROUTE) {
                PolicyView(innerPaddings = innerPadding)
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute = navBackStackEntry?.destination?.route

        val toolbarTitle = when (currentRoute) {
            SETTINGS_VIEW_ROUTE -> stringResource(R.string.settings)
            POLICY_VIEW_ROUTE -> stringResource(R.string.policy)
            else -> stringResource(R.string.matches_today)
        }

        val settingsViewModel: SettingsViewModel = koinViewModel()
        val settingsState by settingsViewModel.state.collectAsState()

        FootballMatchViewerTheme(darkTheme = settingsState.isDarkTheme) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            if (currentRoute != MAIN_VIEW_ROUTE) {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.outline_arrow_back_24),
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                            }
                        },
                        title = {
                            Text(
                                toolbarTitle,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(POLICY_VIEW_ROUTE)
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.outline_contact_support_24),
                                    contentDescription = stringResource(R.string.policy)
                                )
                            }
                            IconButton(onClick = {
                                navController.navigate(
                                    SETTINGS_VIEW_ROUTE
                                )
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.outline_display_settings_24),
                                    contentDescription = stringResource(R.string.settings)
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    graph = createMainGraph(navController, innerPadding),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}