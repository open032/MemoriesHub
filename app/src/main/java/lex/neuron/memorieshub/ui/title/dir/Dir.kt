package lex.neuron.memorieshub.ui.title.dir

import android.content.ContentValues.TAG
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.databinding.ListDirBinding
import lex.neuron.memorieshub.util.exhaustive
import java.util.*

@AndroidEntryPoint
class Dir : Fragment(R.layout.list_dir),
    DirAdapter.OnClickListener, DirAdapter.OnLongItemClickListener {
    private val viewModel: DirViewModel by viewModels()
    var touchHelper : ItemTouchHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_dir, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterDir = DirAdapter(this, this)
        val binding = ListDirBinding.bind(view)

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
                    viewModel.onSwiped(dir)
                }
            }).attachToRecyclerView(dirRv)

            fab.setOnClickListener {
                viewModel.addNewItem()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
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

        viewModel.dir.observe(viewLifecycleOwner) {
            adapterDir.submitList(it)
            binding.dirRv.adapter = adapterDir
        }
    }


    override fun onItemClick(dirEntity: DirEntity) {
        viewModel.onClick(dirEntity)
    }

    override fun onLongItemClick(dirEntity: DirEntity) {
        viewModel.onLongClick(dirEntity)
    }
}