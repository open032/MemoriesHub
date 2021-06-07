package lex.neuron.memorieshub.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat


@Entity(
    tableName = "title_table",
    foreignKeys = [ForeignKey(
        entity = DirEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("dirList"),
        onDelete = ForeignKey.CASCADE
    )]
        )
@Parcelize
data class TitleEntity(
    @ColumnInfo(index = true)
    val dirList: Int,
    val name: String,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}
