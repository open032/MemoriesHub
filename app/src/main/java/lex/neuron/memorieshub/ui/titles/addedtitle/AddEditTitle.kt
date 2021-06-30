package lex.neuron.memorieshub.ui.titles.addedtitle

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.databinding.AddEditTitleBinding
import lex.neuron.memorieshub.util.exhaustive

@AndroidEntryPoint
class AddEditTitle : Fragment(R.layout.add_edit_title) {
    private val viewModel: AddTitleViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = AddEditTitleBinding.bind(view)

        binding.apply {
            editTextCardName.setText(viewModel.titleName)

            fabSaveCard.setOnClickListener {
                var name = editTextCardName.text.toString()
                val sendLaterNet = sendLaterNet()
                viewModel.onSaveClick(name, sendLaterNet)
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTitleEvent.collect { event ->
                when (event) {
                    AddTitleViewModel.AddEditTitleEvent.NavigateBack -> {
                        binding.editTextCardName.clearFocus()
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }

    private fun sendLaterNet(): Boolean {
        var info: NetworkInfo? = null
        var connectivity =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            info = connectivity!!.activeNetworkInfo

            if (info != null) {
                if (info!!.state == NetworkInfo.State.CONNECTED) {
                    return false
                }
            } else {
                return true
            }
        }
        return true
    }}

