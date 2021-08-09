package lex.neuron.memorieshub.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo_from_firebase")
data class MemoFromFirebase(
    @ColumnInfo(index = true)
    val titleList: Int,
    val title: String,
    val testable: Boolean = true,
    val sendNetCreateUpdate: Boolean = false,
    val sendNetDelete: Boolean = false,
    val description: String,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey() val id: Int = 0
)
