package selitskiyapp.hometasks.financialassistant.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import selitskiyapp.hometasks.financialassistant.data.storage.MoneyHolderDao
import selitskiyapp.hometasks.financialassistant.domain.models.MoneyHolder
import selitskiyapp.hometasks.financialassistant.domain.repository.MoneyHoldersRepository
import javax.inject.Inject

@HiltViewModel
class EditMoneyHolderViewModel @Inject constructor(
    private val dao: MoneyHolderDao,
    private val moneyHoldersRepository: MoneyHoldersRepository,
) : ViewModel() {
    private val _moneyHoldersListFlow = MutableStateFlow<List<MoneyHolder>>(emptyList())
    val moneyHoldersListFlow: StateFlow<List<MoneyHolder>> get() = _moneyHoldersListFlow

    val summ = dao.getBalance()

    private val _moneyHolder = MutableStateFlow(MoneyHolder(0, "0",0,0))
    val moneyHolder: StateFlow<MoneyHolder> get() = _moneyHolder

    init {
        getAllMoneyHolders()
    }

    private fun getAllMoneyHolders() {
        viewModelScope.launch {
            _moneyHoldersListFlow.value = moneyHoldersRepository.getMoneyHolders()
        }
    }

    fun getMoneyHolderById(id: Int) {
        viewModelScope.launch {
            _moneyHolder.value =  moneyHoldersRepository.getMoneyHolderById(id)
        }
    }

    fun addMoneyHolder(moneyHolder: MoneyHolder) {
        viewModelScope.launch {
            moneyHoldersRepository.addMoneyHolder(moneyHolder)
        }
    }

    fun updateMoneyHolder(moneyHolder: MoneyHolder) {
        viewModelScope.launch {
            moneyHoldersRepository.updateMoneyHolder(moneyHolder)
        }
    }

    fun deleteMoneyHolder(id: Int) {
        viewModelScope.launch {
            moneyHoldersRepository.deleteMoneyHolder(id)
        }
    }
}