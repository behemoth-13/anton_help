package selitskiyapp.hometasks.financialassistant.presentation.view.bottoms

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import selitskiyapp.hometasks.financialassistant.R
import selitskiyapp.hometasks.financialassistant.databinding.BottomAddOperationBinding
import selitskiyapp.hometasks.financialassistant.domain.models.MoneyHolder
import selitskiyapp.hometasks.financialassistant.domain.models.Operation
import selitskiyapp.hometasks.financialassistant.presentation.recyclers.moneyholder.MoneyHolderAdapter
import selitskiyapp.hometasks.financialassistant.presentation.recyclers.moneyholder.MoneyHolderArrayAdapter
import selitskiyapp.hometasks.financialassistant.presentation.recyclers.moneyholder.MoneyHolderOnItemListener
import selitskiyapp.hometasks.financialassistant.presentation.viewModels.EditMoneyHolderViewModel
import selitskiyapp.hometasks.financialassistant.presentation.viewModels.OperationsFragmentViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.sql.StatementEvent

@AndroidEntryPoint
class AddOperationBottom : BottomSheetDialogFragment() {

    private lateinit var binding: BottomAddOperationBinding
    private val operationsViewModel: OperationsFragmentViewModel by viewModels()
    private val moneyHoldersViewModel: EditMoneyHolderViewModel by viewModels()

    private var category: String = "0"
    private var categoryImageId: Int = 0
    private var moneyHolderId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomAddOperationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActvAddOperationAdapter()

        initCategory()

        initSaveButton()

        }

    private fun initSaveButton() = with(binding) {
            buttonOperationSave.setOnClickListener {
                // todo validation
                val operation = Operation(
                    category = category,
                    moneyHolderId = moneyHolderId!!,
                    value = addValue.editText?.text.toString().toLong(),
                    categoryImageId = categoryImageId,
                    date = getDate(),
                    comment = addComments.editText?.text.toString()
                )

                Log.d("addOperation complete: ", operation.toString())

                operationsViewModel.addOperation(operation)

                dismiss()
            }
    }

    private fun initCategory() = with(binding) {
        chipGroup.setOnCheckedChangeListener { group, checkedId ->

            Toast.makeText(requireContext(), "che", Toast.LENGTH_SHORT).show()

            when (checkedId) {
                chip1.id -> {
                    category = "?????????????? ???? ????????????"
                    categoryImageId = 1
                }
                chip2.id -> {
                    category = "?????????????? ???? ????????????????"
                    categoryImageId = 2
                }
                //                chip3.id -> chip3.chipIcon.toString()
                //                chip4.id -> chip4.chipIcon.toString()
                //                chip5.id -> chip2.chipIcon.toString()
                //                chip6.id -> chip2.chipIcon.toString()
                //                chip7.id -> chip2.chipIcon.toString()

                //                chip2.id -> chip2.chipIcon.toString()
                else -> "???? ???????????? ??????????????"
            }
        }
    }


    private fun getDate(): Long {
//        showDatePicker()
        return 11
    }

//    private fun showDatePicker() {
//        val selectedDateInMillis = currentSelectedDate ?: System.currentTimeMillis()
//
//        val dataPicker =
//            MaterialDatePicker.Builder.datePicker()
//                .setTitleText("???????????????? ????????")
//                .setSelection(selectedDateInMillis)
//                .build()
//                .apply {
//                    addOnPositiveButtonClickListener { dateInMillis ->
//                        onDateSelected(
//                            dateInMillis
//                        )
//                    }
//                }
//
//        dataPicker.show(childFragmentManager, TAG)
//    }
//
//    private fun onDateSelected(date: Long?) {
//        currentSelectedDate = date
//
//        val dateTime: LocalDateTime = LocalDateTime.ofInstant(
//            currentSelectedDate?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault()
//        )
//        val dateAsFormattedText: String? =
//            dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
//        binding.editDate.setText(dateAsFormattedText)
//    }
//
//    private val onEditorActionListener: TextView.OnEditorActionListener =
//        TextView.OnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//                dismiss()
//                return@OnEditorActionListener true
//            }
//            false
//        }

    private fun initActvAddOperationAdapter() {
         lifecycleScope.launchWhenResumed {
            moneyHoldersViewModel.moneyHoldersListFlow.collect { list ->
                val adapter = MoneyHolderArrayAdapter(requireContext(), list)
                binding.actvAddOperation.setAdapter(adapter)
                binding.actvAddOperation.setOnItemClickListener { _, _, position, _ ->
                        moneyHolderId = adapter.getItem(position)?.id
                    }
            }
        }
    }
}


