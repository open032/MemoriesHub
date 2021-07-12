package lex.neuron.memorieshub.ui.titles.testing

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.databinding.FindAPairBinding
import lex.neuron.memorieshub.permission.internet.TAG
import kotlin.random.Random

@AndroidEntryPoint
class Testing : Fragment(R.layout.find_a_pair), TestingAdapterLeft.OnClickListener,
    TestingAdapterRight.OnClickListenerRight {

    private val viewModel: TestingViewModel by viewModels()

    private var leftId: Int = -1
    private var rightId: Int = -2

    private var leftPosition = -3
    private var rightPosition = -3

    lateinit var myAdapterLeft: TestingAdapterLeft
    lateinit var myAdapterRight: TestingAdapterRight

    private var listLeftIt: MutableList<TestingList> = ArrayList()
    private var listRightIt: MutableList<TestingList> = ArrayList()

    private var listLeftRandom: MutableList<TestingList> = ArrayList()
    private var listRightRandom: MutableList<TestingList> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FindAPairBinding.bind(view)







        myAdapterLeft = TestingAdapterLeft(this)
        myAdapterRight = TestingAdapterRight(this)

        binding.apply {
            adapterLeft.apply {
                adapter = myAdapterLeft
                layoutManager = LinearLayoutManager(requireContext())
            }
            adapterRight.apply {
                adapter = myAdapterRight
                layoutManager = LinearLayoutManager(requireContext())
            }
            btn.setOnClickListener {
                myAdapterLeft.int = -1
                myAdapterLeft.notifyDataSetChanged()
            }
        }

        viewModel.getTeMemo.observe(viewLifecycleOwner) {
//            listLeftIt = viewModel.leftList(it)
//            listRightIt = viewModel.rightList(it)

            for (i in 0..it.size - 1) {
                if (it[i].testable) {
                    listLeftIt.add(TestingList(it[i].title, it[i].id))
                    listRightIt.add(TestingList(it[i].description, it[i].id))
                }
            }

            val randomListLeft = viewModel.randomList(listLeftIt.size)
            val randomListRight = viewModel.randomList(listLeftIt.size)

            Log.d(TAG, "listRandom -> -> -> -> ->  : ${randomListLeft}")
            Log.d(TAG, "listRandom -> -> -> -> ->  : ${randomListRight}")
//            listLeftRandom.add(TestingList(listLeftIt[random].name, listLeftIt[random].id))

            for (i in 0..listLeftIt.size - 1) {
                listLeftRandom.add(
                    TestingList(
                        listLeftIt[randomListLeft[i]].name,
                        listLeftIt[randomListLeft[i]].id
                    )
                )
            }

            for (i in 0..listLeftIt.size - 1) {
                listRightRandom.add(
                    TestingList(
                        listRightIt[randomListLeft[i]].name,
                        listRightIt[randomListLeft[i]].id
                    )
                )
            }


            myAdapterLeft.notifyDataSetChanged()
            Log.d(TAG, "listLeftIt === : $listLeftIt")
            Log.d(TAG, "listRightIt === : $listRightIt")
            myAdapterLeft.submitList(listLeftRandom)
            myAdapterRight.submitList(listRightIt)

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.testingEvent.collect { event ->
                when (event) {
                    is TestingViewModel.TestingEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onItemClickLeft(id: Int, position: Int) {
        leftId = id
        leftPosition = position
        Log.d(TAG, "onItemClickLeft: $position")
    }

    override fun onItemClickRight(id: Int, position: Int) {
        rightId = id
        var deleteItem = false
        deleteItem = viewModel.pair(leftId, rightId)
        rightPosition = position


        if (deleteItem) {
            listLeftRandom.removeAt(leftPosition)
            listRightIt.removeAt(rightPosition)
            myAdapterLeft.int = 1
            myAdapterLeft.notifyDataSetChanged()
            myAdapterRight.notifyDataSetChanged()

            viewModel.backStack(listRightIt)
        }

        Log.d(TAG, "onItemClickRight: $position")
    }
}