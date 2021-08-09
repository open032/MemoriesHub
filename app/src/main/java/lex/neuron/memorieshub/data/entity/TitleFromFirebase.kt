package lex.neuron.memorieshub.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "title_form_firebase")
data class TitleFromFirebase(
    @ColumnInfo(index = true)
    val dirList: Int,
    val name: String,
    val sendNetCreateUpdate: Boolean = false,
    val sendNetDelete: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey()val id: Int = 0
)
