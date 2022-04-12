package com.eatmybrain.cryptoprices.ui.crypto_info

import androidx.lifecycle.*
import com.eatmybrain.cryptoprices.data.Repository
import com.eatmybrain.cryptoprices.data.structures.CryptoFullInfo
import com.eatmybrain.cryptoprices.util.ResultOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CryptoInfoViewModel @AssistedInject constructor(
    private val repository: Repository,
    @Assisted
    private val cryptoSymbol:String
) : ViewModel() {
    private val _isRefreshing = MutableLiveData(true)
    val isRefreshing :LiveData<Boolean> = _isRefreshing

    private val _cryptoInfo = MutableLiveData<ResultOf<CryptoFullInfo>>()
    val cryptoInfo: LiveData<ResultOf<CryptoFullInfo>> = _cryptoInfo

    init {
        loadCryptoFullInfo()
    }


    private fun loadCryptoFullInfo() = viewModelScope.launch{
        _isRefreshing.postValue(true)
        val response = withContext(Dispatchers.IO){
            repository.loadCryptoInfo(cryptoSymbol)
        }
        if(response.status.errorCode == 0){
            val cryptoInfo = response.data!![cryptoSymbol]!!
            loadImageUrl(cryptoInfo)
            val result = ResultOf.Success(cryptoInfo)
            _cryptoInfo.postValue(result)
        }else{
            val message = response.status.errorMessage!!
            val result = ResultOf.Failure(message)
            _cryptoInfo.postValue(result)
        }
        _isRefreshing.postValue(false)
    }

    private fun loadImageUrl(info:CryptoFullInfo){
        info.imageUrl = "https://s2.coinmarketcap.com/static/img/coins/128x128/${info.id}.png"
    }

    @AssistedFactory
    interface Factory {
        fun create(cryptoSymbol: String) : CryptoInfoViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory:Factory,
            cryptoSymbol:String
        ) : ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(cryptoSymbol) as T
            }
        }
    }
}