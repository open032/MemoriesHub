package lex.neuron.memorieshub.ui.firebase.crud.read

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AccountData {

    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    fun getAccountData() {
        Log.d(TAG, "getAccountData: ${currentUser?.photoUrl}")
    }

    fun getImageAccount(): String {
        return currentUser?.photoUrl.toString()
    }
}