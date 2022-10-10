package com.example.messenger.ui.navigation

sealed class Screen(val route:String) {
    object SplashScreen:Screen("splash_screen")
    object AuthPhoneScreen:Screen("auth_phone_screen")
    object CheckCodePhoneScreen:Screen("check_code_phone_screen?phone={phone}&code={code}"){
        fun arguments(
            phone:String,
            code:String
        ):String = "check_code_phone_screen?phone=$phone&code=$code"
    }
    object MainScreen:Screen("main_screen")
    object UsernameChangeScreen:Screen("username_change_screen")
    object SettingsScreen:Screen("settings_screen")
    object ZoomableImageScreen:Screen("zoomable_image_screen?url={url}"){
        fun arguments(
            url:String
        ):String = "zoomable_image_screen?url=$url"
    }
    object PhoneNumberChangeScreen:Screen("phone_number_change_screen")
    object ProfileScreen:Screen("profile_screen")
    object UsersScreen:Screen("users_screen")
    object ContactsListScreen:Screen("contacts_list_screen")
    object AuthUpdateUserInfoScreen:Screen("authUpdate_user_info_screen")
    object ChatScreen:Screen("chat_screen?receivingUserId={receivingUserId}"){
        fun arguments(
            receivingUserId:String
        ):String = "chat_screen?receivingUserId=$receivingUserId"
    }
}