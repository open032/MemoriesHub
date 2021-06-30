package lex.neuron.memorieshub.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(
    tableName = "memo_table",
    foreignKeys = [ForeignKey(
        entity = TitleEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("titleList"),
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class MemoEntity(
    @ColumnInfo(index = true)
    val titleList: Int,
    val title: String,
    val sendNetCreateUpdate: Boolean = false,
    val sendNetDelete: Boolean = false,
    val description: String,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}
