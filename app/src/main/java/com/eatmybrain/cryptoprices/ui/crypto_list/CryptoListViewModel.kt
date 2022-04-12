package com.eatmybrain.cryptoprices.ui.crypto_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatmybrain.cryptoprices.data.Repository
import com.eatmybrain.cryptoprices.data.structures.CryptoItemInfo
import com.eatmybrain.cryptoprices.data.structures.CryptoListResponse
import com.eatmybrain.cryptoprices.util.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _cryptoList = MutableLiveData<ResultOf<List<CryptoItemInfo>>>()
    val cryptoList: LiveData<ResultOf<List<CryptoItemInfo>>> = _cryptoList

    private val _defiList = MutableLiveData<ResultOf<List<CryptoItemInfo>>>()
    val defiList:LiveData<ResultOf<List<CryptoItemInfo>>> = _defiList

    private val _nftList = MutableLiveData<ResultOf<List<CryptoItemInfo>>>()
    val nftList :LiveData<ResultOf<List<CryptoItemInfo>>> = _nftList

    private val _metaverseList = MutableLiveData<ResultOf<List<CryptoItemInfo>>>()
    val metaverseList:LiveData<ResultOf<List<CryptoItemInfo>>> = _metaverseList


    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing:LiveData<Boolean> = _isRefreshing

    init {
        loadCryptoList(false)
    }

    fun loadCryptoList(delay:Boolean) = viewModelScope.launch{
        _isRefreshing.postValue(true)

        val response = withContext(Dispatchers.IO){
             repository.loadCryptoList()
        }

        if(delay) delay(1000)

        if(response.status.errorCode == 0){
            loadImageUrls(response)
            parseResponseData(response)
        } else{
           responseError(response)
        }

        _isRefreshing.postValue(false)
    }

    private  fun responseError(response:CryptoListResponse) {
        val errorMessage = response.status.errorMessage!!
        val result = ResultOf.Failure(errorMessage)
        _cryptoList.postValue(result)
        _defiList.postValue(result)
        _nftList.postValue(result)
        _metaverseList.postValue(result)
    }

    private fun loadImageUrls(response: CryptoListResponse){
        response.data!!.forEach { it.imageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/${it.id}.png" }
    }

    private suspend fun parseResponseData(response:CryptoListResponse) = withContext(Dispatchers.IO){
        val crypto = ResultOf.Success(response.data!!.filter { it.tags.contains("mineable") })
        val defi = ResultOf.Success(response.data.filter { it.tags.contains("defi") })
        val nft = ResultOf.Success(response.data.filter { it.tags.contains("collectibles-nfts") })
        val metaverse = ResultOf.Success(response.data.filter { it.tags.contains("metaverse") })
        _cryptoList.postValue(crypto)
        _defiList.postValue(defi)
        _nftList.postValue(nft)
        _metaverseList.postValue(metaverse)
    }

}