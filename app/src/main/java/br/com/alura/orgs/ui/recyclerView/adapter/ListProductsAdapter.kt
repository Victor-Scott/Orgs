package br.com.alura.orgs.ui.recyclerView.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.orgs.R
import br.com.alura.orgs.databinding.ProductItemBinding
import br.com.alura.orgs.extensions.formatterCoinPtBr
import br.com.alura.orgs.extensions.loadImage
import br.com.alura.orgs.model.Product

class ListProductsAdapter(
    val context: Context,
    products: List<Product> = emptyList(),
    var whenClickOnItem: (product: Product) -> Unit = {},
    var whenClickEditar: (product: Product) -> Unit = {},
    var whenClickRemover: (product: Product) -> Unit = {}
) : RecyclerView.Adapter<ListProductsAdapter.ListProductsViewHolder>() {

    private val products = products.toMutableList()


    inner class ListProductsViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        PopupMenu.OnMenuItemClickListener {

        private lateinit var product: Product

        init {
            itemView.setOnClickListener {
                if (::product.isInitialized) {
                    whenClickOnItem(product)
                }
            }

            itemView.setOnLongClickListener {
                PopupMenu(context, itemView).apply {
                    menuInflater.inflate(
                        R.menu.menu_details_product,
                        menu
                    )
                    setOnMenuItemClickListener(this@ListProductsViewHolder)
                }.show()
                true
            }


        }

        fun binds(product: Product) {
            this.product = product
            val name = binding.productItemName
            name.text = product.name
            val desc = binding.productItemDesc
            desc.text = product.desc
            val value = binding.productItemValue
            val valueCoin = product.value.formatterCoinPtBr()
            value.text = valueCoin

            val visibility = if (product.image != null) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.productItemImageView.visibility = visibility
            val url = binding.productItemImageView
            binding.productItemImageView.loadImage(product.image)
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (item.itemId) {
                    R.id.menu_details_product_edit -> {
                        whenClickEditar(product)
                    }
                    R.id.menu_details_product_delete -> {
                        whenClickRemover(product)
                    }
                }
            }
            return true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProductsViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ListProductsViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ListProductsViewHolder, position: Int) {
        val product = products[position]
        holder.binds(product)
    }

    fun update(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()

    }

}
