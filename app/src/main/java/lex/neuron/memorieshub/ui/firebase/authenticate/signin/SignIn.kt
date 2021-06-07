package lex.neuron.memorieshub.ui.firebase.authenticate.signin

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.databinding.FragSignInBinding

@AndroidEntryPoint
class SignIn : Fragment(R.layout.frag_sign_in) {
    private val viewModel: SignInViewModel by viewModels()

    private lateinit var textView: TextView
    private lateinit var btnSignIn: Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mView: View

    companion object {
        private const val RC_SIGN_IN = 120
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragSignInBinding.bind(view)

        binding.apply {

        }

        mView = view

        init(view)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        btnSignIn.setOnClickListener    {
            signIn()
            btnSignIn.visibility = View.GONE
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun init(view: View) {
        textView = view.findViewById(R.id.text_sign_in)
        btnSignIn = view.findViewById(R.id.btn_sign_in)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.e(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e(ContentValues.TAG, "Google sign in failed", e)
//                updateUI(null)
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        textView.text = "firebaseAuthWithGoogle"
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Navigation.findNavController(mView).navigate(R.id.action_addEditDir_to_bottomTest)
                    // Sign in success, update UI with the signed-in user's information
                    textView.text = "googleSignInClient in fragment"
                    Log.e(ContentValues.TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
//                    updateUI(user)
                } else {
                    textView.text = "signInWithCredential:failure"
                    Log.e(ContentValues.TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }
}