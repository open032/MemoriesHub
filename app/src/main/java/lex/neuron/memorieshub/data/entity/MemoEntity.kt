package lex.neuron.memorieshub.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "memo_table",
    foreignKeys = [ForeignKey(
        entity = TitleEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("titleList"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class MemoEntity(
    @ColumnInfo(index = true)
    val titleList: Int,
    val title: String,
    val description: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

)
