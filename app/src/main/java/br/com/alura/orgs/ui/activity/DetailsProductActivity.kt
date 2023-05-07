package br.com.alura.orgs.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityDetailsProductBinding
import br.com.alura.orgs.extensions.formatterCoinPtBr
import br.com.alura.orgs.extensions.loadImage
import br.com.alura.orgs.model.Product
import kotlinx.coroutines.launch

private const val TAG = "DetailsProduct"

class DetailsProductActivity : AppCompatActivity() {

    private var product: Product? = null
    private var idProduct: Long = 0L
    private val binding by lazy {
        ActivityDetailsProductBinding.inflate(layoutInflater)
    }
    private val productDao by lazy {
        AppDatabase.instance(this).productDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Detalhes do produto"
        loadProduct()
    }

    override fun onResume() {
        super.onResume()
        searchProduct()

    }

    private fun searchProduct() {
        lifecycleScope.launch {
            product = productDao.searchForId(idProduct)
            product?.let {
                fillInFields(it)
            } ?: finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details_product, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_details_product_edit -> {
                Intent(this, FormProductsActivity::class.java).apply {
                    putExtra(PRODUCT_ID_KEY, idProduct)
                    startActivity(this)
                }
                Log.i(TAG, "onOptionsItemSelected: editando")
            }
            R.id.menu_details_product_delete -> {
                lifecycleScope.launch {
                    product?.let { productDao.delete(it) }
                    finish()
                }
                Log.i(TAG, "onOptionsItemSelected: removendo")
            }
        }
        return true
    }


    private fun loadProduct() {
        idProduct = intent.getLongExtra(PRODUCT_ID_KEY, 0L)
    }

    private fun fillInFields(productLoaded: Product) {
        with(binding) {
            activityDetailsProductTitle.text = productLoaded.name
            activityDetailsProductDesc.text = productLoaded.desc
            activityDetailsProductValue.text = productLoaded.value.formatterCoinPtBr()
            activityDetailsProductImageView.loadImage(productLoaded.image)
        }
    }
}