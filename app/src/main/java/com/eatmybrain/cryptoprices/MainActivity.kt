package com.eatmybrain.cryptoprices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eatmybrain.cryptoprices.ui.crypto_info.CryptoInfoScreen
import com.eatmybrain.cryptoprices.ui.crypto_info.CryptoInfoViewModel
import com.eatmybrain.cryptoprices.ui.crypto_list.CryptoListScreen
import com.eatmybrain.cryptoprices.ui.theme.CryptoPricesTheme
import com.eatmybrain.cryptoprices.util.CryptoAppScreens
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun cryptoInfoViewModelFactory() : CryptoInfoViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoPricesApp()
        }
    }
}



@Composable
fun CryptoPricesApp() {
    CryptoPricesTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            CryptoAppNavHost(navController = navController)
        }
    }
}

@Composable
fun CryptoAppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = CryptoAppScreens.CryptoList.name,
        modifier = modifier
    ) {
        composable(CryptoAppScreens.CryptoList.name) {
            CryptoListScreen(
                onItemClicked = {
                    navigateToCryptoInfo(navController, it.symbol)
                }
            )
        }

        composable(
            route = "${CryptoAppScreens.CryptoInfo.name}/{symbol}",
            arguments = listOf(
                navArgument("symbol") { type = NavType.StringType }
            )
        ) {
            val cryptoSymbol = it.arguments?.getString("symbol")!!
            CryptoInfoScreen(cryptoSymbol)
        }
    }
}

fun navigateToCryptoInfo(navController: NavController, name:String){
    navController.navigate("${CryptoAppScreens.CryptoInfo.name}/$name")
}