package com.example.flashmemorycardntk.ui.scaffold_and_navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flashmemorycardntk.model.EnumGroupRoutes
import com.example.flashmemorycardntk.model.SCBottomAppBar
import com.example.flashmemorycardntk.ui.screen_bab_fab.screen_vm_fab1.ScreenFab1
import com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group1.ScreenGroup1
import com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group2.ScreenGroup2
import com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group2.ScreenGroup2MultipleAdds
import com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group3.ScreenGroup3
import com.example.flashmemorycardntk.ui.screen_bab_play.screen_vm_play1.ScreenPlay1
import com.example.flashmemorycardntk.ui.screen_bab_shuffle.screen_vm_shuffle1.ScreenShuffle1

@Composable
fun BaseBottomAppBarGraph(
    babNavController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = babNavController,
        startDestination = SCBottomAppBar.Play.route
    ) {
        composable(route = SCBottomAppBar.Play.route) {
            ScreenPlay1(paddingValues)
        }
        composable(route = SCBottomAppBar.Shuffle.route) {
            ScreenShuffle1(paddingValues)
        }
        composable(route = SCBottomAppBar.Group.route) {
            GroupGraph1(paddingValues)
        }
        composable(route = SCBottomAppBar.Add.route) {
            ScreenFab1(paddingValues = paddingValues)
        }
    }
}

//-----------------------------------------------------------
@Composable
fun GroupGraph1(
    paddingValues: PaddingValues,
) {
    val groupNavController = rememberNavController()

    NavHost(
        navController = groupNavController,
        startDestination = SCBottomAppBar.Group.route
    ) {

        composable(route = SCBottomAppBar.Group.route) {
            ScreenGroup1(
                paddingValues = paddingValues,
                nextScreen = { groupId ->
                    groupNavController.navigate(route = EnumGroupRoutes.GROUP_SCREEN_2.route + "/$groupId") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(
            route = EnumGroupRoutes.GROUP_SCREEN_2.route + "/{groupId}",
            arguments = listOf(navArgument("groupId", builder = { type = NavType.LongType }))
        ) { backStackEntry ->

            val groupId = backStackEntry.arguments?.getLong("groupId")
                ?: throw IllegalStateException("GroupId is missing")
            ScreenGroup2(paddingValues = paddingValues,
                nextScreenMultipleAdd = {
                    groupNavController.navigate(route = EnumGroupRoutes.GROUP_SCREEN_2_MA.route + "/$groupId") {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                nextScreenG3 = { cardId, isFrontSide ->
                    groupNavController.navigate(route = "${EnumGroupRoutes.GROUP_SCREEN_3.route}/$groupId/$cardId/$isFrontSide") {
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
        composable(
            route = EnumGroupRoutes.GROUP_SCREEN_2_MA.route
                    + "/{groupId}",
            arguments = listOf(navArgument("groupId", builder = { type = NavType.LongType }))
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getLong("groupId")
                ?: throw IllegalStateException("GroupId is missing")
            ScreenGroup2MultipleAdds(
                paddingValues = paddingValues,
                backScreen = {
                    groupNavController.popBackStack()
                }
            )
        }
        composable(
            route = EnumGroupRoutes.GROUP_SCREEN_3.route + "/{groupId}/{cardId}/{isFrontSide}",
            arguments = listOf(
                navArgument("groupId", builder = { type = NavType.LongType }),
                navArgument("cardId", builder = { type = NavType.LongType }),
                navArgument("isFrontSide", builder = { type = NavType.BoolType })
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getLong("groupId")
                ?: throw IllegalStateException("groupId is missing")
            val cardId = backStackEntry.arguments?.getLong("cardId")
                ?: throw IllegalStateException("cardId is missing")
            val isFrontSide = backStackEntry.arguments?.getBoolean("isFrontSide")
                ?: throw IllegalStateException("isFrontSide is missing")
            ScreenGroup3(
                paddingValues = paddingValues,
                backScreen = {
                    groupNavController.popBackStack()
                })
        }
    }
}