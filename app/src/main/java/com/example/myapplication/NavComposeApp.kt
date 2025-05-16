package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.nav.Action
import com.example.myapplication.nav.Destination
import com.example.myapplication.nav.Destination.AuthenticationOption
import com.example.myapplication.nav.Destination.ChatRoom
import com.example.myapplication.nav.Destination.ChatRoomList
import com.example.myapplication.nav.Destination.Home
import com.example.myapplication.nav.Destination.Login
import com.example.myapplication.nav.Destination.Register
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.view.AuthenticationView
import com.example.myapplication.view.chatroom.ChatRoomListView
import com.example.myapplication.view.home.HomeView
import com.example.myapplication.view.login.LoginView
import com.example.myapplication.view.register.RegisterView
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun NavComposeApp() {
    val navController = rememberNavController()
    val actions = remember(navController) { Action(navController) }

    MyApplicationTheme {
        NavHost(
            navController = navController,
            startDestination =
            if (FirebaseAuth.getInstance().currentUser != null)
                Destination.ChatRoomList
            else
                AuthenticationOption
        ) {
            composable(AuthenticationOption) {
                AuthenticationView(
                    register = actions.register,
                    login = actions.login
                )
            }
            composable(Register) {
                RegisterView(
                    home = actions.home,
                    back = actions.navigateBack
                )
            }
            composable(Login) {
                LoginView(
                    home = actions.home,
                    back = actions.navigateBack
                )
            }
            composable(Destination.ChatRoomList) {
                ChatRoomListView(
                    onChatRoomSelected = { roomId -> actions.chatRoom(roomId) }
                )
            }
            composable(
                Destination.ChatRoom,
                arguments = listOf(navArgument("roomId") { type = NavType.StringType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                HomeView(
                    roomId = roomId,
                    onBackClick = actions.chatRoomList // Pass navigation to chat room list
                )
            }
        }
    }
}
