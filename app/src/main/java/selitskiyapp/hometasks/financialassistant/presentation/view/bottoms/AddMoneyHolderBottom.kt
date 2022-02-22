package selitskiyapp.hometasks.financialassistant.presentation.view.bottoms

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import selitskiyapp.hometasks.financialassistant.R
import selitskiyapp.hometasks.financialassistant.databinding.BottomAddMoneyHolderBinding
import selitskiyapp.hometasks.financialassistant.domain.models.MoneyHolder
import selitskiyapp.hometasks.financialassistant.presentation.utils.AmountInputFilter
import selitskiyapp.hometasks.financialassistant.presentation.viewModels.EditMoneyHolderViewModel

@AndroidEntryPoint
class AddMoneyHolderBottom : BottomSheetDialogFragment() {

    private val viewModel: EditMoneyHolderViewModel by viewModels()
    private lateinit var binding: BottomAddMoneyHolderBinding
    private var type: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomAddMoneyHolderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id: Int = requireArguments().getInt(EditMoneyHolderBottom.MONEY_HOLDER_ID_FROM_EDIT)
        initTypeAdapter()
        initTextFields()
        initSaveButton(id)
        initFields(id)
    }

    private fun initFields(id: Int?) {
        if (id != null && id !== 0) {
            viewModel.getMoneyHolderById(id)
            lifecycleScope.launchWhenResumed {
                viewModel.moneyHolder.collect { moneyHolder ->
                    binding.run {
                        tilName.editText?.setText(moneyHolder.name)
                        //TODO напиши показ типа
                        tilBalance.editText?.setText(moneyHolder.balance.toString())
                    }
                }
            }
        }
    }

    private fun initSaveButton(id: Int?) = with(binding) {
        button.setOnClickListener {
            when {
                tilName.editText?.text.isNullOrEmpty() -> tilName.error = "Вы не ввели значение"
                tilType.editText?.text.isNullOrEmpty() -> tilType.error = "Вы не выбрали тип"
                tilBalance.editText?.text.isNullOrEmpty() -> tilBalance.error =
                    "Вы не ввели текущий баланс"
                else -> {
                    if (id != null && id !== 0) updateMoneyHolder(id) else addMoneyHolder()
                    dismiss()
                    findNavController().navigate(R.id.addMoneyHolderBottom_to_moneyHolderFragment)
                }
            }
        }
    }

    private fun updateMoneyHolder(id: Int) = with(binding) {
        val toUpdate =
            MoneyHolder(
                id = id,
                name = tilName.editText?.text.toString(),
                type = type,
                balance = tilBalance.editText?.text.toString().toLong()
            )
        viewModel.updateMoneyHolder(toUpdate)
    }

    private fun addMoneyHolder() = with(binding) {
        viewModel.addMoneyHolder(
            MoneyHolder(
                name = tilName.editText?.text.toString(),
                type = type,
                balance = tilBalance.editText?.text.toString().toFloat().let { (it * 100).toLong() }
            )
        )
    }

    private fun initTextFields() = with(binding) {
        tilName.editText?.doAfterTextChanged { tilName.error = null }
        tilType.editText?.doAfterTextChanged { tilType.error = null }
        tilBalance.editText?.doAfterTextChanged { tilBalance.error = null }
        tilBalance.editText?.filters = arrayOf(AmountInputFilter())
    }

    private fun initTypeAdapter() = with(binding) {
        actvType.setAdapter(operationStatusAdapter)
        actvType.setOnItemClickListener { _, _, position, _ ->
            type = when (operationStatusAdapter.getItem(position)) {
                getString(R.string.typeCash) -> CASH
                getString(R.string.typeNonCash) -> NON_CASH
                else -> 0
            }
        }
    }

    private val operationStatusAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.item_type_menu_money_holder,
            listOf(
                getString(R.string.typeCash),
                getString(R.string.typeNonCash)
            )
        )
    }

    companion object {
        const val CASH = 1
        const val NON_CASH = 2
    }
}
