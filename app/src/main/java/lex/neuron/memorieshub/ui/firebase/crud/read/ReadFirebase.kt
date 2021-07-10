package lex.neuron.memorieshub.ui.firebase.crud.read

import android.util.Log
import com.google.firebase.database.*
import lex.neuron.memorieshub.permission.internet.TAG
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.DIR
import lex.neuron.memorieshub.util.MEMO_TWO_COLUMNS
import lex.neuron.memorieshub.util.TITLE


class ReadFirebase {


    fun readData() {
        val uid = AUTH.currentUser?.uid.toString()

        val databaseDir: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(DIR).child(uid)

        val databaseTitle: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(TITLE).child(uid)

        val databaseMemo: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)

        databaseDir.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                getData(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        databaseTitle.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                getData(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        databaseMemo.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                getData(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}


fun getData(dataSnapshot: DataSnapshot) {
    for (ds in dataSnapshot.children) {
/*
            val key = ds.key
            val city = ds.child("city").getValue(String::class.java)
            val name = ds.child("name").getValue(String::class.java)
*/
        Log.e(TAG, "getData: $ds")
    }
}