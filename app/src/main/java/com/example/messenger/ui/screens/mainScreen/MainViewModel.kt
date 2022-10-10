package com.example.messenger.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import com.example.messenger.data.firebase.mainList.dataStore.MainListDataStore
import com.example.messenger.data.firebase.mainList.model.MainListItem
import com.example.messenger.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mailListDataStore: MainListDataStore
):ViewModel() {

    private val _responseMailList = MutableStateFlow<Result<List<MainListItem>>>(Result.Loading())
    val responseMainList = _responseMailList.asStateFlow()

    fun getMailList(){
        try {
            mailListDataStore.getMainList({
                _responseMailList.value = Result.Success(it)
            },{
                _responseMailList.value = Result.Error(it.message.toString())
            })
        }catch (e:Exception){
            _responseMailList.value = Result.Error(e.message.toString())
        }
    }
}