package com.eatmybrain.cryptoprices.ui.crypto_info

import androidx.lifecycle.*
import com.eatmybrain.cryptoprices.data.Repository
import com.eatmybrain.cryptoprices.data.enums.PricePeriod
import com.eatmybrain.cryptoprices.data.structures.CryptoFullInfo
import com.eatmybrain.cryptoprices.util.ChartData
import com.eatmybrain.cryptoprices.util.ResultOf
import com.eatmybrain.cryptoprices.util.Time
import com.github.mikephil.charting.data.LineDataSet
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CryptoInfoViewModel @AssistedInject constructor(
    private val repository: Repository,
    @Assisted
    private val cryptoSymbol: String
) : ViewModel() {
    private val _isRefreshing = MutableLiveData(true)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _cryptoInfo = MutableLiveData<ResultOf<CryptoFullInfo>>()
    val cryptoInfo: LiveData<ResultOf<CryptoFullInfo>> = _cryptoInfo


    private val _lineChartData = MutableLiveData<ResultOf<LineDataSet>>()
    val chartData: LiveData<ResultOf<LineDataSet>> = _lineChartData

    init {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadCryptoInfo()
            loadChartData()
            _isRefreshing.value = false
        }

    }

    private suspend fun loadCryptoInfo() {
        val response = withContext(Dispatchers.IO) {
            repository.cryptoInfo(cryptoSymbol)
        }

        _cryptoInfo.value = if (response.status.errorCode == 0) {
            val cryptoInfo = response.data!![cryptoSymbol]!!
            ResultOf.Success(cryptoInfo)
        } else {
            val message = response.status.errorMessage!!
            ResultOf.Failure(message)
        }
    }

    private suspend fun loadChartData(period: PricePeriod = PricePeriod.Today) {
        val ohlcData = withContext(Dispatchers.IO) {
            repository.ohlcData(
                symbol = cryptoSymbol,
                from = Time.fromPeriod(period),
                to = Time.currentTime(),
                resolution = ChartData.resolution(period)
            )
        }

        _lineChartData.value = if (ohlcData != null && ohlcData.status == "ok") {
            val data = ChartData.parse(ohlcData)
            ResultOf.Success(data)
        } else {
            ResultOf.Failure("Request error")
        }

    }

    fun updateChartPeriod(periodText: String) = viewModelScope.launch {
        val period = PricePeriod.fromText(periodText)
        loadChartData(period)
    }


    @AssistedFactory
    interface Factory {
        fun create(cryptoSymbol: String): CryptoInfoViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            cryptoSymbol: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(cryptoSymbol) as T
            }
        }
    }
}