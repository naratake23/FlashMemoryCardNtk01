package com.example.flashmemorycardntk.ui.scaffold_and_navigation

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.flashmemorycardntk.model.BaseBottomAppBar
import com.example.flashmemorycardntk.model.BaseNavigationBarItem
import com.example.flashmemorycardntk.model.BaseSnackbarLayout
import com.example.flashmemorycardntk.model.EnumSnackbarActionScreens
import com.example.flashmemorycardntk.model.SCBottomAppBar
import com.example.flashmemorycardntk.model.SnackbarManager
import com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group1.ViewModelGroup1
import com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group2.ViewModelGroup2

@Composable
fun ScreenBottomAppBar() {
    Log.i("xxt", "ScreenBottomAppBar")

    val babNavController = rememberNavController()

    val vmG1: ViewModelGroup1 = hiltViewModel()
    val vmG2: ViewModelGroup2 = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }

    val screens = listOf(
        SCBottomAppBar.Play,
        SCBottomAppBar.Shuffle,
        SCBottomAppBar.Group,
    )

    val lastMessage by SnackbarManager.messages.collectAsState(initial = null)

    LaunchedEffect(key1 = lastMessage) {
        lastMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = "xxt",
                duration = message.duration,
            )
        }
        SnackbarManager.clearMessage()
    }

Scaffold(
    bottomBar = {
        BaseBottomAppBar(
            babNavController = babNavController,
            screens = screens,
            screenFab = SCBottomAppBar.Add,
            onClickFab = {
                babNavController.navigate(route = it) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            customItem = { isSelected, item ->
                BaseNavigationBarItem(
                    selected = isSelected,
                    onClickItens = {
                        babNavController.navigate(item.route) {
                            popUpTo(babNavController.graph.findStartDestination().id)
                            launchSingleTop = true // Lança como a única instância no topo
                            // Isso garante que, se já estivermos navegando para a tela atual, não criaremos uma nova instância.
                            restoreState =
                                true // Restaura o estado da tela quando navegamos de volta para ela.
                        }
                    },
                    icon = item.icon,
                    label = item.title
                )
            }
        )
    },
    snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState
        ) {
            BaseSnackbarLayout(
                lastMessage = lastMessage,
                actionLabel = {
                    when (lastMessage?.screenSource) {
                        EnumSnackbarActionScreens.GROUP1 -> {
                            vmG1.onUndoSnackbarActionG1()
                            SnackbarManager.clearMessage()
                        }
                        EnumSnackbarActionScreens.GROUP2 -> {
                            vmG2.onUndoSnackbarActionG2()
                            SnackbarManager.clearMessage()
                        }
                        else -> {}
                    }
                }
            )
        }
    }
) {paddingValues ->
    BaseBottomAppBarGraph(
        babNavController = babNavController,
        paddingValues = paddingValues,
    )

}
}