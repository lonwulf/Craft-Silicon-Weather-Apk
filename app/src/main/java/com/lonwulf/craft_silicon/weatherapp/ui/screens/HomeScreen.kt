package com.lonwulf.craft_silicon.weatherapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.lonwulf.craft_silicon.weatherapp.R
import com.lonwulf.craft_silicon.weatherapp.core.util.GenericResultState
import com.lonwulf.craft_silicon.weatherapp.domain.mapper.toDomainWeatherModel
import com.lonwulf.craft_silicon.weatherapp.domain.model.AppSettings
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherModel
import com.lonwulf.craft_silicon.weatherapp.navigation.Destinations
import com.lonwulf.craft_silicon.weatherapp.navigation.NavComposable
import com.lonwulf.craft_silicon.weatherapp.presentation.ui.CircularProgressBar
import com.lonwulf.craft_silicon.weatherapp.presentation.ui.SearchBar
import com.lonwulf.craft_silicon.weatherapp.ui.theme.TextBlack
import com.lonwulf.craft_silicon.weatherapp.ui.viewmodel.SharedViewModel
import com.lonwulf.craft_silicon.weatherapp.util.convertToLocalTime
import com.lonwulf.craft_silicon.weatherapp.util.timezoneToUtcOffset
import org.koin.androidx.compose.navigation.koinNavViewModel

class HomeScreenComposable : NavComposable {
    @Composable
    override fun Composable(
        navHostController: NavHostController, snackbarHostState: SnackbarHostState
    ) {
        HomeScreen(navHostController = navHostController)
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel = koinNavViewModel(),
) {
    var preference by remember { mutableStateOf<AppSettings?>(null) }
    val historyFetchState by sharedViewModel.appSettingsPreference.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
//        sharedViewModel.fetchAllHistory()
        sharedViewModel.fetchWeatherSettings()
    }

    LaunchedEffect(historyFetchState) {

        when (historyFetchState) {
            is GenericResultState.Loading -> isLoading = true
            is GenericResultState.Empty -> {}
            is GenericResultState.Error -> {
                isLoading = false
            }

            is GenericResultState.Success -> {
                isLoading = false
                preference =
                    (historyFetchState as GenericResultState.Success<AppSettings>).result!!
            }
        }
    }


    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (searchField, name, placeholderTxt, img, country, arrowIcn, weatherTile) = createRefs()
        val horizontalGuide = createGuidelineFromBottom(0.5f)

        CircularProgressBar(isLoading)

        SearchBar(modifier = modifier.constrainAs(searchField) {
            top.linkTo(parent.top, 30.dp)
            start.linkTo(parent.start, 20.dp)
            end.linkTo(parent.end, 20.dp)
            width = Dimension.fillToConstraints
        }, onClick = {
            navHostController.navigate(Destinations.SearchScreen.route)
        }, onSearch = {})

        val dataToDisplay =
            preference.let { pref ->
                WeatherModel(
                    name = pref?.name ?: "",
                    country = pref?.country ?: "",
                    timezone = pref?.timezone ?: 0,
                    sunset = pref?.sunset ?: 0,
                    sunrise = pref?.sunrise ?: 0,
                    population = pref?.population ?: 0,
                    lat = pref?.lat ?: 0.0,
                    lon = pref?.lon ?: 0.0,
                    weatherList = pref?.history?.toDomainWeatherModel() ?: emptyList()
                )
            }


        dataToDisplay.takeIf { it.name.isNotEmpty() }?.let {
            Text(
                text = it.country,
                style = MaterialTheme.typography.headlineLarge,
                modifier = modifier.constrainAs(country) {
                    bottom.linkTo(name.top, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Image(
                painter = painterResource(com.lonwulf.craft_silicon.weatherapp.presentation.R.drawable.fancy_arrow),
                contentDescription = "fancy arrow",
                modifier = modifier
                    .size(20.dp)
                    .constrainAs(arrowIcn) {
                        start.linkTo(country.end, margin = 10.dp)
                        top.linkTo(country.top)
                        bottom.linkTo(country.bottom)
                    })
            Text(
                text = it.name,
                style = MaterialTheme.typography.displayLarge,
                modifier = modifier.constrainAs(name) {
                    bottom.linkTo(horizontalGuide)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Box(
                modifier = modifier
                    .constrainAs(weatherTile) {
                        start.linkTo(parent.start, 20.dp)
                        end.linkTo(parent.end, 20.dp)
                        top.linkTo(horizontalGuide)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WeatherItem(
                        label = stringResource(R.string.timezone),
                        value = "${timezoneToUtcOffset(it.timezone)}"
                    )
                    WeatherItem(
                        label = stringResource(R.string.sunrise), value = "${
                            convertToLocalTime(
                                timestamp = it.sunrise,
                                timezoneOffsetSeconds = it.timezone
                            )
                        }"
                    )
                    WeatherItem(
                        label = stringResource(R.string.sunset),
                        value = "${
                            convertToLocalTime(
                                timestamp = it.sunset,
                                timezoneOffsetSeconds = it.timezone
                            )
                        }"
                    )
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

@Composable
fun ShowEmptyData(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.no_city_selected),
            style = MaterialTheme.typography.headlineLarge.copy(
                color = TextBlack
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.please_search),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = TextBlack
            )
        )
    }
}

@Composable
fun WeatherItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label, style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value, style = MaterialTheme.typography.bodyLarge.copy(
                color = TextBlack
            )
        )
    }
}
