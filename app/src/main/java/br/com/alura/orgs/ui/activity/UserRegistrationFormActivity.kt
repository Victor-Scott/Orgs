package br.com.alura.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityUserRegistrationFormBinding
import br.com.alura.orgs.extensions.toHash
import br.com.alura.orgs.model.User
import kotlinx.coroutines.launch

class UserRegistrationFormActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityUserRegistrationFormBinding.inflate(layoutInflater)
    }
    private val userDao by lazy {
        AppDatabase.instance(this).userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configRegisterButton()
    }

    private fun configRegisterButton() {
        binding.activityFormRegistrationButtonRegister.setOnClickListener {
            val newUser = createUser()
            Log.i("RegistrationUser", "onCreate: $newUser")
            lifecycleScope.launch {
                try {
                    userDao.save(newUser)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@UserRegistrationFormActivity,
                        "Erro ao cadastrar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun createUser(): User {
        val usuario = binding.activityFormRegistrationUser.text.toString()
        val nome = binding.activityFormRegistrationName.text.toString()
        val senha = binding.activityFormRegistrationPassword.text.toString().toHash()
        return User(usuario, nome, senha)
    }
}