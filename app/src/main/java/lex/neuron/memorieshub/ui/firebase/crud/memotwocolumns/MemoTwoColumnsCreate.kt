package lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.fireentity.MemoTwoColumnsFirebase
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.MEMO_TWO_COLUMNS

class MemoTwoColumnsCreate {
    fun createMemoTwoColumn(memoEntity: MemoEntity, idMemo: Long, idTitle: Int) {
        val memo = MemoTwoColumnsFirebase(
            idMemo, memoEntity.title,
            memoEntity.testable,
            memoEntity.description,
            memoEntity.createdDateFormatted,
            idTitle
        )

        val uid = AUTH.currentUser?.uid.toString()

        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
        database.child("title $idTitle, memo $idMemo").setValue(memo)
    }
}