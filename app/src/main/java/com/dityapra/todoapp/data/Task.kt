package com.dityapra.todoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @NotNull

    @ColumnInfo(name = "id")
    val id: Int,
    @NotNull

    @ColumnInfo(name = "title")
    val title: String,
    @NotNull

    @ColumnInfo(name = "description")
    val description: String,
    @NotNull

    @ColumnInfo(name = "dueDate")
    val dueDateMillis: Long,
    @NotNull

    @ColumnInfo(name = "completed")
    val isCompleted: Boolean = false
)