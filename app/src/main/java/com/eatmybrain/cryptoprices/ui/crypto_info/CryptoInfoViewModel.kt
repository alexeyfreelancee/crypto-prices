package com.eatmybrain.cryptoprices.ui.crypto_info

import androidx.lifecycle.*
import com.eatmybrain.cryptoprices.data.Repository
import com.eatmybrain.cryptoprices.data.enums.CandlesPeriod
import com.eatmybrain.cryptoprices.data.structures.CandlesData
import com.eatmybrain.cryptoprices.data.structures.CryptoFullInfo
import com.eatmybrain.cryptoprices.util.ResultOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class CryptoInfoViewModel @AssistedInject constructor(
    private val repository: Repository,
    @Assisted
    private val cryptoSymbol:String
) : ViewModel() {
    private val _isRefreshing = MutableLiveData(true)
    val isRefreshing :LiveData<Boolean> = _isRefreshing

    private val _cryptoInfo = MutableLiveData<ResultOf<CryptoFullInfo>>()
    val cryptoInfo: LiveData<ResultOf<CryptoFullInfo>> = _cryptoInfo

    private val _candlesData = MutableLiveData<ResultOf<CandlesData>>()
    val candlesData:LiveData<ResultOf<CandlesData>> = _candlesData

    init {
        viewModelScope.launch {
            _isRefreshing.postValue(true)
            loadCryptoInfo()
            loadCandlesData()
            _isRefreshing.postValue(false)
        }

    }


    private suspend fun loadCryptoInfo() = viewModelScope.launch{

        val response = withContext(Dispatchers.IO){
            repository.loadCryptoInfo(cryptoSymbol)
        }
        if(response.status.errorCode == 0){
            val cryptoInfo = response.data!![cryptoSymbol]!!.apply {
                imageUrl = "https://s2.coinmarketcap.com/static/img/coins/128x128/${this.id}.png"
            }
            val result = ResultOf.Success(cryptoInfo)
            _cryptoInfo.postValue(result)
        }else{
            val message = response.status.errorMessage!!
            val result = ResultOf.Failure(message)
            _cryptoInfo.postValue(result)
        }

    }

    suspend fun loadCandlesData(period: CandlesPeriod = CandlesPeriod.Today) = viewModelScope.launch {
        val response = withContext(Dispatchers.IO){
            val from = getFromValue(period) / 1000
            val to = System.currentTimeMillis() / 1000
            repository.loadCandlesData(cryptoSymbol, from, to)
        }

        if(response != null){
            _candlesData.postValue(ResultOf.Success(response))
        }else{
            _candlesData.postValue(ResultOf.Failure("Request error"))
        }
    }



    private fun getFromValue(period: CandlesPeriod):Long{
        return when(period){
            CandlesPeriod.Today -> System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
            CandlesPeriod.Month -> System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
            CandlesPeriod.Week -> System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
            CandlesPeriod.Year -> System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365)
        }
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