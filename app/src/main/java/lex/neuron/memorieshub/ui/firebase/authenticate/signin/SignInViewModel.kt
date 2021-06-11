package lex.neuron.memorieshub.ui.firebase.authenticate.signin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.RoomDao

class SignInViewModel @ViewModelInject constructor(
) : ViewModel() {
    private val eventChannel = Channel<SignInEvent>()

    lateinit var googleSignInClient: GoogleSignInClient

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

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