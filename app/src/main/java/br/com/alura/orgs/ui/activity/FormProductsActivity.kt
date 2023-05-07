package br.com.alura.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityFormProductsBinding
import br.com.alura.orgs.extensions.loadImage
import br.com.alura.orgs.model.Product
import br.com.alura.orgs.ui.dialog.FormImageDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FormProductsActivity : UserBaseActivity() {

    private val binding by lazy {
        ActivityFormProductsBinding.inflate(layoutInflater)
    }

    private val productDao by lazy {
        AppDatabase.instance(this).productDao()
    }

    private var url: String? = null
    private var idProduct: Long = 0L
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar produto"
        configSaveButton()


        val imageView = binding.activityFormProductsImageView
        imageView.setOnClickListener {
            FormImageDialog(this).show { image ->
                url = image
                binding.activityFormProductsImageView.loadImage(url)
            }
        }

        loadProduct()

    }

    private fun loadProduct() {

        lifecycleScope.launch {
            idProduct = intent.getLongExtra(PRODUCT_ID_KEY, 0L)
            product = productDao.searchForId(idProduct)
            product?.let { product ->
                fillInFields(product)
            }
        }

    }

    private fun fillInFields(product: Product) {
        title = "Editar Produto"
        url = product.image
        with(binding) {
            activityFormProductsTietName.setText(product.name)
            activityFormProductsTietDesc.setText(product.desc)
            activityFormProductsTietValue.setText(product.value.toPlainString())
            activityFormProductsImageView.loadImage(product.image)
        }
    }


    private fun configSaveButton() {
        val saveButton = binding.activityFormProductsSaveButton
        saveButton.setOnClickListener {
            lifecycleScope.launch {
                user.value?.let {user ->
                    val newProduct = createProduct(user.uid)
                    productDao.save(newProduct)
                    finish()
                }
            }

        }
    }

    private fun createProduct(userId: String): Product {
        val nameSpace = binding.activityFormProductsTietName
        val name = nameSpace.text.toString()
        val descSpace = binding.activityFormProductsTietDesc
        val desc = descSpace.text.toString()
        val valueSpace = binding.activityFormProductsTietValue.text.toString()
        val value = if (valueSpace.isBlank()) BigDecimal.ZERO else BigDecimal(valueSpace)

        val newProduct = Product(
            uid = idProduct,
            name = name,
            desc = desc,
            value = value,
            image = url,
            userId = userId
        )
        return newProduct
    }

}