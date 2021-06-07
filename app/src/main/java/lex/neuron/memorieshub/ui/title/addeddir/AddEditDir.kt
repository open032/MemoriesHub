package lex.neuron.memorieshub.ui.title.addeddir

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.databinding.AddEditDirBinding
import lex.neuron.memorieshub.databinding.AddEditTitleBinding
import lex.neuron.memorieshub.util.exhaustive

@AndroidEntryPoint
class AddEditDir : Fragment(R.layout.add_edit_dir) {
    private val viewModel: AddEditDirViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = AddEditDirBinding.bind(view)

        binding.apply {
            et.setText(viewModel.dirName)

            fab.setOnClickListener {
                var name = et.text.toString()
                viewModel.onSaveClick(name)
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
}