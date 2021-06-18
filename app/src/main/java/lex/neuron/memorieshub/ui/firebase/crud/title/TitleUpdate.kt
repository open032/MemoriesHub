package lex.neuron.memorieshub.ui.firebase.crud.title

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.data.fireentity.TitleFirebase
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.TITLE

class TitleUpdate {
    fun updateTitle(titleEntity: TitleEntity) {
        val idDir = titleEntity.dirList
        val idTitle = titleEntity.id

        val title = TitleFirebase(
            titleEntity.id.toLong(),
            titleEntity.name,
            titleEntity.createdDateFormatted,
            titleEntity.dirList
        )

        val uid = AUTH.currentUser?.uid.toString()

        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(TITLE).child(uid)
        database.child("dir $idDir, title $idTitle").setValue(title)
    }
}