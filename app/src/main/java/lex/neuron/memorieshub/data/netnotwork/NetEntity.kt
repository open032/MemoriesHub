package lex.neuron.memorieshub.data.netnotwork

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "net_table")
data class NetEntity(
    val dirId: Long = 0,
    val titleId: Int = 0,
    val memoTwoColumn: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
