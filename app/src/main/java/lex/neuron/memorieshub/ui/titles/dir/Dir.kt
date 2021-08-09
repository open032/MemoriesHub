package lex.neuron.memorieshub.ui.titles.dir

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.databinding.ListDirBinding
import lex.neuron.memorieshub.ui.firebase.crud.read.AccountData
import lex.neuron.memorieshub.util.exhaustive


@AndroidEntryPoint
class Dir : Fragment(R.layout.list_dir), DirAdapter.RenameItem, DirAdapter.DeleteItem,
    DirAdapter.OnClickListener, DirAdapter.OnLongItemClickListener {

    private val viewModel: DirViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_dir, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var layoutPosition = false

        val binding = ListDirBinding.bind(view)
        val adapterDir = DirAdapter(this, this, this, this)


        binding.apply {


            dirRv.apply {
                adapter = adapterDir
                layoutManager = LinearLayoutManager(requireContext())


                layoutLG.setOnClickListener {
                    layoutPosition = !layoutPosition
                    if (layoutPosition) {
                        layoutLG.setImageResource(R.drawable.ic_liner_layout)
                        layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    } else {
                        layoutLG.setImageResource(R.drawable.ic_grid_layout)
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }

                registerForContextMenu(imageAuth)

                imageAuth.setOnClickListener {

                    val popup = PopupMenu(context, imageAuth)
                    popup.menuInflater.inflate(R.menu.menu_account, popup.menu)

                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.log_out -> {
                                viewModel.authSignOut()
                                Log.d(TAG, "login out: ")
                            }
                            R.id.dir -> {
                                viewModel.deleteAllRoom(sendLaterNet())
                                viewModel.createDir()
                                viewModel.createTitleFromFirebase()
                                viewModel.createMemoFromFirebase()
                            }
                        }
                        true
                    }
                    true
                    popup.show()
                }

                val accountData = AccountData()
                val photoUrl = accountData.getImageAccount()
                Glide.with(this).load(photoUrl).circleCrop().into(imageAuth);

                setHasFixedSize(true)
            }

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
                        val action = DirDirections.actionBottomTestToListMainFrag2(event.id, event.name)
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
                    is DirViewModel.DirEvent.NavigateToLogIn -> {
                        val action =
                            DirDirections.actionDirListToSignIn()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.showList()
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

    override fun renameItem(dirEntity: DirEntity) {
        viewModel.renameItem(dirEntity)
    }


    override fun deleteItem(dirEntity: DirEntity) {
        viewModel.titleList(dirEntity)
        val sendLaterNet = sendLaterNet()
        viewModel.deleteItem(dirEntity, sendLaterNet)
    }

    override fun onItemClick(dirEntity: DirEntity) {
        viewModel.onClick(dirEntity)
    }


    override fun onLongItemClick(dirEntity: DirEntity) {
    }
}