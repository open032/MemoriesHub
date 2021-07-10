package lex.neuron.memorieshub.ui.firebase.crud.read

import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import lex.neuron.memorieshub.permission.internet.TAG

class AccountData {

    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    fun getAccountData() {

/*
        Log.e(TAG, "getAccountData: ${currentUser?.uid}")
        Log.e(TAG, "getAccountData: ${currentUser?.displayName}")
        Log.e(TAG, "getAccountData: ${currentUser?.email}")
*/

//        Glide.with(this).load(currentUser?.photoUrl).into(imageView);
        Log.e(TAG, "getAccountData: ${currentUser?.photoUrl}")

//        id_txt.text = currentUser?.uid
//        name_txt.text = currentUser?.displayName
//        email_txt.text = currentUser?.email
    }

    fun getImageAccount(): String {
        return currentUser?.photoUrl.toString()
    }

}