package br.com.none.orgs.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Entity
@Parcelize
data class Product(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0L,

    val name: String,
    val desc: String,
    val value: BigDecimal,
    val image: String? = null,
    val userId: String? = null
) : Parcelable
