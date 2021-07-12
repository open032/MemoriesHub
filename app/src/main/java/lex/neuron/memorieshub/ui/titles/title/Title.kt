package lex.neuron.memorieshub.ui.titles.title

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.databinding.ListTitleBinding
import lex.neuron.memorieshub.permission.internet.TAG
import lex.neuron.memorieshub.ui.firebase.crud.title.TitleDelete
import lex.neuron.memorieshub.util.exhaustive


@AndroidEntryPoint
class Title : Fragment(R.layout.list_title), TitleAdapter.RenameItem, TitleAdapter.DeleteItem,
    TitleAdapter.TestingItem,
    TitleAdapter.OnLongItemClickListener, TitleAdapter.OnClickListener {

    private val viewModel: TitleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var activity: AppCompatActivity = getActivity() as AppCompatActivity

        var layoutPosition = false

        val binding = ListTitleBinding.bind(view)

//        activity.setSupportActionBar(binding.bottomAppBar)

        val listMainAdapter = TitleAdapter(this, this, this, this, this)

        binding.apply {

            mainListRv.apply {
                adapter = listMainAdapter
                layoutManager = LinearLayoutManager(requireContext())


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



                mainListRv.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        LinearLayoutManager.VERTICAL
                    )
                )

//                setHasFixedSize(true)
            }
/*
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
                    val sendLaterNet = sendLaterNet()
                    viewModel.onSwiped(title, sendLaterNet)
                }
            }).attachToRecyclerView(mainListRv)
*/

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

    override fun onLongItemClick(titleEntity: TitleEntity) {
//        viewModel.onLongTitleSelected(titleEntity)
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