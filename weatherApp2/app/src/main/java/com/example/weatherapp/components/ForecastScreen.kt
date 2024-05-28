package com.example.weatherapp.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weatherapp.components.weather.Weather
import com.example.weatherapp.components.weather.WeatherType
import com.example.weatherapp.components.weather.WeatherViewModel
import com.example.weatherapp.components.weather.WeatherViewModelFactory

@Composable
fun ForecastScreen() {
    val viewModel: WeatherViewModel = viewModel(factory = WeatherViewModelFactory.create())
    val weather = viewModel.weather.collectAsState().value

    viewModel.getWeather()

    //scroll state for inner content
    val scrollState = rememberScrollState()

    var infoIsOpen by remember { mutableStateOf(false) }
    var expandedDayIndex by remember { mutableStateOf(-1) }

    val timeList = weather?.daily?.time

    Box(
        modifier = Modifier
            .padding(top = 60.dp),
        contentAlignment = Alignment.Center,

        ) {
        Column {

            Text(text = "7 DAYS",
                fontSize = 35.sp,
                color = Color.Yellow,
                modifier = Modifier.padding(start = 130.dp)
            )
            Spacer(modifier = Modifier.size(45.dp))
            Box(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = 500.dp)
            ) {

                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    Row {
                        Text(text = "DAY",
                            fontSize = 20.sp,
                            color = Color.Yellow,
                            modifier = Modifier.padding(start = 50.dp)
                        )

                        Text(text = "°C",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier.padding(start = 160.dp)
                        )
                    }
                    // Today's forecast
                    Column {
                        if (timeList != null) {
                            for (day in 0 until 7) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Spacer(modifier = Modifier.size(20.dp))
                                    Text(
                                        text = "${getDayFromDate(index = day, weather = weather)
                                            ?: "Loading"}.${getMonthFromDate(day, weather)}",
                                        fontSize = 30.sp,
                                        modifier = Modifier.padding(start = 16.dp),
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.size(50.dp))
                                    Icon(
                                        imageVector = WeatherType.getWeatherType(weather.daily.weather_code[day]).icon,
                                        contentDescription = WeatherType.getWeatherType(weather.daily.weather_code[day]).weatherDesc,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.size(15.dp))
                                    Text(
                                        text = "${weather.daily.temperature_2m_max[day]}°C",
                                        fontSize = 30.sp,
                                        modifier = Modifier.padding(start = 16.dp),
                                        color = Color.White
                                    )
                                }
                                    Row {
                                        Button(
                                            onClick = {
                                                expandedDayIndex = if (expandedDayIndex == day) {
                                                    -1
                                                } else {
                                                    day
                                                }
                                            },
                                            modifier = Modifier
                                                .padding(start = 10.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF87CEEB))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.ArrowForwardIos,
                                                contentDescription = "Arrow",
                                                modifier = Modifier.size(18.dp),
                                                tint = Color.White,
                                            )
                                        }
                                        if (expandedDayIndex == day) {
                                            Text(text = "min: ${weather.daily.temperature_2m_min[day]}°C",
                                                fontSize = 20.sp,
                                                modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                                                color = Color.White
                                            )
                                            Text(text = WeatherType.getWeatherType(weather.daily.weather_code[day]).weatherDesc,
                                                fontSize = 18.sp,
                                                modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                                                color = Color.White
                                            )
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }

    }
}
fun getDayFromDate(index: Int, weather: Weather?): String? {
    val timeString = weather?.daily?.time?.get(index)
    return timeString?.substring(8, 10)
}
fun getMonthFromDate(index: Int, weather: Weather?): String? {
    val timeString = weather?.daily?.time?.get(index)
    return timeString?.substring(5, 7)
}