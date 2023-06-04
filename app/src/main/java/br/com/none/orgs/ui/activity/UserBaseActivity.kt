package br.com.none.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.com.none.orgs.database.AppDatabase
import br.com.none.orgs.extensions.goTo
import br.com.none.orgs.model.User
import br.com.none.orgs.preferences.dataStore
import br.com.none.orgs.preferences.userLoggedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class UserBaseActivity : AppCompatActivity() {

    private val userDao by lazy {
        AppDatabase.instance(this).userDao()
    }
    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    protected val user: StateFlow<User?> = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLoggedUser()
    }

    private fun checkLoggedUser() {
        lifecycleScope.launch {
            dataStore.data.collect { preferences ->
                preferences[userLoggedPreferences]?.let {
                    searchUser(it)
                } ?: goToLogin()
            }
        }
    }

    private suspend fun searchUser(it: String): User? {
        return userDao.searchForId(it).firstOrNull().also {
            _user.value = it
        }
    }

    protected fun logoutUser() {
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences.remove(userLoggedPreferences)
            }
        }
    }

    private fun goToLogin() {
        goTo(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }


}