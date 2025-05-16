package com.example.glancewidgetconnectroom

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.example.glancewidgetconnectroom.ui.theme.GlanceWidgetConnectRoomTheme

val LocalNoteRoom = compositionLocalOf<NoteViewModel> { error("error getting LocalNoteRoom") }

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlanceWidgetConnectRoomTheme {
                val room = DatabaseProvider.getDatabase(this)
                val noteRoom = NoteViewModel(room.NoteDao(),this.applicationContext)
                CompositionLocalProvider(LocalNoteRoom provides noteRoom) {
                    HomeScreen()
                }
            }
        }
    }
}