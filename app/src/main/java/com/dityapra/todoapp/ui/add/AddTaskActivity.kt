package com.dityapra.todoapp.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dityapra.todoapp.R
import com.dityapra.todoapp.data.Task
import com.dityapra.todoapp.ui.ViewModelFactory
import com.dityapra.todoapp.utils.DatePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var viewModel: AddTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        supportActionBar?.title = getString(R.string.add_task)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[AddTaskViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val mTitle =
                    findViewById<TextInputEditText>(R.id.add_ed_title).text.toString().trim()
                val mDescription =
                    findViewById<TextInputEditText>(R.id.add_ed_description).text.toString().trim()
                val task = Task(0, mTitle, mDescription, dueDateMillis, false)

                viewModel.addTask(task)
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)
        dueDateMillis = calendar.timeInMillis
    }
}