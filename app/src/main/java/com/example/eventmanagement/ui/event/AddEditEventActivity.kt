package com.example.eventmanagement.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eventmanagement.R
import com.example.eventmanagement.data.model.Event
import com.example.eventmanagement.ui.dashboard.DashboardActivity
import com.example.eventmanagement.utils.UiState
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class AddEditEventActivity : AppCompatActivity() {

    private val vm: EventViewModel by viewModels()

    // Class-level date variable
    private var date: Date = Date(System.currentTimeMillis() + 3600000)

    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit_event)

        val title = findViewById<EditText>(R.id.title)
        val description = findViewById<EditText>(R.id.description)
        val location = findViewById<EditText>(R.id.location)
        val dateTextView = findViewById<TextView>(R.id.date)
        val saveButton = findViewById<Button>(R.id.save)
        val deleteButton = findViewById<Button>(R.id.delete)

        // Get event ID if editing
        id = intent.getStringExtra("id").orEmpty()

        if (id.isNotBlank()) {

            title.setText(
                intent.getStringExtra("title").orEmpty()
            )

            description.setText(
                intent.getStringExtra("description").orEmpty()
            )

            location.setText(
                intent.getStringExtra("location").orEmpty()
            )

            date = Date(
                intent.getLongExtra(
                    "date",
                    date.time
                )
            )
        }

        // Display selected date
        dateTextView.text = date.toString()

        // Open date/time picker
        dateTextView.setOnClickListener {
            pickDateTime(dateTextView)
        }

        // Save
        saveButton.setOnClickListener {

            if (title.text.toString().trim().isBlank()) {
                title.error = "Title is required"
                return@setOnClickListener
            }

            // Date cannot be in the past
            if (date.before(Date())) {
                Toast.makeText(
                    this,
                    "Date cannot be in the past",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val event = Event(
                id = id,
                title = title.text.toString().trim(),
                description = description.text.toString().trim(),
                dateTime = Timestamp(date),
                location = location.text.toString().trim()
            )

            if (id.isBlank()) {
                vm.add(event)
            } else {
                vm.update(event)
            }
           // finish()
        }

        // Delete
        if (id.isBlank()) {
            deleteButton.visibility = android.view.View.GONE
        } else {
            deleteButton.visibility = android.view.View.VISIBLE

            deleteButton.setOnClickListener {
                vm.delete(id)
                finish()
            }
        }

        // Observe ViewModel state
        lifecycleScope.launch {
            vm.state.collect { state ->

                when (state) {

                    is UiState.Success -> {
                       finish()
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@AddEditEventActivity,
                            state.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> {
                        // Loading / Idle
                    }
                }
            }
        }
    }

    private fun pickDateTime(view: TextView) {

        // Use the class-level date
        val calendar = Calendar.getInstance()
        calendar.time = date

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->

                calendar.set(
                    Calendar.YEAR,
                    year
                )

                calendar.set(
                    Calendar.MONTH,
                    month
                )

                calendar.set(
                    Calendar.DAY_OF_MONTH,
                    dayOfMonth
                )

                TimePickerDialog(
                    this,
                    { _, hour, minute ->

                        calendar.set(
                            Calendar.HOUR_OF_DAY,
                            hour
                        )

                        calendar.set(
                            Calendar.MINUTE,
                            minute
                        )

                        calendar.set(
                            Calendar.SECOND,
                            0
                        )

                        calendar.set(
                            Calendar.MILLISECOND,
                            0
                        )

                        // Update class-level date
                        date = calendar.time

                        // Update UI
                        view.text = date.toString()
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // ⭐ Don't allow dates before today
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        // Show calendar
        datePickerDialog.show()
    }
}