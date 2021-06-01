package lex.neuron.memorieshub.ui.title.memo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
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
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.databinding.ListMemoBinding
import lex.neuron.memorieshub.util.exhaustive

@AndroidEntryPoint
class Memo : Fragment(R.layout.list_memo),
    MemoAdapter.OnLongClickListener {
    private val viewModel: MemoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ListMemoBinding.bind(view)
        val adapterMemo = MemoAdapter(this)

        binding.apply {
            memoRv.apply {
                adapter = adapterMemo
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
                    val memo = adapterMemo.currentList[viewHolder.adapterPosition]
                    viewModel.onSwipe(memo)
                }
            }).attachToRecyclerView(memoRv)

            fabAddMemo.setOnClickListener {
                viewModel.addNewMemo()
            }
        }

        viewModel.memo.observe(viewLifecycleOwner) {
            adapterMemo.submitList(it)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.memoEvent.collect { event ->
                when (event) {
                    is MemoViewModel.MemoEvent.NavigateToAddScreen -> {
                        val action =
                            MemoDirections.actionMemoFragToAddEditMemo(event.id, "", "", -1)
                        findNavController().navigate(action)
                    }
                    is MemoViewModel.MemoEvent.NavigateToEditScreen -> {
                        val action =
                            MemoDirections.actionMemoFragToAddEditMemo(
                                event.id, event.titleMemo, event.description, event.idMemo
                            )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
    }

    override fun onLongItemClick(memoEntity: MemoEntity) {
        viewModel.longClick(memoEntity)
    }
}