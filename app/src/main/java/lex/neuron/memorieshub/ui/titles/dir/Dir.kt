package lex.neuron.memorieshub.ui.titles.dir

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
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

//    val scope = CoroutineScope(Dispatchers.IO + CoroutineName("My Coroutine"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_dir, container, false)
        val binding = ListDirBinding.bind(view)
        val adapterDir = DirAdapter(this, this, this, this)


        /*val mainJob = scope.launch {
            val job1 = launch {
                while (true) {
                    yield()
                    Log.d("coroutineScope", "Job 1 Running...")
                }
            }
            val job2 = launch {
                Log.d("coroutineScope", "Job 2 Running...")
            }
            delay(1000L)
            Log.d("coroutineScope", "Canceling...")
            job2.cancelAndJoin()
            Log.d("coroutineScope", "Job 2 CANCELING")
        }

        runBlocking {
            delay(2000L)
            Log.d("coroutineScope", "Canceling...")
            mainJob.cancelAndJoin()
            Log.d("coroutineScope", "Main Job CANCELING")
        }*/

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
//                layoutManager = LinearLayoutManager(requireContext())
                layoutManager = LinearLayoutManager(requireContext())
//                dirRv.layoutManager = GridLayoutManager(requireContext(),2)


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



                registerForContextMenu(imageAuth)

                imageAuth.setOnClickListener {

                    val popup = PopupMenu(context, imageAuth)
                    //Inflating the Popup using xml file
                    popup.menuInflater.inflate(R.menu.menu_account, popup.menu)


                    popup.setOnMenuItemClickListener {
                        viewModel.authSignOut()
//                        Navigation.findNavController(view).navigate(R.id.action_account_to_signIn)
                        true
                    }

                    popup.show()//showing popup menu

                }


                val accountData = AccountData()
                val photoUrl = accountData.getImageAccount()
                Glide.with(this).load(photoUrl).circleCrop().into(imageAuth);

                setHasFixedSize(true)
            }

            /*ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
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
            }).attachToRecyclerView(dirRv)*/

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
                    is DirViewModel.DirEvent.NavigateToLogIn -> {
                        val action =
                            DirDirections.actionDirListToSignIn()
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

    override fun renameItem(dirEntity: DirEntity) {
        viewModel.renameItem(dirEntity)
    }


    override fun deleteItem(dirEntity: DirEntity) {
//        val dir = adapterDir.currentList[viewHolder.adapterPosition]
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