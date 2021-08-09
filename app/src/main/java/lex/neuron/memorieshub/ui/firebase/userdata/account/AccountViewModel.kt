package lex.neuron.memorieshub.ui.firebase.authenticate.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lex.neuron.memorieshub.util.AUTH
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    val currentUser = AUTH.currentUser
    var authName = currentUser?.displayName
    var authEmail = currentUser?.email
    var photoUrl = currentUser?.photoUrl

    fun authSignOut() {
        AUTH.signOut()
    }

}
