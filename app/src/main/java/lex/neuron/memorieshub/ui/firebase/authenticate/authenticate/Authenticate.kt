@file:Suppress("DEPRECATION")

package lex.neuron.memorieshub.ui.firebase.authenticate.authenticate

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.util.AUTH

@AndroidEntryPoint
class Authenticate : Fragment(R.layout.frag_authenticate_firebase) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AUTH = FirebaseAuth.getInstance()
        val user = AUTH.currentUser



        if (user != null) {
            Navigation.findNavController(view).navigate(R.id.action_authenticate_to_dir)
            Log.e(TAG, "user =! null",)
        } else {
            Navigation.findNavController(view).navigate(R.id.action_authenticate_to_signIn)

            Log.e(TAG, "user = null", )
        }
    }
}