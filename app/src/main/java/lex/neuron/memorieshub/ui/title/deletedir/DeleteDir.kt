package lex.neuron.memorieshub.ui.title.deletedir

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.databinding.DeleteDirListBinding
import lex.neuron.memorieshub.databinding.ListDirBinding
import java.util.*

@AndroidEntryPoint
class DeleteDir : BottomSheetDialogFragment(), DeleteDirAdapter.OnClickListener {
    private val viewModel: DeleteDirViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.delete_dir_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mList: MutableList<DeleteDirItem> = LinkedList()
        mList.add(DeleteDirItem("Delete Dir"))

        val adapterDelete = DeleteDirAdapter(mList, this)
        val binding = DeleteDirListBinding.bind(view)

        binding.apply {
            rv.apply {
                adapter = adapterDelete
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }

    override fun onItemClick() {
        viewModel.deleteDir()
    }


}