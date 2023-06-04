package br.com.none.orgs.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
    @PrimaryKey
    val uid: String,
    val name: String,
    val password: String
)
