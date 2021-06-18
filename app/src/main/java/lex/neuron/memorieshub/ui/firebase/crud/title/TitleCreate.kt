package lex.neuron.memorieshub.ui.firebase.crud.title

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.data.fireentity.TitleFirebase
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.TITLE

class TitleCreate {

    fun createTitle(newTitle: TitleEntity, idTitle: Long, idDir: Int) {
        val title = TitleFirebase(
            idTitle, newTitle.name,
            newTitle.createdDateFormatted,
            idDir
        )

        val uid = AUTH.currentUser?.uid.toString()

        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(TITLE).child(uid)
        database.child("dir $idDir, title $idTitle").setValue(title)
    }
}