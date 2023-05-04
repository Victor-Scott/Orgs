package br.com.alura.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityLoginBinding
import br.com.alura.orgs.extensions.goTo
import br.com.alura.orgs.extensions.toHash
import br.com.alura.orgs.preferences.dataStore
import br.com.alura.orgs.preferences.userLoggedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val userDao by lazy {
        AppDatabase.instance(this).userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configRegisterButton()
        configEnterButton()
    }

    private fun configEnterButton() {
        binding.activityLoginButtonEnter.setOnClickListener {
            val user = binding.activityLoginUser.text.toString()
            val password = binding.activityLoginPassword.text.toString().toHash()
            Log.i("LoginActivity", "onCreate: $user - $password")

            lifecycleScope.launch {
                userDao.authenticate(user, password)?.let {
                    dataStore.edit { preferences ->
                        preferences[userLoggedPreferences] = it.uid
                    }
                    goTo(ListProductsActivity::class.java)
                    finish()
                } ?: Toast.makeText(
                    this@LoginActivity,
                    "Falha na autenticação",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun configRegisterButton() {
        binding.activityLoginButtonRegister.setOnClickListener {
            goTo(UserRegistrationFormActivity::class.java)
        }
    }
}