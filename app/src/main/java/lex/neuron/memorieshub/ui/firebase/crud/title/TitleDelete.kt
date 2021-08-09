package lex.neuron.memorieshub.ui.firebase.crud.title

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.MEMO_TWO_COLUMNS
import lex.neuron.memorieshub.util.TITLE

class TitleDelete {
    fun deleteTitle(titleEntity: TitleEntity, list: MutableList<Int>) {

        val dirId = titleEntity.dirList
        val titleId = titleEntity.id

        val uid = AUTH.currentUser?.uid.toString()
        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(TITLE).child(uid)
                .child("dir $dirId, title $titleId")

        database.removeValue()

        // delete Memo if nested
        val size = list.size - 1
        for (i in 0..size) {
            val db: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
                    .child("title $titleId, memo ${list[i]}")
            db.removeValue()
        }
    }
}