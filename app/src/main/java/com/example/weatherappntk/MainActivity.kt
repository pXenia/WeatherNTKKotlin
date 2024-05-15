package com.example.weatherappntk

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherappntk.ui.theme.Blue
import com.example.weatherappntk.ui.theme.WeatherAppNTKTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val apiKey = "" // добавить API ключ с openweathermap.org
    private lateinit var weatherService: WeatherService

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherService = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

        setContent {
            WeatherAppNTKTheme {
                var cityName by remember { mutableStateOf("") }
                var weatherData by remember { mutableStateOf<WeatherData?>(null) }
                Scaffold(
                    containerColor = Blue,
                    content = {
                        OutlinedTextField(
                            value = cityName,
                            onValueChange = { cityName = it },
                            label = { Text("Город") },
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            shape = RoundedCornerShape(10.dp),
                            leadingIcon = {
                                IconButton(onClick = {
                                    GlobalScope.launch(Dispatchers.IO) {
                                        try {
                                            val data =
                                                weatherService.getWeather(cityName, apiKey)
                                            weatherData = data
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Ошибка!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }) {
                                    Icon(Icons.Filled.Refresh, "Refresh")
                                }
                            }
                        )
                        Column(
                            modifier = Modifier.fillMaxSize().padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (weatherData != null) {
                                WeatherCard(weatherData!!)
                            } else {
                                Text("Нет данных о погоде")
                            }
                        }
                    }
                )
            }
        }
    }
}
@SuppressLint("SimpleDateFormat")
@Composable
fun WeatherCard(weatherData: WeatherData) {
    AsyncImage(
        model = ImageToDraw(weatherData.weather[0].icon),
        contentDescription = null
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "${weatherData.name} ${SimpleDateFormat("dd-MM-yyyy HH:mm").format(Date())}",
        fontSize = 20.sp,
        color = Color.Gray
    )
    Spacer(modifier = Modifier.height(60.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Температура:\n${weatherData.main.temp.toInt() - 273}°C",
            textAlign = TextAlign.Center
        )
        Text(
            text = "Давление:\n${weatherData.main.pressure * 0.75} мм рт. ст.",
            textAlign = TextAlign.Center
        )
        Text(
            text = "Влажность:\n${weatherData.main.humidity}%",
            textAlign = TextAlign.Center
        )
    }
}

