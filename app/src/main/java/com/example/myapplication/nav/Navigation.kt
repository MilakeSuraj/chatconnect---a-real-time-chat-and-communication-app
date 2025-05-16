package com.example.myapplication.nav

import androidx.navigation.NavHostController
import com.example.myapplication.nav.Destination.Home
import com.example.myapplication.nav.Destination.Login
import com.example.myapplication.nav.Destination.Register



object Destination {
    const val AuthenticationOption = "authenticationOption"
    const val Register = "register"
    const val Login = "login"
    const val Home = "home"
    const val ChatRoomList = "chatRoomList"
    const val ChatRoom = "chatRoom/{roomId}"
}

class Action(navController: NavHostController) {
    val home: () -> Unit = {
        navController.navigate(Home) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
            restoreState = false
        }
    }
    val login: () -> Unit = { navController.navigate(Login) }
    val register: () -> Unit = { navController.navigate(Register) }
    val navigateBack: () -> Unit = { navController.popBackStack() }
    val chatRoomList: () -> Unit = {
        navController.navigate(Destination.ChatRoomList) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
            restoreState = false
        }
    }
    val chatRoom: (String) -> Unit = { roomId ->
        navController.navigate("chatRoom/$roomId")
    }
}