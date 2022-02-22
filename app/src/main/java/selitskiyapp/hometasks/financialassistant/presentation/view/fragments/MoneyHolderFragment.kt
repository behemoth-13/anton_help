package selitskiyapp.hometasks.financialassistant.presentation.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import selitskiyapp.hometasks.financialassistant.R
import selitskiyapp.hometasks.financialassistant.databinding.FragmentMoneyHolderBinding
import selitskiyapp.hometasks.financialassistant.presentation.recyclers.moneyholder.MoneyHolderAdapter
import selitskiyapp.hometasks.financialassistant.presentation.recyclers.moneyholder.MoneyHolderOnItemListener
import selitskiyapp.hometasks.financialassistant.presentation.viewModels.EditMoneyHolderViewModel

@AndroidEntryPoint
class MoneyHolderFragment : Fragment(R.layout.fragment_money_holder) {
    private val binding: FragmentMoneyHolderBinding by viewBinding()
    private val adapter by lazy { MoneyHolderAdapter(itemClickListenerMoneyHolder) }
    private val viewModel: EditMoneyHolderViewModel by viewModels()

    private val itemClickListenerMoneyHolder: MoneyHolderOnItemListener =
        object : MoneyHolderOnItemListener {
            override fun onItemClickListener(id: Int) {

                findNavController().navigate(
                    R.id.moneyHolderFragment_to_editMoneyHolderBottom,
                    bundleOf(MONEY_HOLDER_ID_FROM_HOLDER to id)
                )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAddButton()
    }

    override fun onResume() {
        super.onResume()

        initObservers()

        initRecycler()
    }

    private fun initObservers() {

        lifecycleScope.launchWhenResumed {
            viewModel.moneyHoldersListFlow.collect {
                adapter.submitList(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.summ.collect {
                Log.d("sasdfas", "summ = ${it / 100f}")
                Toast.makeText(requireContext(), "summ = ${it / 100f}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecycler() = with(binding) {
        recyclerMoneyHolder.adapter = adapter
        recyclerMoneyHolder.layoutManager = LinearLayoutManager(context)

    }

    private fun initAddButton() = with(binding) {
        fab.setOnClickListener {
            val action =
                MoneyHolderFragmentDirections.moneyHolderFragmentToAddMoneyHolderBottom()
            findNavController().navigate(action)

        }
    }
    companion object {
        const val MONEY_HOLDER_ID_FROM_HOLDER = "HOLDER"
    }
}