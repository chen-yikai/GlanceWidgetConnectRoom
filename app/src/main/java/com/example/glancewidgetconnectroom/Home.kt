package com.example.glancewidgetconnectroom

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CaretScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.glance.GlanceId
import androidx.glance.LocalContext
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import kotlinx.coroutines.launch
import kotlin.math.sin

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(context:Context) {
    val noteRoom = LocalNoteRoom.current
    val notes by noteRoom.notes.collectAsState(emptyList())
    var showAddNote by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (showAddNote) {
        AddNoteDialog { showAddNote = false }
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            showAddNote = true
        }) {
            Icon(
                Icons.Default.Add,
                contentDescription = null
            )
        }
    }) { innerPadding ->
        LazyColumn(
            Modifier.padding(PaddingValues(top = innerPadding.calculateTopPadding())),
            contentPadding = PaddingValues(10.dp)
        ) {
            stickyHeader {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("All Notes", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                    FilledTonalButton(onClick = {
                        scope.launch {
                          val app =   AppClass.appContext as AppClass
                           app.updateWidget()
                        }
                    }) { Text("Update Widget") }
                }
            }
            items(notes) {
                var showTool by remember { mutableStateOf(false) }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    showTool = !showTool
                                }, onTap = {
                                    showTool = false
                                }
                            )
                        }
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Column(Modifier.padding(10.dp)) {
                            Text(it.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(it.content)
                        }
                        if (showTool) IconButton(
                            onClick = {
                                noteRoom.deleteNote(it)
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(dismiss: () -> Unit) {
    val noteRoom = LocalNoteRoom.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = dismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Add Note",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = title,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { title = it },
                singleLine = true,
                placeholder = { Text("Title") })
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = content,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { content = it },
                singleLine = false,
                placeholder = { Text("Note Content") })
            Spacer(Modifier.height(20.dp))
            FilledTonalButton(onClick = {
                noteRoom.addNote(Note(title = title, content = content))
                dismiss()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Done")
            }
        }
    }
}