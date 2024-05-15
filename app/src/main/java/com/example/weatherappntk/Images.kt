package com.example.weatherappntk



fun ImageToDraw(id: String): Int{
    return when (id.substring(0,2)) {
        "01" -> R.drawable.p01d
        "02" -> R.drawable.p02d
        "03" -> R.drawable.p03d
        "04" -> R.drawable.p04d
        "09" -> R.drawable.p09d
        "10" -> R.drawable.p10d
        "11" -> R.drawable.p11d
        "13" -> R.drawable.p13d
        "50" -> R.drawable.p50d
        else -> {R.drawable.p01d}
    }
}