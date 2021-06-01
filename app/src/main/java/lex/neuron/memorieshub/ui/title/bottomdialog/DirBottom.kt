package lex.neuron.memorieshub.ui.title.bottomdialog

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.databinding.DeleteMeBinding

@AndroidEntryPoint
class DirBottom : BottomSheetDialogFragment(),
TestDirAdapter.OnClickListener, TestDirAdapter.OnLongItemClickListener{
    private val viewModel: BottomTestViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.delete_me, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterMemo = TestDirAdapter(this, this)
        val binding = DeleteMeBinding.bind(view)

        binding.apply {
            memoRv.apply {

                adapter = adapterMemo
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            fabAddMemo.setOnClickListener {

            }
        }

        viewModel.memo.observe(viewLifecycleOwner) {
            adapterMemo.submitList(it)
            binding.memoRv.adapter = adapterMemo
        }
    }

    override fun onItemClick(dirEntity: DirEntity) {
        Log.e(TAG, "onItemClick: bottomTest ${dirEntity.name}" )
    }

    override fun onLongItemClick(dirEntity: DirEntity) {
        TODO("Not yet implemented")
    }
}