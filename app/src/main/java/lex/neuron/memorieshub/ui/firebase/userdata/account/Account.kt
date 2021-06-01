package lex.neuron.memorieshub.ui.firebase.authenticate.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.databinding.FragAccountBinding

@AndroidEntryPoint
class Account : Fragment(R.layout.frag_account) {
    private val viewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragAccountBinding.bind(view)

        binding.apply {
            txtName.text = viewModel.authName
            txtEmail.text = viewModel.authEmail
            Glide.with(requireContext()).load(viewModel.photoUrl).into(profileImage)
            signOut.setOnClickListener{
                viewModel.authSignOut()
                Navigation.findNavController(view).navigate(R.id.action_account_to_signIn)
            }

        }

    }
}