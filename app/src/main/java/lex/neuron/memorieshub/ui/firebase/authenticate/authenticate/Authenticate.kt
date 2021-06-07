@file:Suppress("DEPRECATION")

package lex.neuron.memorieshub.ui.firebase.authenticate.authenticate

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import lex.neuron.memorieshub.R

@AndroidEntryPoint
class Authenticate : Fragment(R.layout.frag_authenticate_firebase) {
//    private lateinit var textView: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCvpWait: CircularProgressView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        mCvpWait.visibility = View.VISIBLE

        if (user != null) {
            Navigation.findNavController(view).navigate(R.id.action_authenticate_to_bottomTest)
//            mCvpWait.visibility = View.GONE
//            textView.text = "user != null"
            Log.e(TAG, "user =! null",)
        } else {
            Navigation.findNavController(view).navigate(R.id.action_authenticate_to_signIn)

//            mCvpWait.visibility = View.GONE
//            textView.text = "Registration"
            Log.e(TAG, "user = null", )
        }
    }

    private fun init(view: View) {
//        textView = view.findViewById(R.id.text_in_auth)
        mCvpWait = view.findViewById(R.id.cpv_auth)
    }
}