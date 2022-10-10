package com.example.messenger.ui.screens.checkCodePhoneScreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.data.firebase.user.model.User
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.theme.primaryBackground
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.view.Animation
import com.example.messenger.ui.view.LottieAnimation
import com.example.messenger.ui.view.OTPTextFields
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CheckCodePhoneRoute(
    navController: NavController,
    phoneNumberChange:Boolean = false,
    phone: String,
    code: String,
    viewModel: CheckCodePhoneViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val systemUiController = rememberSystemUiController()

    val firebaseAuth = remember { FirebaseAuth.getInstance() }

    var codePhoneValue by remember { mutableStateOf("") }
    var nextAuthUpdateUserInfoScreen by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, block = {

        systemUiController.setSystemBarsColor(color = primaryBackground)

        viewModel.getUserByPhone(
            phoneNumber = phone,
            onSuccessListener = { id ->
                nextAuthUpdateUserInfoScreen = id == null
            }
        )
    })

    CheckCodePhoneScreen(
        phone = phone,
        codePhoneValue = codePhoneValue,
        onPhoneCodeValueChanged = { getOpt ->
            codePhoneValue = getOpt
            if (getOpt.length < 6) return@CheckCodePhoneScreen
            if (phoneNumberChange){

            } else {
                viewModel.verificationPhoneCode(
                    verificationCode = code,
                    code = getOpt,
                    onCompleteListener = { isSuccessful ->
                        if (isSuccessful){
                            viewModel.addUser(
                                user = User(
                                    id = firebaseAuth.currentUser?.uid ?: return@verificationPhoneCode,
                                    phone = phone
                                ),
                                onCompleteListener = { task ->
                                    if (task.isSuccessful){
                                        viewModel.addPhone(
                                            phone = phone,
                                            onSuccessListener = {
                                                navController.navigate(
                                                    if (nextAuthUpdateUserInfoScreen)
                                                        Screen.AuthUpdateUserInfoScreen.route
                                                    else
                                                        Screen.MainScreen.route
                                                ){
                                                    popUpTo(Screen.CheckCodePhoneScreen.route){
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            )
                        }else {
                            Toast.makeText(
                                context,
                                "Неверный код",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    )
}

@Composable
private fun CheckCodePhoneScreen(
    phone:String,
    codePhoneValue:String,
    onPhoneCodeValueChanged:(String) -> Unit
) {

    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = primaryBackground
    ) {
        LazyColumn {
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            top = 10.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(120.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .width(
                            width = screenWidthDp,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    LottieAnimation(
                        animation = Animation.CheckCodePhone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .padding(5.dp)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "+$phone",
                        color = primaryText,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Код активации отправлен на Ваш телефон. \n Пожалуйста, ввидете его ниже.",
                        color = primaryText,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    OTPTextFields(
                        length = 6,
                        modifier = Modifier.padding(5.dp),
                        onValueChanged = onPhoneCodeValueChanged,
                        value = codePhoneValue
                    )
                }
            }
        }
    }
}