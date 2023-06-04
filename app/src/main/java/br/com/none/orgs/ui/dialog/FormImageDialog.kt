package br.com.none.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.com.none.orgs.databinding.FormImageBinding
import br.com.none.orgs.extensions.loadImage

class FormImageDialog(private val context: Context) {

    fun show(whenLoaded: (image: String) -> Unit) {

        val binding by lazy {
            FormImageBinding.inflate(LayoutInflater.from(context))
        }

        binding.formImageButtonUpload.setOnClickListener {
            val url = binding.formImageTietUrl.text.toString()
            binding.formImageImageView.loadImage(url)
        }

        AlertDialog.Builder(context)
            .setView(binding.root)
            .setPositiveButton("Confirmar") { _, _ ->
                val url = binding.formImageTietUrl.text.toString()
                whenLoaded(url)
            }
            .setNegativeButton("Cancelar") { _, _ ->

            }.show()
    }
}