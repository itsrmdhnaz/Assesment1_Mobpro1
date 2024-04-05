package org.d3if3026.assesment1.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if3026.assesment1.screen.formatCurrency
import java.time.LocalDate
import java.util.Locale
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class UserModel : ViewModel(){
    private val _data = MutableLiveData<List<User>>()
    private val data: LiveData<List<User>> = _data
    private val _auth = MutableLiveData<Auth?>()
    val auth: LiveData<Auth?> = _auth

    init {
        _data.value = getDataDummy()
        _auth.value = data.value?.let { Auth(it[0]) }
    }

    fun getAuthUserId(): UUID? {
        return auth.value?.user?.id
    }

    fun login(username: String, password: String): User? {
        val user = _data.value.orEmpty().find { it.username == username && it.password == password }
        if (user != null) {
            _auth.value = Auth(user)
        }
        return user
    }

    fun logout() {
        _auth.value = null
    }

    private fun getUserByUUID(userId: UUID): User? {
        return _data.value?.find { it.id == userId }
    }

    fun getCurrencyAuth(): String {
        return _auth.value?.user?.currency ?: return "IDR"
    }

    fun getUsernameAuth(): String? {
        return _auth.value?.user?.username
    }

    fun getTotalIncome(): String? {
        val currentUser = _auth.value?.user ?: return ""
        return auth.value?.user?.let { it ->
            formatCurrency(currentUser.transaction.filter { it.amount > 0 }.sumOf { it.amount.toDouble() }.toFloat(), it.currency,
                Locale(Locale.getDefault().language, Locale.getDefault().language))
        }
    }

    fun getTotalOutcome(): String? {
        val currentUser = _auth.value?.user ?: return ""
        return auth.value?.user?.let { it ->
            formatCurrency(currentUser.transaction.filter { it.amount < 0 }.sumOf { it.amount.toDouble() }.toFloat(), it.currency,
                Locale(Locale.getDefault().language, Locale.getDefault().language))
        }
    }

    fun getTotalAmount(): Float {
        val currentUser = _auth.value?.user ?: return 0f
        return currentUser.transaction.sumOf { it.amount.toDouble() }.toFloat()
    }

    fun transferTo(senderUsername: String, receiverUsername: String, amount: Float): Boolean {
        val sender = _data.value.orEmpty().find { it.username == senderUsername.lowercase() }
        val receiver = _data.value.orEmpty().find { it.username == receiverUsername.lowercase() }

        if(senderUsername.lowercase() == receiverUsername.lowercase()){
            return false
        }

        if (sender != null && receiver != null) {
            val convertedAmount = convertCurrency(sender.currency, receiver.currency, amount)

            val transactionSender = Transaction(UUID.randomUUID(), amount * -1, LocalDate.now(),
                receiver.username
            )
            val transactionReceiver = Transaction(UUID.randomUUID(), convertedAmount, LocalDate.now(),
                sender.username
            )

            addTransaction(sender.id, transactionSender)
            addTransaction(receiver.id, transactionReceiver)
            return true
        }
        return false
    }


    fun addTransaction(userId: UUID, transaction: Transaction) {
        val currentData = _data.value.orEmpty().toMutableList()
        val updatedData = currentData.map { user ->
            if (user.id == userId) {
                val updatedTransactionList = user.transaction + transaction
                user.copy(transaction = updatedTransactionList)
            } else {
                user
            }
        }
        _data.value = updatedData
        _auth.value = auth.value?.user?.let { it -> getUserByUUID(it.id)?.let { Auth(it) } }
    }
}

fun convertCurrency(
    fromCurrency: String,
    toCurrency: String,
    amount: Float
): Float {
    val usdToIdr = 15000.0f
    val eurToIdr = 17000.0f
    val gbpToIdr = 20000.0f

    // Convert the amount to IDR first
    val amountInIdr: Float = when (fromCurrency) {
        "USD" -> amount * usdToIdr
        "EUR" -> amount * eurToIdr
        "GBP" -> amount * gbpToIdr
        "IDR" -> amount
        else -> 0f
    }

    // Convert from IDR to the target currency
    return when (toCurrency) {
        "USD" -> {
            if (fromCurrency == "IDR") {
                amount / usdToIdr
            } else {
                amountInIdr / usdToIdr
            }
        }
        "EUR" -> {
            if (fromCurrency == "IDR") {
                amount / eurToIdr
            } else {
                amountInIdr / eurToIdr
            }
        }
        "GBP" -> {
            if (fromCurrency == "IDR") {
                amount / gbpToIdr
            } else {
                amountInIdr / gbpToIdr
            }
        }
        "IDR" -> amountInIdr
        else -> 0f
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun getDataDummy(): List<User> {
    val password = "6706223026"
    return listOf(
        User(
            id = UUID.randomUUID(),
            username = "rmdhnaz",
            password = password,
            transaction = List(10) { index ->
                Transaction(UUID.randomUUID(), (index + 1) * 100.0f, LocalDate.now(), "Transaction ${index + 1}")
            },
            currency = "IDR"
        ),
        User(
            id = UUID.randomUUID(),
            username = "itsme",
            password = password,
            transaction = List(10) { index ->
                Transaction(UUID.randomUUID(), (index + 1) * 200.0f, LocalDate.now(), "Transaction ${index + 1}")
            },
            currency = "EUR"
        ),
        User(
            id = UUID.randomUUID(),
            username = "admin",
            password = password,
            transaction = List(10) { index ->
                Transaction(UUID.randomUUID(), (index + 1) * 300.0f, LocalDate.now(), "Transaction ${index + 1}")
            },
            currency = "GBP"
        ),
        User(
            id = UUID.randomUUID(),
            username = "Mobpro",
            password = password,
            transaction = List(10) { index ->
                Transaction(UUID.randomUUID(), (index + 1) * 300.0f, LocalDate.now(), "Transaction ${index + 1}")
            },
            currency = "USD"
        )
    )
}