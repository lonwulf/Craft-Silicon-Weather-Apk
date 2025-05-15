package com.lonwulf.craft_silicon.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.lonwulf.craft_silicon.weatherapp.R
import com.lonwulf.craft_silicon.weatherapp.core.util.GenericResultState
import com.lonwulf.craft_silicon.weatherapp.domain.model.AppSettings
import com.lonwulf.craft_silicon.weatherapp.navigation.Destinations
import com.lonwulf.craft_silicon.weatherapp.navigation.NavComposable
import com.lonwulf.craft_silicon.weatherapp.navigation.TopLevelDestinations
import com.lonwulf.craft_silicon.weatherapp.ui.theme.BottomBarBgGray
import com.lonwulf.craft_silicon.weatherapp.ui.theme.BottomBarSelectedColor
import com.lonwulf.craft_silicon.weatherapp.ui.theme.TextBlack
import com.lonwulf.craft_silicon.weatherapp.ui.viewmodel.SharedViewModel
import com.lonwulf.craft_silicon.weatherapp.util.formatPressure
import com.lonwulf.craft_silicon.weatherapp.util.formatVisibility
import com.lonwulf.craft_silicon.weatherapp.util.formatWindSpeed
import org.koin.androidx.compose.navigation.koinNavViewModel

class ForecastScreenComposable : NavComposable {
    @Composable
    override fun Composable(
        navHostController: NavHostController,
        snackbarHostState: SnackbarHostState
    ) {
        ForecastScreen(navHostController = navHostController)
    }
}

@Composable
fun ForecastScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val parentEntry =
        remember { navHostController.getBackStackEntry(TopLevelDestinations.HomeScreen.route) }
    val vm = koinNavViewModel<SharedViewModel>(viewModelStoreOwner = parentEntry)
    var preference by remember { mutableStateOf<AppSettings?>(null) }
    val historyFetchState by vm.appSettingsPreference.collectAsState()


    LaunchedEffect(Unit) {
        vm.fetchAllHistory()
    }
    LaunchedEffect(historyFetchState) {
        when (historyFetchState) {
            is GenericResultState.Loading -> {}
            is GenericResultState.Empty -> {}
            is GenericResultState.Error -> {}
            is GenericResultState.Success -> {
                preference =
                    (historyFetchState as GenericResultState.Success<AppSettings>).result!!
            }
        }
    }
    preference.takeIf { it != null }?.let {
        LazyColumn(
            modifier = modifier
                .background(color = BottomBarBgGray)
                .fillMaxSize()
                .padding(10.dp)
        ) {
            items(items = preference?.history?.reversed() ?: emptyList()) { prefs ->
                ElevatedCard(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp)
                        .clickable {
                            vm.setWeatherObject(prefs.weatherDetails.toList())
                            navHostController.navigate(Destinations.WeatherDetailsScreen.route)
                        },
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
                        val (humidity, feelsLike, windSpeed, visibility, minTemp, maxTemp, pressure) = createRefs()
                        Text(
                            text = "Humidity: ${prefs.humidity}%",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextBlack
                            ),
                            modifier = modifier.constrainAs(humidity) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start, margin = 20.dp)
                            })

                        Text(
                            text = "Feels Like: ${prefs.feelsLike}°",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextBlack
                            ),
                            modifier = modifier.constrainAs(feelsLike) {
                                top.linkTo(humidity.bottom, margin = 10.dp)
                                start.linkTo(parent.start, margin = 20.dp)
                            })
                        Text(
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextBlack
                            ),
                            text = "Visibility: ${formatVisibility(prefs.visibility)}",
                            modifier = modifier.constrainAs(visibility) {
                                top.linkTo(feelsLike.bottom, margin = 10.dp)
                                start.linkTo(parent.start, margin = 20.dp)
                            })
                        Text(
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextBlack
                            ),
                            text = "Pressure: ${formatPressure(prefs.pressure)}",
                            modifier = modifier.constrainAs(pressure) {
                                top.linkTo(visibility.bottom, margin = 10.dp)
                                start.linkTo(parent.start, margin = 20.dp)
                            })
                        Text(
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextBlack
                            ),
                            text = "Wind speed: ${formatWindSpeed(prefs.windSpeed)}",
                            modifier = modifier.constrainAs(windSpeed) {
                                top.linkTo(pressure.bottom, margin = 10.dp)
                                start.linkTo(parent.start, margin = 20.dp)
                            })
                        Text(
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextBlack
                            ),
                            text = "${prefs.tempMax}°C",
                            color = Color(0xFFE53935),
                            modifier = modifier.constrainAs(maxTemp) {
                                top.linkTo(humidity.top)
                                end.linkTo(parent.end, margin = 20.dp)
                            })
                        Text(
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextBlack
                            ),
                            text = "${prefs.tempMin}°C",
                            color = Color(0xFF1E88E5),
                            modifier = modifier.constrainAs(minTemp) {
                                top.linkTo(maxTemp.bottom, margin = 10.dp)
                                end.linkTo(parent.end, margin = 20.dp)
                            })
                    }
                }
            }
        }
    } ?: ShowEmptyHistoryData()
}

@Composable
fun ShowEmptyHistoryData() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.headlineLarge.copy(
                color = TextBlack
            )
        )
    }

}
