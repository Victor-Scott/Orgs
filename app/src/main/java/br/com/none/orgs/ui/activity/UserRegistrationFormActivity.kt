package br.com.none.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import br.com.none.orgs.database.AppDatabase
import br.com.none.orgs.databinding.ActivityUserRegistrationFormBinding
import br.com.none.orgs.extensions.toHash
import br.com.none.orgs.extensions.toast
import br.com.none.orgs.model.User
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
            saveUser(newUser)
        }
    }

    private fun saveUser(user: User) {
        lifecycleScope.launch {
            try {
                userDao.save(user)
                finish()
            } catch (e: Exception) {
                Log.e("RegistrationForm", "saveUser: $e")
                toast("Erro ao cadastrar")
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