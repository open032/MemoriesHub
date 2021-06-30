package lex.neuron.memorieshub.ui.titles.addedmemo

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
import lex.neuron.memorieshub.databinding.AddEditMemoBinding
import lex.neuron.memorieshub.util.exhaustive

@AndroidEntryPoint
class AddEditMemo : Fragment(R.layout.add_edit_memo) {
    private val viewModel: AddEditMemoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = AddEditMemoBinding.bind(view)

        binding.apply {
            etTitleMemo.setText(viewModel.title)
            etDescriptionMemo.setText(viewModel.desc)

            fabSaveMemo.setOnClickListener {
                val sendLaterNet = sendLaterNet()
                val titleFab = etTitleMemo.text.toString()
                viewModel.onSaveClick(titleFab, etDescriptionMemo.text.toString(), sendLaterNet)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditMemoEvent.collect { event ->
                when (event) {
                    AddEditMemoViewModel.AddEditMemoEvent.NavigationBack -> {
                        binding.etTitleMemo.clearFocus()
                        binding.etDescriptionMemo.clearFocus()
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
    }
}