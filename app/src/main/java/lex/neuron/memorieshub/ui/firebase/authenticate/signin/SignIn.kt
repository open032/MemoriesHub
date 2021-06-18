package lex.neuron.memorieshub.ui.firebase.authenticate.signin

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.databinding.FragSignInBinding
import lex.neuron.memorieshub.util.AUTH


@AndroidEntryPoint
class SignIn : Fragment(R.layout.frag_sign_in) {
    private val viewModel: SignInViewModel by viewModels()

    private lateinit var mView: View
    private lateinit var binding: FragSignInBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragSignInBinding.bind(view)

        binding.apply {
            btnSignIn.setOnClickListener {
                viewModel.signIn()
                btnSignIn.visibility = View.GONE
            }
        }

        mView = view



        viewModel.googleSignInClient = GoogleSignIn.getClient(requireContext(), viewModel.gso(getString(R.string.default_web_client_id)))

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.signInEvent.collect { event ->
                when (event) {
                    is SignInViewModel.SignInEvent.SignIn -> {
                        resultLauncher.launch(Intent(viewModel.googleSignInClient.getSignInIntent()))
                    }
                }
            }
        }
    }


    var resultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //                updateUI(null)
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        binding.circularPv.visibility = View.VISIBLE


        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        viewModel.auth.signInWithCredential(credential)
        AUTH.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Navigation.findNavController(mView).navigate(R.id.action_signIn_to_listMainFrag)
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(ContentValues.TAG, "signInWithCredential:success")
//                    val user = viewModel.auth.currentUser
                    val user = AUTH.currentUser
//                    updateUI(user)
                } else {
                    Log.e(ContentValues.TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }
}