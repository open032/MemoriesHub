package lex.neuron.memorieshub.ui.titles.dir

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.databinding.ListDirBinding
import lex.neuron.memorieshub.permission.internet.NetworkManager
import lex.neuron.memorieshub.util.exhaustive

@AndroidEntryPoint
class Dir : Fragment(R.layout.list_dir),
    DirAdapter.OnClickListener, DirAdapter.OnLongItemClickListener {

    private val viewModel: DirViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_dir, container, false)
        val binding = ListDirBinding.bind(view)
        val adapterDir = DirAdapter(this, this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = ListDirBinding.bind(view)
        val adapterDir = DirAdapter(this, this)


        binding.apply {

            dirRv.apply {
                adapter = adapterDir
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
                    val dir = adapterDir.currentList[viewHolder.adapterPosition]
                    viewModel.titleList(dir)
                    val sendLaterNet = sendLaterNet()
                    viewModel.onSwiped(dir, sendLaterNet)
                }
            }).attachToRecyclerView(dirRv)

            fab.setOnClickListener {
                viewModel.addNewItem()
            }

            viewModel.dir.observe(viewLifecycleOwner) {
                adapterDir.submitList(it)
                binding.dirRv.adapter = adapterDir
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dirEvent.collect { event ->
                when (event) {
                    is DirViewModel.DirEvent.NavigateToTitleList -> {
                        val action = DirDirections.actionBottomTestToListMainFrag2(event.id)
                        findNavController().navigate(action)
                    }
                    is DirViewModel.DirEvent.NavigateToEditTitleDir -> {
                        val action =
                            DirDirections.actionBottomTestToAddEditDir(event.id, event.name)
                        findNavController().navigate(action)
                    }
                    is DirViewModel.DirEvent.NavigateToAddDir -> {
                        val action =
                            DirDirections.actionBottomTestToAddEditDir(-1, "")
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

    override fun onItemClick(dirEntity: DirEntity) {
        viewModel.onClick(dirEntity)
    }


    override fun onLongItemClick(dirEntity: DirEntity) {
        viewModel.onLongClick(dirEntity)
    }
}