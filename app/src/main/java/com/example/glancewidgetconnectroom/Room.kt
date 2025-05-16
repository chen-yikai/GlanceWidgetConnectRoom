package com.example.glancewidgetconnectroom

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String
)

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<Note>>

    @Insert
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)
}

@Database(entities = [Note::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun NoteDao(): NoteDao
}

fun getDataBase(context: Context): AppDataBase {
    return Room
        .databaseBuilder(context.applicationContext, AppDataBase::class.java, "app_db")
        .fallbackToDestructiveMigration().build()
}

class NoteViewModel(private val db: NoteDao) : ViewModel() {
    val notes = db.getAll()

    fun addNote(note: Note) {
        viewModelScope.launch {
            db.insert(note)
        }
        triggerWidgetUpdate()
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            db.delete(note)
        }
        triggerWidgetUpdate()
    }
    private fun triggerWidgetUpdate() {
        viewModelScope.launch {
            GlanceWidget().updateAll(AppClass.appContext)
        }
    }
}

class NoteRepository(private val context: Context) {
    private val dao: NoteDao by lazy {
        getDataBase(context.applicationContext).NoteDao()
    }

    suspend fun getAllNotes(): List<Note> {
        return dao.getAll().first()
    }
}
