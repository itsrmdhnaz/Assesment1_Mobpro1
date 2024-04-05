package org.d3if3026.assesment1.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3026.assesment1.R
import org.d3if3026.assesment1.component.LoginTextField
import org.d3if3026.assesment1.model.UserModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(navController: NavHostController, userModel: UserModel) {

    var username by rememberSaveable {
        mutableStateOf("")
    }
    var usernameError by rememberSaveable {
        mutableStateOf(false)
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordError by rememberSaveable {
        mutableStateOf(false)
    }
    var peekPass by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F3F3))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier
                .padding(bottom = 10.dp)
                .size(130.dp)
        )
        Text(
            text = stringResource(R.string.title_app),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 25.dp)
        )
        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        LoginTextField(
            value = username,
            onValueChange = {
                            username = it
            },
            modifier = Modifier,
            placeholder = {
                          Text(text = stringResource(R.string.username_login))
            },
            supportingText = {
                ErrorHint(isError = passwordError)

            },
            leadingIcon = {
                          Icon(imageVector = Icons.Default.Person, contentDescription = "")
            },
            trailingIcon = {
                IconPicker(isError = usernameError, unit = "")
            },
            isError = usernameError,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
        )
        LoginTextField(
            value = password,
            onValueChange = {
                password = it
            },
            modifier = Modifier,
            placeholder = {
                Text(text = stringResource(R.string.password_login))
            },
            supportingText = {
                ErrorHint(isError = passwordError)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Password, contentDescription = "")
            },
            trailingIcon = {
                IconPicker(isError = passwordError, unit = {
                    IconButton(onClick = {
                        peekPass = !peekPass
                    }) {
                        if(peekPass){
                            Icon(imageVector = Icons.Default.ArrowDropDownCircle, contentDescription = "")
                        } else   Icon(imageVector = Icons.Default.RemoveRedEye, contentDescription = "")
                    }
                })
            },
            isError = passwordError,
            visualTransformation = if(peekPass) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Done
            ),
        )
        Button(
            onClick = {
                      usernameError = (username == "" || username == "0")
                      passwordError = (password == "" || password == "0")
                if(usernameError || passwordError) return@Button
                val success = userModel.login(username, password)
                if(success != null){
                    navController.navigate(route = "home"){
                        popUpTo("login"){
                            inclusive = true
                        }
                    }
                    Toast.makeText(context,
                        context.getString(R.string.berhasil_login_halo, success.username), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,
                        context.getString(R.string.username_atau_password_salah), Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.width(130.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(id = R.string.login),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.login),
                modifier = Modifier.weight(3f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController(), UserModel())
}