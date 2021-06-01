package lex.neuron.memorieshub.ui.title.title

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.databinding.TitleAdapterBinding
import lex.neuron.memorieshub.util.exhaustive
import com.google.android.material.bottomappbar.BottomAppBar


@AndroidEntryPoint
class Title : Fragment(R.layout.title_adapter),
    TitleAdapter.OnLongItemClickListener, TitleAdapter.OnClickListener {

    private val viewModel: TitleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity: AppCompatActivity = getActivity() as AppCompatActivity

        val binding = TitleAdapterBinding.bind(view)

        activity.setSupportActionBar(binding.bottomAppBar)

        val listMainAdapter = TitleAdapter(this, this)

        binding.apply {

            mainListRv.apply {
                adapter = listMainAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val title = listMainAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onSwiped(title)
                }
            }).attachToRecyclerView(mainListRv)

            fabAddTask.setOnClickListener {
                viewModel.onAddNewTitleClick()
            }
        }

        viewModel.title.observe(viewLifecycleOwner) {
            listMainAdapter.submitList(it)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.titleEvent.collect { event ->
                when (event) {
                    is TitleViewModel.TitleEvent.NavigateToAddScreen -> {
                        val action =
                            TitleDirections.actionListMainFragToAddEditTitleFrag(
                                -1,
                                ""
                            )
                        findNavController().navigate(action)
                    }
                    is TitleViewModel.TitleEvent.NavigateToAnotherList -> {
                        val action =
                            TitleDirections.actionListMainFragToMemoFrag(event.id)
                        findNavController().navigate(action)
                    }

                    is TitleViewModel.TitleEvent.NavigateToEditTitleScreen -> {
                        val action =
                            TitleDirections.actionListMainFragToAddEditTitleFrag(
                                event.id,
                                event.name
                            )
                        findNavController().navigate(action)
                    }
                    is TitleViewModel.TitleEvent.NavigateToBottomSheet -> {
                        val action =
                            TitleDirections.actionListMainFragToBottomTest()
                        findNavController().navigate(action);
                    }
                }.exhaustive
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                viewModel.onMenuClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(titleEntity: TitleEntity) {
        viewModel.onTitleSelected(titleEntity)
    }

    override fun onLongItemClick(titleEntity: TitleEntity) {
        viewModel.onLongTitleSelected(titleEntity)
    }
}