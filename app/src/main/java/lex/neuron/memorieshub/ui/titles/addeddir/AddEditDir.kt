package lex.neuron.memorieshub.ui.titles.addeddir

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.databinding.AddEditDirBinding
import lex.neuron.memorieshub.permission.internet.TAG
import lex.neuron.memorieshub.util.exhaustive


@AndroidEntryPoint
class AddEditDir : Fragment(R.layout.add_edit_dir) {
    private val viewModel: AddEditDirViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = AddEditDirBinding.bind(view)

        binding.apply {
            et.setText(viewModel.dirName)
            
            showLog.setOnClickListener {
                viewModel.showLog()
            }

            fab.setOnClickListener {
                val sendLaterNet = sendLaterNet()
                Log.e(TAG, "onViewCreated: $sendLaterNet")

                val name = et.text.toString()
                viewModel.onSaveClick(name, sendLaterNet)
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditEvent.collect { event ->
                when (event) {
                    AddEditDirViewModel.AddEditEvent.NavigateBack -> {
                        binding.et.clearFocus()
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