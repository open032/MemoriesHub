package lex.neuron.memorieshub.ui.titles.memo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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

        var layoutPosition = false

        binding.apply {
            memoRv.apply {
                adapter = adapterMemo
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)



                layoutLG.setOnClickListener {
                    layoutPosition = !layoutPosition
                    if (layoutPosition) {
                        layoutLG.setImageResource(R.drawable.ic_baseline_grid_on_24)
                        layoutManager = GridLayoutManager(requireContext(), 2)
                    } else {
                        layoutLG.setImageResource(R.drawable.ic_liner_layout)
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }

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
                    val sendLaterNet = sendLaterNet()
                    viewModel.onSwipe(memo, sendLaterNet)
                }
            }).attachToRecyclerView(memoRv)

            fabAddMemo.setOnClickListener {
                viewModel.addNewMemo()
            }

            imageBack.setOnClickListener {
                viewModel.arrowBack()
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
                            MemoDirections.actionMemoFragToAddEditMemo(
                                event.id,
                                "",
                                "",
                                -1,
                                true
                            )
                        findNavController().navigate(action)
                    }
                    is MemoViewModel.MemoEvent.NavigateToEditScreen -> {
                        val action =
                            MemoDirections.actionMemoFragToAddEditMemo(
                                event.id, event.titleMemo, event.description, event.idMemo, event.testable
                            )
                        findNavController().navigate(action)
                    }
                    is MemoViewModel.MemoEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }

    override fun onLongItemClick(memoEntity: MemoEntity) {
        viewModel.longClick(memoEntity)
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