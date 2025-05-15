package com.lonwulf.craft_silicon.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.lonwulf.craft_silicon.weatherapp.navigation.Destinations
import com.lonwulf.craft_silicon.weatherapp.navigation.NavComposable
import com.lonwulf.craft_silicon.weatherapp.navigation.TopLevelDestinations
import com.lonwulf.craft_silicon.weatherapp.presentation.ui.CommonToolBar
import com.lonwulf.craft_silicon.weatherapp.presentation.ui.LoadImageFromUrl
import com.lonwulf.craft_silicon.weatherapp.ui.theme.BottomBarBgGray
import com.lonwulf.craft_silicon.weatherapp.ui.theme.BottomBarSelectedColor
import com.lonwulf.craft_silicon.weatherapp.ui.viewmodel.SharedViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel

class WeatherDetailsScreenComposable : NavComposable {
    @Composable
    override fun Composable(
        navHostController: NavHostController,
        snackbarHostState: SnackbarHostState
    ) {
        WeatherDetailsScreen(navHostController = navHostController)
    }
}

@Composable
fun WeatherDetailsScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val parentEntry =
        remember { navHostController.getBackStackEntry(TopLevelDestinations.HomeScreen.route) }
    val vm = koinNavViewModel<SharedViewModel>(viewModelStoreOwner = parentEntry)
    val details by vm.detailsStateFlow.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        CommonToolBar(title = Destinations.WeatherDetailsScreen.route, onclick = {
            navHostController.navigateUp()
        })
        LazyColumn(
            modifier = modifier
                .background(color = BottomBarBgGray)
                .fillMaxSize()
                .padding(10.dp)
        ) {
            items(items = details) { prefs ->
                ElevatedCard(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp),
                    elevation = CardDefaults.elevatedCardElevation(8.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = BottomBarBgGray,
                        contentColor = BottomBarSelectedColor
                    )
                ) {
                    ConstraintLayout(
                        modifier = modifier
                            .background(color = BottomBarBgGray)
                            .padding(10.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        val (main, desc, img) = createRefs()
                        LoadImageFromUrl(
                            url = "https:${prefs.icon}",
                            ctx = LocalContext.current,
                            modifier = modifier
                                .width(100.dp)
                                .constrainAs(img) {
                                    start.linkTo(parent.start, margin = 5.dp)
                                    top.linkTo(main.top)
                                    bottom.linkTo(desc.bottom)
                                    height = Dimension.fillToConstraints
                                })
                        Text(text = "Main: ${prefs.main}", modifier = modifier.constrainAs(main) {
                            top.linkTo(parent.top, margin = 10.dp)
                            end.linkTo(parent.end, margin = 20.dp)
                        })
                        Text(
                            text = "Description: ${prefs.description}",
                            modifier = modifier.constrainAs(desc) {
                                top.linkTo(main.bottom, margin = 10.dp)
                                end.linkTo(parent.end, margin = 20.dp)
                            })
                    }
                }
            }
        }
    }
}