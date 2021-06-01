package lex.neuron.memorieshub.ui.firebase.authenticate.account

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth

class AccountViewModel @ViewModelInject constructor(
    application: Application
) : AndroidViewModel(application) {


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    var authName = currentUser?.displayName
    var authEmail = currentUser?.email
    var photoUrl = currentUser?.photoUrl

    fun authSignOut() {
        auth.signOut()
//        Navigation.findNavController(getApplication()).navigate(R.id.action_account_to_signIn)
    }

}
