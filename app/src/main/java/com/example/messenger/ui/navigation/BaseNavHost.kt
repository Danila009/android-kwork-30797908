@file:OptIn(ExperimentalAnimationApi::class)

package com.example.messenger.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.messenger.ui.screens.authPhoneScreen.AuthPhoneRoute
import com.example.messenger.ui.screens.authUpdateUserInfoScreen.AuthUpdateUserInfoRoute
import com.example.messenger.ui.screens.chatScreen.ChatRout
import com.example.messenger.ui.screens.checkCodePhoneScreen.CheckCodePhoneRoute
import com.example.messenger.ui.screens.contactsListScreen.ContactsListRoute
import com.example.messenger.ui.screens.mainScreen.MainRoute
import com.example.messenger.ui.screens.phoneNumberChangeScreen.PhoneNumberChangeRoute
import com.example.messenger.ui.screens.profileScreen.ProfileRoute
import com.example.messenger.ui.screens.settingsScreen.SettingsRoute
import com.example.messenger.ui.screens.splashScreen.SplashScreen
import com.example.messenger.ui.screens.usernameChangeScreen.UsernameChangeRoute
import com.example.messenger.ui.screens.usersScreen.UsersRoute
import com.example.messenger.ui.screens.zoomableImageScreen.ZoomableImageScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@Composable
fun BaseNavHost(
    navHostController: NavHostController,
    startDestination:Screen
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = startDestination.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = {300},
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = {-300},
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 300 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -300 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
        builder = {

            composable(Screen.SplashScreen.route){
                SplashScreen(
                    navController = navHostController
                )
            }

            composable(
                route = Screen.AuthPhoneScreen.route
            ){
                AuthPhoneRoute(
                    navController = navHostController
                )
            }

            composable(
                route = Screen.CheckCodePhoneScreen.route,
                arguments = listOf(
                    navArgument("phone"){
                        type = NavType.StringType
                    },
                    navArgument("code"){
                        type = NavType.StringType
                    }
                )
            ){
                CheckCodePhoneRoute(
                    navController = navHostController,
                    phone = it.arguments!!.getString("phone",""),
                    code = it.arguments!!.getString("code","")
                )
            }
            composable(Screen.MainScreen.route){
                MainRoute(
                    navController = navHostController
                )
            }
            composable(Screen.ProfileScreen.route){
                ProfileRoute(
                    navController = navHostController
                )
            }
            composable(
                Screen.ChatScreen.route,
                arguments = listOf(
                    navArgument("receivingUserId"){
                        type = NavType.StringType
                    }
                )
            ){
                ChatRout(
                    navController = navHostController,
                    receivingUserId = it.arguments!!.getString("receivingUserId","")
                )
            }
            composable(Screen.ContactsListScreen.route){
                ContactsListRoute(
                    navController = navHostController
                )
            }
            composable(Screen.AuthUpdateUserInfoScreen.route){
                AuthUpdateUserInfoRoute(
                    navController = navHostController
                )
            }
            composable(Screen.UsersScreen.route){
                UsersRoute(
                    navController = navHostController
                )
            }
            composable(Screen.UsernameChangeScreen.route){
                UsernameChangeRoute(
                    navController = navHostController
                )
            }
            composable(Screen.PhoneNumberChangeScreen.route){
                PhoneNumberChangeRoute(
                    navController = navHostController
                )
            }
            composable(
                route = Screen.ZoomableImageScreen.route,
                arguments = listOf(
                    navArgument("url"){
                        type = NavType.StringType
                    }
                )
            ){
                ZoomableImageScreen(
                    navController = navHostController,
                    url = it.arguments!!.getString("url","")
                )
            }
            composable(Screen.SettingsScreen.route){
                SettingsRoute(
                    navController = navHostController
                )
            }
        }
    )
}