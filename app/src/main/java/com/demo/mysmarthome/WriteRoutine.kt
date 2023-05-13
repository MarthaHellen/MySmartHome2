package com.demo.mysmarthome

import Database.DatabaseHandler
import Models.RoutineModel
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class WriteRoutine : AppCompatActivity(){
    private lateinit var sharedPreferences: SharedPreferences //helps us keep state
    private lateinit var routineNameET: EditText
    private val SHARED_PREFS_KEY = "MySharedPreferences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_routine)

        val addActionButton = findViewById<FloatingActionButton>(R.id.button)
        val addEventButton = findViewById<FloatingActionButton>(R.id.AddEvent)
        routineNameET  = findViewById(R.id.RoutineName)
        sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
       // Share or retrieve persistent data

        // Getting the various shared preferences on page load
        getTimeSharedPreferences()
        getRoutineSharedPreferences()
        getNotificationsSharedPreferences()

//setting on click listeners to the various buttons
        addActionButton.setOnClickListener { addAction() }
        addEventButton.setOnClickListener { addEvent() }

        //input dialogue
        val notificationCreated = intent.getBooleanExtra("notificationCreated", false)
       if (notificationCreated) {
           showInputDialog()
           intent.putExtra("notificationCreated", false) // set notificationCreated value to false
       }

        //getting data from selectevent activity to display time
        val timeSet = intent.getBooleanExtra("showTimePicker", false)
        if (timeSet) {
            showTimePickerDialog()
            intent.putExtra("showTimePicker", false) // set timeSet value to false
        }
    }
    private fun getNotificationsSharedPreferences() {
        val notificationText = sharedPreferences.getString("NotificationPrefs", null)
        if (notificationText != null) {
            val actionRowLayout = layoutInflater.inflate(R.layout.updatednotification, null)
            actionRowLayout.findViewById<TextView>(R.id.tv_AddNotification).text = "Send Notification: $notificationText"
            val container = findViewById<ViewGroup>(R.id.actionsTVContainer)
            val actionsTV = findViewById<TextView>(R.id.actionsTV)
            val index = container.indexOfChild(actionsTV)
            container.removeView(actionsTV)
            container.addView(actionRowLayout, index)
            actionRowLayout.id = R.id.actionsTV
        }
    }

    private fun getRoutineSharedPreferences() {
        val routine = sharedPreferences.getString("routineName", null)
        routineNameET.setText(routine)
    }

    private fun getTimeSharedPreferences() {
        val timeText = sharedPreferences.getString("TimePrefs", null)
        if (timeText != null) {
            val eventRowLayout = layoutInflater.inflate(R.layout.time_set, null)
            val selectedTimeTVContainer = findViewById<ViewGroup>(R.id.selectedTime)
            val selectedTimeTV = findViewById<TextView>(R.id.selected)
            val index = selectedTimeTVContainer.indexOfChild(selectedTimeTV)
            selectedTimeTVContainer.removeView(selectedTimeTV)
            selectedTimeTVContainer.addView(eventRowLayout, index)
            eventRowLayout.id = R.id.selected
            findViewById<TextView>(R.id.TimeHere).text = "The time is $timeText"
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.edit().clear().apply()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = calendar.get(Calendar.AM_PM)

        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val hourText = if (selectedHour == 0 || selectedHour == 12) "12" else (selectedHour % 12).toString()
                val minuteText = if (selectedMinute < 10) "0$selectedMinute" else "$selectedMinute"
                val amPmText = if (amPm == Calendar.AM) "AM" else "PM"
                val timeText = "$hourText:$minuteText $amPmText"

                // Inflate the LinearLayout resource file
                val eventRowLayout = layoutInflater.inflate(R.layout.time_set, null)
                // Update the text of the TextView in the LinearLayout
                // eventRowLayout.findViewById<TextView>(R.id.tv_AddTime).text = "The time is $timeText"
                // Replace the TextView with id @+id/selectedTimeTV with the inflated LinearLayout
                val selectedTimeTVContainer = findViewById<ViewGroup>(R.id.selectedTime)
                val selectedTimeTV = findViewById<TextView>(R.id.selected)
                val index = selectedTimeTVContainer.indexOfChild(selectedTimeTV)
                selectedTimeTVContainer.removeView(selectedTimeTV)
                selectedTimeTVContainer.addView(eventRowLayout, index)
                eventRowLayout.id = R.id.selected // Set the ID of the inflated LinearLayout to the ID of the TextView that was removed

                // Change the text in the text view with id @+id/tv_AddTime
                findViewById<TextView>(R.id.TimeHere).text = "The time is $timeText"


                // save the value to shared preferences
                val editor = sharedPreferences.edit()
                editor.putString("TimePrefs", timeText)
                editor.apply()


            }, hour, minute, false
        )

        timePickerDialog.show()
    }

    //buttons and click listeners
    private fun addEvent() {
        sharedPreferences.edit().putString("routineName", routineNameET.text.toString()).apply()
        val intent = Intent(this, SelectEvent::class.java)
        startActivity(intent)
    }

    private fun addAction() {
        val intent = Intent(this, SelectThing::class.java)
        startActivity(intent)
    }

    private fun showInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter a value")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            val inputText = input.text.toString()

            val actionRowLayout = layoutInflater.inflate(R.layout.updatednotification, null)
            actionRowLayout.findViewById<TextView>(R.id.tv_AddNotification).text = "Send Notification: $inputText"
            val container = findViewById<ViewGroup>(R.id.actionsTVContainer)
            val actionsTV = findViewById<TextView>(R.id.actionsTV)
            val index = container.indexOfChild(actionsTV)
            container.removeView(actionsTV)
            container.addView(actionRowLayout, index)
            actionRowLayout.id = R.id.actionsTV


            // save the value to shared preferences
            val editor = sharedPreferences.edit()
            editor.putString("NotificationPrefs", inputText)
            editor.apply()
//            container.removeAllViews()
//            container.addView(actionRowLayout)

            showProcessingDialog(input.text.toString())

        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showProcessingDialog(value: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Creating new routine")
        builder.setCancelable(false)

        val progressBar = ProgressBar(this)
        builder.setView(progressBar)

        val dialog = builder.create()
        dialog.show()

        //TODO: Replace this with the logic to create a new routine using the entered value
//        Handler().postDelayed({
//            dialog.dismiss()
//        }, 3000)

        addRoutineRecord()
    }

    private fun addRoutineRecord() {
        val routine = sharedPreferences.getString("routineName", null)
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(routine?.isNotEmpty() == true){
            val status = databaseHandler.addRoutine(RoutineModel(0, routine,"Never"))
            if (status > -1){
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                sharedPreferences.edit().clear().apply()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("SELECTED_FRAGMENT", "routines")
                startActivity(intent)
            }
        }else{
            Toast.makeText(applicationContext, "Error creating routine", Toast.LENGTH_LONG).show()
        }
    }





}

////variables for the shared preferances
//    private lateinit var editTextInput: EditText
//    private lateinit var sharedPreferences: SharedPreferences
//   private val SHARED_PREFS_KEY = "MySharedPreferences"
//
//
//    // Declare your EditText and LinearLayout variables
// //   private lateinit var routineEditText: EditText
//  //  private lateinit var linearLayout: LinearLayout
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//
//        val notificationCreated = intent.getBooleanExtra("notificationCreated", false)
//        if (notificationCreated) {
//            showInputDialog()
//            intent.putExtra("notificationCreated", false) // set notificationCreated value to false
//        }
//
//
//        val showTimePicker = intent.getBooleanExtra("showTimePicker", false)
//
//        // Display time picker dialog if necessary
//        if (showTimePicker) {
//            showTimePickerDialog()
//        }
//
//
//        sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
//
//
//        // Getting the various shared preferences on page load
//        getTimeSharedPreferences()
//        getRoutineSharedPreferences()
//        getNotificationsSharedPreferences()
//
//        setContentView(R.layout.activity_write_routine)
//// Find the EditText by its ID
//       // editTextInput = findViewById(R.id.RoutineName)
//
//// Get the SharedPreferences instance
//      //  sharedPreferences = getSharedPreferences( SHARED_PREFS_KEY, Context.MODE_PRIVATE)
//
//// Load the saved text from Shared Preferences and set it in the EditText
//     //   val savedText = sharedPreferences.getString("Greeting", "")
//     //   editTextInput.setText(savedText)
//
//
//
//        // Find the button in the write activity layout
//        val addEventButton= findViewById<FloatingActionButton>(R.id.AddEvent)
//
//        // Set a click listener on the button
//        addEventButton.setOnClickListener {
//            // Create an Intent to navigate to the select_event layout
//            val intent = Intent(this,SelectEvent::class.java)
//            startActivity(intent)
//        }
//
//        val addActionButton= findViewById<FloatingActionButton>(R.id.button)
//
//        addActionButton.setOnClickListener {
//            // Create an Intent to navigate to the select_event layout
//            val intent = Intent(this,SelectThing::class.java)
//            startActivity(intent)
//        }
//
//        // Find the EditText and LinearLayout in the activity layout
//      //  routineEditText = findViewById(R.id.RoutineName)
//      //  linearLayout = findViewById(R.id.visibal)
//// Set a text change listener on the EditText
//      //  routineEditText.addTextChangedListener(object : TextWatcher {
//           // override fun afterTextChanged(s: Editable?) {
//                // Update the visibility of the LinearLayout based on whether EditText has text or not
//           //     if (s?.isNotBlank() == true) {
//               //     linearLayout.visibility = View.VISIBLE
//               // } else {
//                //    linearLayout.visibility = View.INVISIBLE
//             //   }
//         //   }
//
//          //  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // No action needed
//         //   }
//
//         //   override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // No action needed
//         //   }
//      //  })
//
//
//    }
//
//
//   // override fun onPause() {
//    //    super.onPause()
//
//        // Save the text in Shared Preferences when the activity is paused
//    //    val editor = sharedPreferences.edit()
//    //    editor.putString("Greeting", editTextInput.text.toString())
//     //   editor.apply()
//  //  }
//
//    //shared prefernces for all the data being input
//
//    private fun getNotificationsSharedPreferences() {
//        val notificationText = sharedPreferences.getString("NotificationPrefs", null)
//        if (notificationText != null) {
//            val actionRowLayout = layoutInflater.inflate(R.layout.updatednotification, null)
//            actionRowLayout.findViewById<TextView>(R.id.tv_AddNotification).text = "Send Notification: $notificationText"
//            val container = findViewById<ViewGroup>(R.id.actionsTVContainer)
//            val actionsTV = findViewById<TextView>(R.id.actionsTV)
//            val index = container.indexOfChild(actionsTV)
//            container.removeView(actionsTV)
//            container.addView(actionRowLayout, index)
//            actionRowLayout.id = R.id.actionsTV
//        }
//    }
//
//    private fun getRoutineSharedPreferences() {
//        val routine = sharedPreferences.getString("routineName", null)
//        editTextInput.setText(routine)
//    }
//
//    private fun getTimeSharedPreferences() {
//        val timeText = sharedPreferences.getString("TimePrefs", null)
//        if (timeText != null) {
//            val eventRowLayout = layoutInflater.inflate(R.layout.time_set, null)
//            val selectedTimeTVContainer = findViewById<ViewGroup>(R.id.selectedTime)
//            val selectedTimeTV = findViewById<TextView>(R.id.selected)
//            val index = selectedTimeTVContainer.indexOfChild(selectedTimeTV)
//            selectedTimeTVContainer.removeView(selectedTimeTV)
//            selectedTimeTVContainer.addView(eventRowLayout, index)
//            eventRowLayout.id = R.id.selected
//            findViewById<TextView>(R.id.TimeHere).text = "The time is $timeText"
//        }
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        sharedPreferences.edit().clear().apply()
//    }
//
//
//
//    private fun showTimePickerDialog() {
//        // Create a new instance of TimePickerDialog
//        val timePickerDialog = TimePickerDialog(
//            this, this, 0, 0, false) // You can customize the initial time here
//
//        // Show the time picker dialog
//        timePickerDialog.show()
//    }
//    // Implement the onTimeSet() method from TimePickerDialog.OnTimeSetListener
//    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//        // Handle the selected time, e.g. update the TextView with the selected time
//        val selectedTime = "$hourOfDay:$minute"
//        val timeSetLayout = layoutInflater.inflate(R.layout.time_set, null)
//        val selectedLinearLayout = findViewById<ViewGroup>(R.id.selectedTime)
//        val selectedTimeTextView = findViewById<TextView>(R.id.selected)
//        val index = selectedLinearLayout.indexOfChild( selectedTimeTextView)
//        selectedLinearLayout.removeView(selectedTimeTextView)
//        selectedLinearLayout.addView(timeSetLayout, index)
//        timeSetLayout.id = R.id.selected // Set the ID of the inflated LinearLayout to the ID of the TextView that was removed
//
//        // Change the text in the text view with id @+id/tv_AddTime
//        findViewById<TextView>(R.id.TimeHere).text = "The time is $selectedTime"
//
//
//
//
//
//    }
//
//
//
//    private fun showInputDialog() {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Enter a value")
//
//        val input = EditText(this)
//        builder.setView(input)
//
//        builder.setPositiveButton("OK") { dialog, _ ->
//            dialog.dismiss()
//            val inputText = input.text.toString()
//
//            val actionRowLayout = layoutInflater.inflate(R.layout.updatednotification, null)
//            actionRowLayout.findViewById<TextView>(R.id.tv_AddNotification).text = "Send Notification: $inputText"
//            val container = findViewById<ViewGroup>(R.id.actionsTVContainer)
//            val actionsTV = findViewById<TextView>(R.id.actionsTV)
//            val index = container.indexOfChild(actionsTV)
//            container.removeView(actionsTV)
//            container.addView(actionRowLayout, index)
//            actionRowLayout.id = R.id.actionsTV
//
//
//            // save the value to shared preferences
//            val editor = sharedPreferences.edit()
//            editor.putString("NotificationPrefs", inputText)
//            editor.apply()
//
//
//            showProcessingDialog(input.text.toString())
//
//        }
//
//        builder.setNegativeButton("Cancel") { dialog, _ ->
//            dialog.cancel()
//        }
//
//        builder.show()
//    }
//
//    private fun showProcessingDialog(value: String) {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Creating new routine")
//        builder.setCancelable(false)
//
//        val progressBar = ProgressBar(this)
//        builder.setView(progressBar)
//
//        val dialog = builder.create()
//        dialog.show()
//
//        //TODO: Replace this with the logic to create a new routine using the entered value
//
//
//        addRoutineRecord()
//    }
//
//    private fun addRoutineRecord() {
//        val routine = sharedPreferences.getString("routineName", null)
//        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
//        if(routine?.isNotEmpty() == true){
//            val status = databaseHandler.addRoutine(RoutineModel(0, routine,"Never"))
//            if (status > -1){
//                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
//                sharedPreferences.edit().clear().apply()
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("SELECTED_FRAGMENT", "routines")
//                startActivity(intent)
//            }
//        }else{
//            Toast.makeText(applicationContext, "Error creating routine", Toast.LENGTH_LONG).show()
//        }
//    }
   // }
