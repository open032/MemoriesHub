package lex.neuron.memorieshub.ui.firebase.crud.dir

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.permission.internet.TAG
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.DIR
import lex.neuron.memorieshub.util.MEMO_TWO_COLUMNS
import lex.neuron.memorieshub.util.TITLE

class DirDelete {

    fun deleteDir(dirEntity: DirEntity, listTitle: MutableList<Int>, listMemo: MutableList<MemoForDelete>) {

        Log.d(TAG, "deleteDir listTitle: $listTitle")
        Log.d(TAG, "deleteDir listMemo: $listMemo")
        val dirId = dirEntity.id
        val uid = AUTH.currentUser?.uid.toString()

        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(DIR).child(uid)
                .child("dir $dirId")
        database.removeValue()

        // delete Title if nested
        val sizeTitle = listTitle.size - 1
        for (i in 0..sizeTitle) {
            val db: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(TITLE).child(uid)
                    .child("dir $dirId, title ${listTitle[i]}")
            db.removeValue()
        }

        // delete Memo if nested
        val sizeMemo = listMemo.size - 1
        for (i in 0..sizeMemo) {
            val db: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
                    .child("title ${listMemo[i].titleId}, memo ${listMemo[i].memoId}")
            db.removeValue()
        }
    }
}