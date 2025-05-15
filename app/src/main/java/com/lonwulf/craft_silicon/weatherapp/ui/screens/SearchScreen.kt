package com.lonwulf.craft_silicon.weatherapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.lonwulf.craft_silicon.weatherapp.core.util.GenericResultState
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherModel
import com.lonwulf.craft_silicon.weatherapp.navigation.Destinations
import com.lonwulf.craft_silicon.weatherapp.navigation.NavComposable
import com.lonwulf.craft_silicon.weatherapp.navigation.TopLevelDestinations
import com.lonwulf.craft_silicon.weatherapp.presentation.ui.CommonToolBar
import com.lonwulf.craft_silicon.weatherapp.presentation.ui.SearchBar
import com.lonwulf.craft_silicon.weatherapp.ui.theme.BottomBarBgGray
import com.lonwulf.craft_silicon.weatherapp.ui.theme.TextBlack
import com.lonwulf.craft_silicon.weatherapp.ui.viewmodel.SharedViewModel
import com.lonwulf.craft_silicon.weatherapp.util.thousandFormatter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.navigation.koinNavViewModel


class SearchScreenComposable : NavComposable {
    @Composable
    override fun Composable(
        navHostController: NavHostController, snackbarHostState: SnackbarHostState
    ) {
        SearchScreen(navHostController = navHostController, snackbarHostState = snackbarHostState)
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val parentEntry =
        remember { navHostController.getBackStackEntry(TopLevelDestinations.HomeScreen.route) }
    val vm = koinNavViewModel<SharedViewModel>(viewModelStoreOwner = parentEntry)
    var weatherObject by remember { mutableStateOf<WeatherModel?>(null) }
    val apiState by vm.weatherForeCastStateFlow.collectAsState()
    var isClicked by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(apiState) {
        when (apiState) {
            is GenericResultState.Loading -> {
            }

            is GenericResultState.Empty -> {}
            is GenericResultState.Error -> scope.launch {
                Log.e("Err:   ", (apiState as GenericResultState.Error).msg.toString())
                snackbarHostState.showSnackbar(
                    message = (apiState as GenericResultState.Error).msg ?: "something went wrong",
                    duration = SnackbarDuration.Short
                )
            }

            is GenericResultState.Success -> {
                weatherObject =
                    (apiState as GenericResultState.Success<WeatherModel>).result!!
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        CommonToolBar(title = Destinations.SearchScreen.route, onclick = {
            navHostController.navigateUp()
        })
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (searchField, weatherTile, placeholderTxt) = createRefs()

            SearchBar(modifier = modifier.constrainAs(searchField) {
                top.linkTo(parent.top, 30.dp)
                start.linkTo(parent.start, 20.dp)
                end.linkTo(parent.end, 20.dp)
                width = Dimension.fillToConstraints
            }, onClick = {

            }, onSearch = {
                if (it.isNotEmpty()) {
                    vm.fetchWeatherForeCast(query = it)
                }
            })

            weatherObject.takeIf { it != null }?.let { model ->
                ElevatedCard(
                    modifier = modifier
                        .wrapContentHeight()
                        .clickable {
                            isClicked = true
                            weatherObject?.let {
                                vm.addWeatherHistory(it)
                            }
                            navHostController.navigate(TopLevelDestinations.HomeScreen.route)
                        }
                        .constrainAs(weatherTile) {
                            top.linkTo(searchField.bottom, margin = 20.dp)
                            start.linkTo(parent.start, 30.dp)
                            end.linkTo(parent.end, 30.dp)
                            width = Dimension.fillToConstraints
                        },
                    elevation = CardDefaults.elevatedCardElevation(8.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = BottomBarBgGray,
                        contentColor = TextBlack
                    )
                ) {
                    ConstraintLayout(
                        modifier = modifier
                            .background(color = BottomBarBgGray)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(10.dp)
                    ) {
                        val (country, population, name, checkIcn, lat, lon, latTitle, lonTitle) = createRefs()
                        val verticalGuideLine = createGuidelineFromStart(0.4f)
                        val coordGuideLine = createGuidelineFromStart(0.5f)

                        model.name?.let {
                            Text(
                                style = MaterialTheme.typography.titleLarge,
                                text = it,
                                modifier = modifier.constrainAs(name) {
                                    top.linkTo(parent.top, margin = 5.dp)
                                    start.linkTo(parent.start, margin = 5.dp)
                                })
                        }
                        model.country?.let {
                            Text(
                                style = MaterialTheme.typography.displayMedium,
                                text = "$it",
                                modifier = modifier.constrainAs(country) {
                                    top.linkTo(name.bottom)
                                    start.linkTo(name.start)
                                })
                        }
                        model.population?.let {
                            Text(
                                style = MaterialTheme.typography.bodyLarge,
                                text = "Population: ${thousandFormatter(it)}",
                                modifier = modifier.constrainAs(population) {
                                    top.linkTo(name.top, 5.dp)
                                    start.linkTo(verticalGuideLine)
                                })
                        }
                        model.lat?.let {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                text = "Lat: ",
                                modifier = modifier.constrainAs(latTitle) {
                                    top.linkTo(country.top, 5.dp)
                                    end.linkTo(coordGuideLine, 5.dp)
                                })
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                text = it.toString(),
                                modifier = modifier.constrainAs(lat) {
                                    top.linkTo(latTitle.bottom, 5.dp)
                                    end.linkTo(coordGuideLine, 5.dp)
                                })
                        }
                        model.lon?.let {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                text = "Long: ",
                                modifier = modifier.constrainAs(lonTitle) {
                                    top.linkTo(latTitle.top)
                                    start.linkTo(coordGuideLine, 5.dp)
                                })
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                text = it.toString(),
                                modifier = modifier.constrainAs(lon) {
                                    top.linkTo(lonTitle.bottom, 5.dp)
                                    start.linkTo(coordGuideLine, 5.dp)
                                })
                        }

                        if (isClicked) {
                            Image(
                                painter = painterResource(com.lonwulf.craft_silicon.weatherapp.presentation.R.drawable.check_circle_52dp),
                                contentDescription = "",
                                modifier = modifier
                                    .size(20.dp)
                                    .constrainAs(checkIcn) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        end.linkTo(parent.end, 5.dp)
                                    })
                        }
                        Spacer(modifier = modifier.height(10.dp))
                    }
                }
            } ?: ShowEmptyData(modifier.constrainAs(placeholderTxt) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
        }
    }
}
