package com.adrianlambertt.dino

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.icu.util.Calendar
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class MyWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.my_widget)

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 5..11 -> "Good Morning ❤\uFE0F"
        in 12..16 -> "Good Afternoon ❤\uFE0F"
        else -> "Good Evening ❤\uFE0F"
    }

    views.setTextViewText(R.id.widgetGreeting, greeting)
    views.setImageViewResource(R.id.widgetImage, getSeasonalImage())

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

fun getSeasonalImage(): Int {
    val month = Calendar.getInstance().get(Calendar.MONTH)
    return  getWinterImage()

//    return when(month) {
//        in 0..1, 11 -> R.drawable.winter_1
//        in 2..4 -> R.drawable.spring_1
//        in 5..7 -> R.drawable.summer_1
//        else -> R.drawable.autumn_1
//    }
}

fun getWinterImage(): Int {
    val random = (0..14).random()
    return when(random) {
        in 0..2 -> R.drawable.winter_1
        in 3..5 -> R.drawable.winter_2
        in 6..8 -> R.drawable.winter_3
        else -> R.drawable.winter_4
//        else -> R.drawable.winter_5
    }
}