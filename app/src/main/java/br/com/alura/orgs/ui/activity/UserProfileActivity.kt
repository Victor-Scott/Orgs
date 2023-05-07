package br.com.alura.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.databinding.ActivityUserProfileBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class UserProfileActivity : UserBaseActivity() {

    private val binding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Perfil"
        configLogoutButton()
        fillInFields()

    }

    private fun configLogoutButton() {
        binding.activityUserProfileLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun fillInFields() {
        lifecycleScope.launch {
            user.filterNotNull().collect {
                binding.activityUserProfileName.text = it.name
                binding.activityUserProfileUser.text = it.uid
            }
        }
    }
}