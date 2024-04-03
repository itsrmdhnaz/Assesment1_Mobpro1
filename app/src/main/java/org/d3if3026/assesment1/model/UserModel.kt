package org.d3if3026.assesment1.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class UserModel : ViewModel(){
    private val _data = MutableLiveData<List<User>>()
    val data: LiveData<List<User>> = _data
    private val _auth = MutableLiveData<Auth?>()
    val auth: LiveData<Auth?> = _auth

    init {
        _data.value = getDataDummy()
        _auth.value = data.value?.firstOrNull()?.let { Auth(it) }
    }

    fun addUser(user: User) {
        val currentData = _data.value.orEmpty().toMutableList()
        currentData.add(user)
        _data.value = currentData
    }

    fun deleteUserById(userId: UUID) {
        val currentData = _data.value.orEmpty().toMutableList()
        val updatedData = currentData.filter { it.id != userId }
        _data.value = updatedData
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

    fun getUserById(userId: UUID): User? {
        return _data.value?.find { it.id == userId }
    }

    fun getTotalIncome(): Float {
        val currentUser = _auth.value?.user ?: return 0f
        return currentUser.transaction.filter { it.amount > 0 }.sumByDouble { it.amount.toDouble() }.toFloat()
    }

    fun getTotalOutcome(): Float {
        val currentUser = _auth.value?.user ?: return 0f
        return currentUser.transaction.filter { it.amount < 0 }.sumByDouble { it.amount.toDouble() }.toFloat()
    }

    fun getTotalAmount(): Float {
        val currentUser = _auth.value?.user ?: return 0f
        return currentUser.transaction.sumByDouble { it.amount.toDouble() }.toFloat()
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
        _auth.value = getUserById(userId)?.let { Auth(it) }
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
            currency = "USD"
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
        )
    )
}