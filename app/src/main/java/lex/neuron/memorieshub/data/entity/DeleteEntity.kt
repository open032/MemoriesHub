package lex.neuron.memorieshub.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delete_table")
data class DeleteEntity(
    val name: String,
    val id: Int = -1,
    val secondId: Int = -1,
    @PrimaryKey(autoGenerate = true) val idPrimary: Int = 0
)