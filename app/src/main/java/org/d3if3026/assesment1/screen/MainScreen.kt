package org.d3if3026.assesment1.screen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.d3if3026.assesment1.R
import org.d3if3026.assesment1.model.Auth
import org.d3if3026.assesment1.model.Transaction
import org.d3if3026.assesment1.model.User
import org.d3if3026.assesment1.model.UserModel
import org.d3if3026.assesment1.model.convertCurrency
import org.d3if3026.assesment1.navigation.Screen
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, userModel: UserModel) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = stringResource(R.string.logo)
                    )
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
                    IconButton(onClick = {
                        navController.navigate(route = "login") {
                            popUpTo("home") { inclusive = true }
                        }
                        userModel.logout()
                        Toast.makeText(
                            context,
                            context.getString(R.string.berhasil_melakukan_logout),
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
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
        date.format(
            DateTimeFormatter.ofPattern(
                "EEEE, dd/MM/yyyy",
                Locale(Locale.getDefault().language, Locale.getDefault().country)
            )
        )

    // bottom sheets
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val currencyList = listOf("USD", "EUR", "GBP", "IDR")
    // input state
    var selectMenu by rememberSaveable { mutableStateOf("") }
    var expandedFromCurrency by rememberSaveable { mutableStateOf(false) }
    var selectedFromCurrency by rememberSaveable { mutableStateOf(userModel.getCurrencyAuth()) }

    var expandedToCurrency by rememberSaveable { mutableStateOf(false) }
    var selectedToCurrency by rememberSaveable { mutableStateOf(currencyList.filter { it != userModel.getCurrencyAuth() }[0]) }

    var value by rememberSaveable {
        mutableStateOf("")
    }
    var valueError by rememberSaveable {
        mutableStateOf(false)
    }

    var result by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    // state in userViewModel
    val auth: Auth? by userModel.auth.observeAsState()

    val context = LocalContext.current

    // head up
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = spacedBy(16.dp),
    ) {
        Row {
            Column(
                modifier = Modifier,
            ) {
                Text(
                    text = stringResource(R.string.current_balance),
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
                    text = formatCurrency(
                        userModel.getTotalAmount(),
                        userModel.getCurrencyAuth(),
                        Locale(Locale.getDefault().language, Locale.getDefault().country)
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.End),
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        // body up
        auth?.let { ListTransaction(it.user) }
        Row {
            Text(
                text = userModel.getTotalIncome()?.let { stringResource(R.string.`in`, it) } ?: "",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = userModel.getTotalOutcome()?.let { stringResource(R.string.`out`, it) }
                    ?: "",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
        Column {
            Text(
                text = stringResource(R.string.make_transaction),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
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
                        selectMenu = "Transfer"
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "",
                        )
                        Text(text = stringResource(R.string.transfer))
                    }
                }
                Button(
                    onClick = {
                        selectMenu = "Loan"
                        showBottomSheet = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .size(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Money,
                            contentDescription = "",
                        )
                        Text(text = stringResource(R.string.loan))
                    }
                }
            }
        }

        Column {
            Text(
                text = stringResource(R.string.money_conversion),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(5.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(16.dp),
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        value = it
                        valueError = (value == "" || value == "0")
                        if (valueError) return@OutlinedTextField
                        result = convertCurrency(
                            selectedFromCurrency,
                            selectedToCurrency,
                            value.toFloat()
                        )
                    },
                    modifier = Modifier
                        .weight(1f),
                    placeholder = { Text(text = stringResource(R.string.amount)) },
                    leadingIcon = {
                        Text(
                            text = Currency.getInstance(selectedFromCurrency).symbol,
                            fontSize = 15.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.5f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                    ),
                    trailingIcon = {
                        IconPicker(isError = valueError, unit = {
                            if (result != 0f) {
                                IconButton(onClick = {
                                    shareData(
                                        context,
                                        context.getString(
                                            R.string.share_result,
                                            formatCurrency(
                                                value.toFloat(),
                                                Locale(
                                                    Locale.getDefault().language,
                                                    Locale.getDefault().country
                                                )
                                            ),
                                            selectedToCurrency,
                                            result.toString()
                                        )
                                    )
                                }) {
                                    Icon(imageVector = Icons.Default.Share, contentDescription = "")
                                }
                            }
                        })
                    },
                    supportingText = {
                        ErrorHint(isError = valueError)
                    },
                    isError = valueError,
                    singleLine = true,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expandedFromCurrency,
                        onExpandedChange = { expandedFromCurrency = !expandedFromCurrency },
                        modifier = Modifier.weight(2f)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.menuAnchor(),
                            readOnly = true,
                            value = selectedFromCurrency,
                            onValueChange = {},
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFromCurrency) },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                        )
                        ExposedDropdownMenu(
                            expanded = expandedFromCurrency,
                            onDismissRequest = { expandedFromCurrency = false },
                        ) {
                            currencyList.forEach { currency ->
                                DropdownMenuItem(
                                    text = { Text(currency) },
                                    onClick = {
                                        selectedFromCurrency = currency
                                        expandedFromCurrency = false
                                        valueError = (value == "" || value == "0")
                                        if (valueError) return@DropdownMenuItem
                                        result = convertCurrency(
                                            selectedFromCurrency,
                                            selectedToCurrency,
                                            value.toFloat()
                                        )
                                    },
                                    enabled = currency != selectedFromCurrency
                                )
                            }
                        }
                    }

                    IconButton(onClick = {
                        val temp = selectedFromCurrency
                        selectedFromCurrency = selectedToCurrency
                        selectedToCurrency = temp
                        valueError = (value == "" || value == "0")
                        if (valueError) return@IconButton
                        result = convertCurrency(
                            selectedFromCurrency,
                            selectedToCurrency,
                            value.toFloat()
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.SwapHoriz,
                            contentDescription = "",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedToCurrency,
                        onExpandedChange = { expandedToCurrency = !expandedToCurrency },
                        modifier = Modifier.weight(2f)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.menuAnchor(),
                            readOnly = true,
                            value = selectedToCurrency,
                            onValueChange = {},
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedToCurrency) },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                        )
                        ExposedDropdownMenu(
                            expanded = expandedToCurrency,
                            onDismissRequest = { expandedToCurrency = false },
                        ) {
                            currencyList.forEach { currency ->
                                DropdownMenuItem(
                                    text = { Text(currency) },
                                    onClick = {
                                        selectedToCurrency = currency
                                        expandedToCurrency = false
                                        valueError = (value == "" || value == "0")
                                        if (valueError) return@DropdownMenuItem
                                        result = convertCurrency(
                                            selectedFromCurrency,
                                            selectedToCurrency,
                                            value.toFloat()
                                        )
                                    },
                                    enabled = currency != selectedToCurrency
                                )
                            }
                        }
                    }
                }
                Text(
                    text = if (valueError) {
                        stringResource(
                            R.string.convert,
                            formatCurrency(
                                0f,
                                selectedFromCurrency,
                                Locale(Locale.getDefault().country, Locale.getDefault().country)
                            )
                        )
                    } else {
                        if (selectedFromCurrency == "IDR") {
                            stringResource(
                                R.string.convert,
                                "${Currency.getInstance(selectedToCurrency).symbol} ${
                                    result.toString().substring(
                                        0,
                                        if (result.toString().length < 7) result.toString().length else 7
                                    )
                                }"
                            )
                        } else stringResource(
                            R.string.convert,
                            formatCurrency(
                                result,
                                selectedFromCurrency,
                                Locale(Locale.getDefault().country, Locale.getDefault().country)
                            )
                        )

                    },
                    style = MaterialTheme.typography.titleMedium,
                )
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
                    .height(if (selectMenu == "Transfer") 380.dp else 230.dp)
            ) {
                // Sheet content
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (selectMenu == "Transfer") {
                        TransferMoney(
                            userModel = userModel,
                            closeBottomSheet = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                            },

                            )
                    } else {
                        RequestLoan(
                            userModel = userModel,
                            closeBottomSheet = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestLoan(
    userModel: UserModel,
    closeBottomSheet: () -> Unit,
) {
    var amountLoan by rememberSaveable { mutableStateOf("") }
    var amountLoanError by rememberSaveable { mutableStateOf(false) }
    val focuseRequester by remember {
        mutableStateOf(FocusRequester())
    }

    LaunchedEffect(key1 = Unit, block = {
        focuseRequester.requestFocus()
    })

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
    ) {
        Text(
            text = stringResource(R.string.request_loan),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            OutlinedTextField(
                value = amountLoan,
                onValueChange = {
                    amountLoan = it
                },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focuseRequester),
                placeholder = { Text(text = stringResource(id = R.string.amount)) },
                leadingIcon = {
                    Text(
                        text = Currency.getInstance(userModel.auth.value!!.user.currency).getSymbol(
                            Locale(
                                Locale.getDefault().language,
                                Locale.getDefault().country
                            )
                        ), fontSize = 15.sp
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.5f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                trailingIcon = {
                    IconPicker(isError = amountLoanError, unit = "")
                },
                supportingText = {
                    ErrorHint(isError = amountLoanError)
                },
                isError = amountLoanError
            )
            IconButton(
                onClick = {
                    amountLoanError = (amountLoan == "" || amountLoan == "0")
                    if (amountLoanError) return@IconButton
                    userModel.getAuthUserId()?.let {
                        userModel.addTransaction(
                            it,
                            Transaction(
                                userModel.getAuthUserId()!!,
                                amountLoan.toFloat(),
                                LocalDate.now(),
                            )
                        )
                        amountLoan = ""
                        amountLoanError = false
                        closeBottomSheet()
                    }
                },
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(paddingValues = PaddingValues(4.dp))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "",
                )
            }
        }
    }
}

//0xFFFFBC06

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransferMoney(
    userModel: UserModel,
    closeBottomSheet: () -> Unit
) {

    var amountTransfer by rememberSaveable { mutableStateOf("") }
    var amountTransferError by rememberSaveable { mutableStateOf(false) }

    var transferTo by rememberSaveable { mutableStateOf("") }
    var transferToError by rememberSaveable { mutableStateOf(false) }

    val focuseRequester by remember {
        mutableStateOf(FocusRequester())
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        focuseRequester.requestFocus()
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFFFBC06),
                RoundedCornerShape(16.dp),
            )
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.transfer_money),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = transferTo,
            onValueChange = {
                transferTo = it.trim()
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focuseRequester),
            placeholder = { Text(text = stringResource(id = R.string.username)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.5f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            trailingIcon = {
                IconPicker(isError = transferToError, unit = "")
            },
            supportingText = {
                ErrorHint(isError = transferToError)
            },
            isError = transferToError
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = amountTransfer,
            onValueChange = {
                amountTransfer = it
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = stringResource(id = R.string.amount)) },
            leadingIcon = {
                Text(
                    text = Currency.getInstance(userModel.auth.value!!.user.currency).getSymbol(
                        Locale(
                            Locale.getDefault().language,
                            Locale.getDefault().country
                        )
                    ), fontSize = 15.sp
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.5f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            trailingIcon = {
                IconPicker(isError = amountTransferError, unit = "")
            },
            supportingText = {
                ErrorHint(isError = amountTransferError)
            },
            isError = amountTransferError
        )

        Spacer(modifier = Modifier.size(16.dp))

        IconButton(
            onClick = {
                amountTransferError = (amountTransfer == "" || amountTransfer == "0")
                transferToError = (transferTo == "" || transferTo == "0")
                if (transferTo.lowercase() == userModel.getUsernameAuth()?.lowercase()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_same_name),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@IconButton
                }
                if (amountTransfer.toFloat() > userModel.getTotalAmount()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_less),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@IconButton
                }
                if (amountTransferError || transferToError) return@IconButton
                val transferSuccess = userModel.getUsernameAuth()?.let {
                    userModel.transferTo(
                        it,
                        transferTo,
                        amountTransfer.toFloat()
                    )
                }

                if (transferSuccess == false) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.username_notfound), Toast.LENGTH_SHORT
                    ).show()
                    return@IconButton
                }
                amountTransfer = ""
                transferTo = ""
                closeBottomSheet()
            },
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(paddingValues = PaddingValues(4.dp))
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "",
            )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListTransaction(user: User, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .size(250.dp)
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
                    status = if (user.transaction[user.transaction.size - it - 1].amount < 0) stringResource(
                        R.string.withdrawal
                    ) else stringResource(R.string.deposit),
                    amount = user.transaction[user.transaction.size - it - 1].amount,
                    currency = user.currency
                )
                Divider()
            }
        }
    }
}


@Composable
fun ItemTransaction(index: Int, status: String, amount: Float, currency: String) {
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
                text = formatCurrency(
                    amount,
                    currency,
                    Locale(Locale.getDefault().language, Locale.getDefault().country)
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 18.sp
            )
        }
    }
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
    return if (amount < 0) {
        "– ${formatter.format(amount.absoluteValue)}"
    } else {
        formatter.format(amount)
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

@Composable
fun IconPicker(isError: Boolean, unit: @Composable () -> Unit) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        unit()
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}


@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.error))
    }
}

fun main() {
    val amount = 1234.56f
    val locale = Locale.US
    val formattedAmount = formatCurrency(amount, locale)
    println(formattedAmount) // Output: 1,234.56
}


