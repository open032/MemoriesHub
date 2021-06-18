package lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.MEMO_TWO_COLUMNS

class MemoTwoColumnsDelete {
    fun deleteMemoTwoColumns(memo: MemoEntity) {
        val idTitle = memo.titleList
        val idMemo = memo.id

        val uid = AUTH.currentUser?.uid.toString()
        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
                .child("title $idTitle, memo $idMemo")
        database.removeValue()

    }
}