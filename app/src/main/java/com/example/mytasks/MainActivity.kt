package com.example.mytasks

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.content.Context

class MainActivity : Activity() {

    private lateinit var tasksLayout: LinearLayout
    private val tasks = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("MyTasks", Context.MODE_PRIVATE)
        tasks.addAll(prefs.getStringSet("tasks", emptySet()) ?: emptySet())

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(24, 24, 24, 24)
        root.setBackgroundColor(Color.rgb(245, 247, 250))

        val title = TextView(this)
        title.text = "📋 مهامي اليومية"
        title.textSize = 26f
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.rgb(30, 60, 100))
        root.addView(title)

        val input = EditText(this)
        input.hint = "اكتب مهمة جديدة..."
        root.addView(input)

        val addButton = Button(this)
        addButton.text = "إضافة مهمة"
        root.addView(addButton)

        val scroll = ScrollView(this)
        tasksLayout = LinearLayout(this)
        tasksLayout.orientation = LinearLayout.VERTICAL
        scroll.addView(tasksLayout)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        addButton.setOnClickListener {
            val task = input.text.toString().trim()

            if (task.isNotEmpty()) {
                tasks.add(task)
                input.text.clear()
                saveTasks()
                displayTasks()
            }
        }

        setContentView(root)
        displayTasks()
    }

    private fun saveTasks() {
        getSharedPreferences("MyTasks", Context.MODE_PRIVATE)
            .edit()
            .putStringSet("tasks", tasks.toSet())
            .apply()
    }

    private fun displayTasks() {
        tasksLayout.removeAllViews()

        tasks.forEachIndexed { index, task ->
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.gravity = Gravity.CENTER_VERTICAL

            val text = TextView(this)
            text.text = "• $task"
            text.textSize = 18f
            text.setTextColor(Color.BLACK)

            row.addView(
                text,
                LinearLayout.LayoutParams(0, -2, 1f)
            )

            val delete = Button(this)
            delete.text = "حذف"

            delete.setOnClickListener {
                tasks.removeAt(index)
                saveTasks()
                displayTasks()
            }

            row.addView(delete)
            tasksLayout.addView(row)
        }
    }
}
