package br.com.alura.orgs.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityListProductsBinding
import br.com.alura.orgs.extensions.goTo
import br.com.alura.orgs.model.Product
import br.com.alura.orgs.preferences.dataStore
import br.com.alura.orgs.preferences.userLoggedPreferences
import br.com.alura.orgs.ui.recyclerView.adapter.ListProductsAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ListProductsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityListProductsBinding.inflate(layoutInflater)
    }

    private val productDao by lazy {
        AppDatabase.instance(this).productDao()
    }

    private val userDao by lazy {
        AppDatabase.instance(this).userDao()
    }

    private var product: Product? = null
    private var idProduct: Long = 0L
    private val adapter = ListProductsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Orgs"
        configRecyclerView()
        configFab()
        lifecycleScope.launch {
            launch {
                productDao.searchAll().collect {
                    adapter.update(it)
                }
            }

            launch {
                dataStore.data.collect { preferences ->
                    preferences[userLoggedPreferences]?.let {
                        launch {
                            userDao.searchForId(it).collect {
                                Log.i("ListProducts", "onCreate: $it")
                            }
                        }
                    } ?: goToLogin()
                }
            }
        }
    }

    private fun goToLogin() {
        goTo(LoginActivity::class.java)
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_products, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycleScope.launch {
            createOptionsMenuOrder(item)
        }

        return true
    }

    private suspend fun createOptionsMenuOrder(item: MenuItem): Boolean {
        val productsOrdered: List<Product>? = when (item.itemId) {
            R.id.menu_list_products_name_asc ->
                productDao.searchAllOrderByNameAsc()
            R.id.menu_list_products_name_desc ->
                productDao.searchAllOrderByNameDesc()
            R.id.menu_list_products_desc_asc ->
                productDao.searchAllOrderByDescAsc()
            R.id.menu_list_products_desc_desc ->
                productDao.searchAllOrderByDescDesc()
            R.id.menu_list_products_value_asc ->
                productDao.searchAllOrderByValueAsc()
            R.id.menu_list_products_value_desc ->
                productDao.searchAllOrderByValueDesc()
            else -> null
        }

        when (item.itemId) {
            R.id.menu_list_products_no_order ->
                productDao.searchAll().collect {
                    adapter.update(it)
                }
            R.id.menu_list_products_logout ->
                lifecycleScope.launch {
                    dataStore.edit { preferences ->
                        preferences.remove(userLoggedPreferences)
                    }
                }

        }

        productsOrdered?.let {
            adapter.update(it)
        }
        return true
    }

    private fun configFab() {
        val fab = binding.activityListProductFab
        fab.setOnClickListener {
            val intent = Intent(this, FormProductsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configRecyclerView() {
        val recyclerView = binding.activityListProductRecyclerView
        recyclerView.adapter = adapter
        adapter.whenClickOnItem = {
            val intent = Intent(this, DetailsProductActivity::class.java).apply {
                putExtra(PRODUCT_ID_KEY, it.uid)
            }
            startActivity(intent)
        }

        adapter.whenClickEditar = {
            idProduct = it.uid
            Intent(this, FormProductsActivity::class.java).apply {
                putExtra(PRODUCT_ID_KEY, idProduct)
                startActivity(this)
            }
            Log.i("ListProducts", "configRecyclerView: Editar $it")
        }

        adapter.whenClickRemover = {
            idProduct = it.uid
            lifecycleScope.launch {
                product = productDao.searchForId(idProduct)
                product?.let { productDao.delete(it) }

            }
            Log.i("ListProducts", "configRecyclerView: Remover $it")
        }
    }
}
