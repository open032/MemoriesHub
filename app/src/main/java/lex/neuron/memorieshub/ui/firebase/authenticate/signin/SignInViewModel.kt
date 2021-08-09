package lex.neuron.memorieshub.ui.firebase.authenticate.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    val dao: RoomDao
) : ViewModel() {
    private val eventChannel = Channel<SignInEvent>()
    lateinit var googleSignInClient: GoogleSignInClient

    val signInEvent = eventChannel.receiveAsFlow()


    fun gso(str: String): GoogleSignInOptions {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(str)
            .requestEmail()
            .build()
        return gso
    }


    fun signIn() = viewModelScope.launch {
        eventChannel.send(SignInEvent.SignIn)
    }

    sealed class SignInEvent {
        object SignIn : SignInEvent()
    }
}