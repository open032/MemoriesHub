package lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.fireentity.MemoTwoColumnsFirebase
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.MEMO_TWO_COLUMNS

class MemoTwoColumnsUpdate {
    fun updateMemoTwoColumns(updateMemoEntity: MemoEntity) {
        val idTitle = updateMemoEntity.titleList
        val idMemo = updateMemoEntity.id

        val memo = MemoTwoColumnsFirebase(
            updateMemoEntity.id.toLong(),
            updateMemoEntity.title,
            updateMemoEntity.testable,
            updateMemoEntity.description,
            updateMemoEntity.createdDateFormatted,
            updateMemoEntity.titleList
        )

        val uid = AUTH.currentUser?.uid.toString()

        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
        database.child("title $idTitle, memo $idMemo").setValue(memo)
    }
}