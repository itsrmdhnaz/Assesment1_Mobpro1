package org.d3if3026.assesment1.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3026.assesment1.R
import org.d3if3026.assesment1.model.Transaction
import org.d3if3026.assesment1.model.User
import org.d3if3026.assesment1.model.UserModel
import org.d3if3026.assesment1.navigation.Screen
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale
import java.util.UUID
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, userModel: UserModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFECEBEB),
                    titleContentColor = Color(0xFF444445)
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(id = R.string.app_name),
                            tint = Color(0xFF444445),
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = stringResource(id = R.string.app_name),
                            tint = Color(0xFF444445),
                        )
                    }
                },
            )
        },
        containerColor = Color(0xFFF4F3F3)
    ) { padding ->
        ScreenContent(Modifier.padding(padding), userModel)

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenContent(modifier: Modifier = Modifier, userModel: UserModel) {
    val date = LocalDate.now()
    val formattedDate =
        date.format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale(Locale.getDefault().language, Locale.getDefault().country)))

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var amountLoan by rememberSaveable { mutableStateOf("") }





    // head up
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = spacedBy(20.dp),
    ) {
        Row {
            Column(
                modifier = Modifier,
            ) {
                Text(
                    text = "Current balance",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "3840€",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.End),
                    textAlign = TextAlign.End,
                )
            }
        }
        // body up
        if (userModel.auth.value != null) {
            ListTransaction(userModel.auth.value!!.user, {})
        }
        Text(
            text = "Make Transaction",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Color.White,
                    RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    showBottomSheet = true
                },
                modifier = Modifier
                    .weight(1f)
                    .size(80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFBC06),
                )
            ) {
                Column(
                    verticalArrangement = spacedBy(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "",
                    )
                    Text(text = "Transfer Money")
                }
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .size(80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                )
            ) {
                Column(
                    verticalArrangement = spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Money,
                        contentDescription = "",
                    )
                    Text(text = "Request loan")
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    Color(0xFF4CAF50),
                    RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = "Request Loan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                   value = amountLoan,
                    onValueChange = {
                       amountLoan = formatCurrency(parseCurrency(it), Locale(Locale.getDefault().language, Locale.getDefault().country))
                    },
                    modifier = Modifier
                        .weight(1f),
                  placeholder = { Text(text = "amount") },
                    leadingIcon = {
                        Text(text = Currency.getInstance(userModel.auth.value!!.user.currency).getSymbol(Locale(Locale.getDefault().language, Locale.getDefault().country)))
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onPrevious = {
                        // Move cursor to the end when the next action is triggered
                        amountLoan.length
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.5f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(paddingValues = PaddingValues(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "",
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            // Sheet content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TransferMoney()
            }
        }
    }
}

@Composable
fun RequestLoan(){

}

@Composable
fun TransferMoney(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                Color(0xFFFFBC06),
                RoundedCornerShape(16.dp),
            )
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ){
        Text(
            text = "Transfer Money",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            horizontalArrangement = spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.5f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.5f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(paddingValues = PaddingValues(8.dp))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "",
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListTransaction(user: User, onClick: () -> Unit, modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .size(300.dp)
            .background(
                Color.White,
                RoundedCornerShape(16.dp),
            )
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(
                user.transaction.size
            ) {
                ItemTransaction(
                    index = user.transaction.size - it,
                    status = if (user.transaction[user.transaction.size - it - 1].amount < 0) "WITHDRAWAL" else "DEPOSIT",
                    amount = user.transaction[user.transaction.size - it - 1].amount,
                    currency = user.currency
                )
                Divider()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
@Composable
fun ListTransactionPreview(){
    ListTransaction(User(
        id = UUID.randomUUID(),
        username = "User",
        password = "123",
        transaction = listOf(
            Transaction(
                id = UUID.randomUUID(),
                amount = 1000f,
                date = LocalDate.now(),
                transactionDescription = "Deposit",
            ), Transaction(
                id = UUID.randomUUID(),
                amount = 1000f,
                date = LocalDate.now(),
                transactionDescription = "Deposit",
            ), Transaction(
                id = UUID.randomUUID(),
                amount = -1000f,
                date = LocalDate.now(),
                transactionDescription = "Deposit",
            ), Transaction(
                id = UUID.randomUUID(),
                amount = 1000f,
                date = LocalDate.now(),
                transactionDescription = "Deposit",
            ),),
        currency = "IDR"), onClick = {}, modifier = Modifier.fillMaxWidth())
}


@Composable
fun ItemTransaction(index: Int, status: String, amount: Float, currency: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(16.dp)
            .background(
                Color.White,
                CircleShape,
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "$index $status",
                modifier = Modifier
                    .background(
                        if (amount < 0) Color(0xFFEC375C) else Color(0xFF4CAF50),
                        CircleShape,
                    )
                    .height(25.dp)
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = formatCurrency(amount, currency, Locale(Locale.getDefault().language, Locale.getDefault().country)),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 18.sp
            )
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun ItemTransactionPreview() {
    ItemTransaction(1,"Deposit", 1000f, "IDR")
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController(), userModel = UserModel())
}

fun formatCurrency(amount: Float, currency: String, locale: Locale): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    formatter.currency = Currency.getInstance(currency)
    return if(amount < 0){
        "– ${formatter.format(amount.absoluteValue)}"
    } else {
        formatter.format(amount)
    }
}

fun parseCurrency(currencyString: String, currency: String, locale: Locale): Float? {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    formatter.currency = Currency.getInstance(currency)
    return try {
        val parsedNumber = formatter.parse(currencyString) ?: return null
        parsedNumber.toFloat()
    } catch (e: ParseException) {
        null
    }
}

fun formatCurrency(amount: Float, locale: Locale): String {
    if (amount == 0f) {
        return "0"
    }
    val formatter: DecimalFormat = NumberFormat.getNumberInstance(locale) as DecimalFormat
    formatter.applyPattern("#,##0")
    return formatter.format(amount.toInt())
}

fun parseCurrency(currencyString: String): Float {
    if(currencyString.isEmpty()) {
        return 0f
    }
     if (currencyString.startsWith('0') && currencyString.length > 1) {
        return currencyString.substring(1).toFloat()
    }
    val cleanedString = currencyString.replace(".", "").replace(",", "")
    return cleanedString.toFloatOrNull() ?: 0f
}

@Composable
fun IconPicker(isError: Boolean, unit: String){
    if(isError){
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean){
    if(isError){
        Text(text = "error")
    }
}

fun main() {
    val amount = 1234.56f
    val locale = Locale.US
    val formattedAmount = formatCurrency(amount, locale)
    println(formattedAmount) // Output: 1,234.56
}

