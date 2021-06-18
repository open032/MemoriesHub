package lex.neuron.memorieshub.ui.titles.title

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.databinding.ListTitleBinding
import lex.neuron.memorieshub.util.exhaustive


@AndroidEntryPoint
class Title : Fragment(R.layout.list_title),
    TitleAdapter.OnLongItemClickListener, TitleAdapter.OnClickListener {

    private val viewModel: TitleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity: AppCompatActivity = getActivity() as AppCompatActivity

        val binding = ListTitleBinding.bind(view)

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
                    viewModel.memoList(title)
                    viewModel.onSwiped(title)
                }
            }).attachToRecyclerView(mainListRv)

            fabAddTask.setOnClickListener {
                viewModel.onAddNewTitleClick()
            }

            imageBack.setOnClickListener {
                viewModel.arrowBack()
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
                                event.id,
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
                    is TitleViewModel.TitleEvent.NavigateBack -> {
                        val action =
                            TitleDirections.actionListMainFragToBottomTest()
                            findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_title_entity, menu)

        val searchItem = menu.findItem(R.id.more_vert)
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.more_vert -> {
                viewModel.moreVert()
            }
           *//* android.R.id.home -> {
                viewModel.onMenuClick()
            }*//*
        }
        return super.onOptionsItemSelected(item)
    }*/

    override fun onItemClick(titleEntity: TitleEntity) {
        viewModel.onTitleSelected(titleEntity)
    }

    override fun onLongItemClick(titleEntity: TitleEntity) {
        viewModel.onLongTitleSelected(titleEntity)
    }
}