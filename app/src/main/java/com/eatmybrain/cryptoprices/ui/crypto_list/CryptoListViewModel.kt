package com.eatmybrain.cryptoprices.ui.crypto_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatmybrain.cryptoprices.ListLoadingDelay
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
        _isRefreshing.value = true

        val response = withContext(Dispatchers.IO){
             repository.cryptoList()
        }

        if(delay) delay(ListLoadingDelay)

        if(response.status.errorCode == 0){
            parseResponseData(response)
        } else{
           responseError(response)
        }

        _isRefreshing.value = false
    }

    private  fun responseError(response:CryptoListResponse) {
        val errorMessage = response.status.errorMessage!!
        val result = ResultOf.Failure(errorMessage)
        _cryptoList.value = result
        _defiList.value = result
        _nftList.value = result
        _metaverseList.value = result
    }



    private suspend fun parseResponseData(response:CryptoListResponse) = withContext(Dispatchers.IO){
        val crypto = ResultOf.Success(response.data?.filter { it.tags.contains("mineable") } ?: emptyList())
        val defi = ResultOf.Success(response.data?.filter { it.tags.contains("defi") }?: emptyList())
        val nft = ResultOf.Success(response.data?.filter { it.tags.contains("collectibles-nfts") }?: emptyList())
        val metaverse = ResultOf.Success(response.data?.filter { it.tags.contains("metaverse") }?: emptyList())
        withContext(Dispatchers.Main){
            _cryptoList.value = crypto
            _defiList.value = defi
            _nftList.value = nft
            _metaverseList.value = metaverse
        }

    }

}