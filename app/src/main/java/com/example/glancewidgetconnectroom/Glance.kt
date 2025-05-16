package com.example.glancewidgetconnectroom

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.IconImageProvider
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class GlanceReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = GlanceWidget()
}

class GlanceWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val notes by NoteRepository(context).getAllNotes().collectAsState(emptyList())

            Scaffold(titleBar = {
                TitleBar(
                    startIcon = ImageProvider(R.drawable.note),
                    title = "All Notes",
                    actions = {
                        Image(
                            provider = ImageProvider(R.drawable.edit),
                            contentDescription = "Add",
                            modifier = GlanceModifier
                                .clickable (
                                    actionStartActivity(intent)
                                )
                                .padding(16.dp)
                        )
                    }
                )
            }, modifier = GlanceModifier.fillMaxSize()) {
                LazyColumn(GlanceModifier.background(GlanceTheme.colors.widgetBackground)) {
                    items(notes) {
                        Box(
                            GlanceModifier.background(GlanceTheme.colors.widgetBackground)
                                .padding(bottom = 10.dp)
                        ) {
                            Column(
                                GlanceModifier.padding(10.dp)
                                    .fillMaxWidth()
                                    .background(GlanceTheme.colors.primaryContainer)
                                    .cornerRadius(10.dp)
                            ) {
                                Text(
                                    it.title,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                )
                                Text(it.content)
                            }
                        }
                    }
                }
            }
        }
    }
}