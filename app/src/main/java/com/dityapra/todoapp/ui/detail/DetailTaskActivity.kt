package com.dityapra.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dityapra.todoapp.R
import com.dityapra.todoapp.ui.ViewModelFactory
import com.dityapra.todoapp.utils.DateConverter
import com.dityapra.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var dTitle: TextInputEditText
    private lateinit var dDesc: TextInputEditText
    private lateinit var dDueDate: TextInputEditText
    private lateinit var dBtnDelete: Button
    private lateinit var viewModel: DetailTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        dTitle = findViewById(R.id.detail_ed_title)
        dDesc = findViewById(R.id.detail_ed_description)
        dDueDate = findViewById(R.id.detail_ed_due_date)
        dBtnDelete = findViewById(R.id.btn_delete_task)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[DetailTaskViewModel::class.java]

        viewModel.setTaskId(intent.getIntExtra(TASK_ID, 0))
        viewModel.task.observe(this, { task ->
            if (task != null) {
                dTitle.setText(task.title)
                dDesc.setText(task.description)
                dDueDate.setText(DateConverter.convertMillisToString(task.dueDateMillis))
            }
        })

        dBtnDelete.setOnClickListener {
            viewModel.deleteTask()
            onBackPressed()
        }
    }
}