package com.eatmybrain.cryptoprices.ui.crypto_list


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.eatmybrain.cryptoprices.data.enums.CryptoAppTab
import com.eatmybrain.cryptoprices.data.structures.CryptoItemInfo
import com.eatmybrain.cryptoprices.ui.components.CryptoLazyColumn
import com.eatmybrain.cryptoprices.ui.components.CryptoTabsRow
import com.eatmybrain.cryptoprices.ui.components.LoadingError
import com.eatmybrain.cryptoprices.util.doIfFailure
import com.eatmybrain.cryptoprices.util.doIfSuccess
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun CryptoListScreen(
    viewModel: CryptoListViewModel = hiltViewModel(),
    onItemClicked: (CryptoItemInfo) -> Unit
) {
    Column {
        val allTabs = CryptoAppTab.values().map { it.name }
        var currentTab by rememberSaveable { mutableStateOf(CryptoAppTab.Crypto) }
        CryptoTabsRow(
            allScreens = allTabs,
            currentScreen = currentTab.toString(),
            onItemClicked = {
                currentTab = CryptoAppTab.fromTitle(it)
            }
        )

        CryptoList(
            currentTab = currentTab,
            viewModel = viewModel,
            onItemClicked = onItemClicked
        )
    }

}

@Composable
fun CryptoList(
    currentTab: CryptoAppTab,
    viewModel: CryptoListViewModel,
    onItemClicked: (CryptoItemInfo) -> Unit
) {
    val cryptoListResult by when (currentTab) {
        CryptoAppTab.Crypto -> viewModel.cryptoList.observeAsState()
        CryptoAppTab.DeFi -> viewModel.defiList.observeAsState()
        CryptoAppTab.Metaverse -> viewModel.metaverseList.observeAsState()
        CryptoAppTab.NFT -> viewModel.nftList.observeAsState()
    }
    val isRefreshing by viewModel.isRefreshing.observeAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing ?: true)


    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh =  { viewModel.loadCryptoList(true) },
        modifier = Modifier.fillMaxSize()
    ) {
        cryptoListResult?.doIfSuccess { listItems ->
            CryptoLazyColumn(listItems = listItems, onItemClicked = onItemClicked)
        }

        cryptoListResult?.doIfFailure {
            LoadingError()
        }
    }
}




