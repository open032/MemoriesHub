package lex.neuron.memorieshub.ui.title.addedtitle

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
            editTextTitleCardName.setText(viewModel.titleName)

            fabSaveTitleCard.setOnClickListener {
                var name = editTextTitleCardName.text.toString()
                viewModel.onSaveClick(name)
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTitleEvent.collect { event ->
                when (event) {
                    AddTitleViewModel.AddEditTitleEvent.NavigateBack -> {
                        binding.editTextTitleCardName.clearFocus()
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}

