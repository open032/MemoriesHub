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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.databinding.ListMemoBinding
import lex.neuron.memorieshub.util.exhaustive

@AndroidEntryPoint
class Memo : Fragment(R.layout.list_memo),
    MemoAdapter.RenameItem, MemoAdapter.DeleteItem {
    private val viewModel: MemoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ListMemoBinding.bind(view)
        val adapterMemo = MemoAdapter(this, this)

        var layoutPosition = false

        binding.apply {
            viewModel.checkMemo()

            address.text = viewModel.titleName

            memoRv.apply {
                adapter = adapterMemo
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)



                layoutLG.setOnClickListener {
                    layoutPosition = !layoutPosition
                    if (layoutPosition) {
                        layoutLG.setImageResource(R.drawable.ic_liner_layout)
                        layoutManager = GridLayoutManager(requireContext(), 2)
                    } else {
                        layoutLG.setImageResource(R.drawable.ic_grid_layout)
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }

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
                                event.id,
                                event.titleMemo,
                                event.description,
                                event.idMemo,
                                event.testable
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

    override fun renameItem(memoEntity: MemoEntity) {
        viewModel.longClick(memoEntity)
    }

    override fun deleteItem(memoEntity: MemoEntity) {
        val sendLaterNet = sendLaterNet()
        viewModel.onSwipe(memoEntity, sendLaterNet)
    }
}