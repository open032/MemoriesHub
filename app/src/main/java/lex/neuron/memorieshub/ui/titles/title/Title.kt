package lex.neuron.memorieshub.ui.titles.title

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.databinding.ListTitleBinding
import lex.neuron.memorieshub.util.exhaustive


@AndroidEntryPoint
class Title : Fragment(R.layout.list_title), TitleAdapter.RenameItem, TitleAdapter.DeleteItem,
    TitleAdapter.TestingItem, TitleAdapter.OnClickListener {

    private val viewModel: TitleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var layoutPosition = false

        val binding = ListTitleBinding.bind(view)

        val listMainAdapter = TitleAdapter(this, this, this, this)

        binding.apply {
            viewModel.checkTitle()
            viewModel.checkMemo()

            address.text = viewModel.dirName

            mainListRv.apply {
                adapter = listMainAdapter
                layoutManager = LinearLayoutManager(requireContext())

                layoutLG.setOnClickListener {
                    layoutPosition = !layoutPosition
                    if (layoutPosition) {
                        layoutLG.setImageResource(R.drawable.ic_liner_layout)
                        layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)                    } else {
                        layoutLG.setImageResource(R.drawable.ic_grid_layout)
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }

                mainListRv.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        LinearLayoutManager.VERTICAL
                    )
                )
            }

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
                            TitleDirections.actionListMainFragToMemoFrag(event.id, event.name)
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
                        findNavController().navigate(action)
                    }
                    is TitleViewModel.TitleEvent.NavigateToTesting -> {
                        val action =
                            TitleDirections.actionListMainFragToTesting2(event.id)
                        findNavController().navigate(action)
                    }
                    is TitleViewModel.TitleEvent.NavigateBack -> {
                        val action =
                            TitleDirections.actionListMainFragToBottomTest()
                        findNavController().navigate(action)
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

    override fun onItemClick(titleEntity: TitleEntity) {
        viewModel.onTitleSelected(titleEntity)
    }

    override fun renameItem(titleEntity: TitleEntity) {
        viewModel.renameItem(titleEntity)
    }

    override fun deleteItem(titleEntity: TitleEntity) {
        viewModel.memoList(titleEntity)
        val sendLaterNet = sendLaterNet()
        viewModel.deleteItem(titleEntity, sendLaterNet)
    }

    override fun testingItem(titleEntity: TitleEntity) {
        viewModel.testingItem(titleEntity)
    }
}