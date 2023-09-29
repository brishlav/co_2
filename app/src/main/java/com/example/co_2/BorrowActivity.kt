package com.example.co_2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
class BorrowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrow)

        val book: RentalBook? = intent.getParcelableExtra("rentalBook")
        val daysSeekBar: SeekBar = findViewById(R.id.daysSeekBar)
        val totalPriceTextView: TextView = findViewById(R.id.totalPriceTextView)
        val saveButton: Button = findViewById(R.id.saveButton)
        val cancelButton: Button = findViewById(R.id.cancelButton) // New Cancel Button
        val borrowBookImageView: ImageView = findViewById(R.id.borrowBookImage)
        val borrowBookNameTextView: TextView = findViewById(R.id.borrowBookNameTextView)
        var userCredits = 100
        book?.let {
            borrowBookNameTextView.text = "Title: ${it.name}"
            borrowBookImageView.setImageResource(it.imageResId)


            daysSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val totalPrice = progress * book.pricePerDay // Assuming book is non-null here
                    totalPriceTextView.text = "Total Price: $totalPrice credits for $progress days"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })


            saveButton.setOnClickListener {
                val selectedDays = daysSeekBar.progress
                if (selectedDays == 0) {
                    Toast.makeText(this, "Please select the number of days.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Return from the click listener without proceeding further
                }

                val returnDate = calculateReturnDate(selectedDays)
                val totalPrice = selectedDays * book.pricePerDay // Assuming book is non-null here

                if (totalPrice > userCredits) {
                    Toast.makeText(this, "Not enough credits.", Toast.LENGTH_SHORT).show()
                } else {
                    val returnIntent = Intent()
                    returnIntent.putExtra("selectedDays", selectedDays)
                    returnIntent.putExtra("returnDate", returnDate)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }


            cancelButton.setOnClickListener {
                finish() // Finish the activity and return to the previous screen without saving any changes
            }
        }
    }

    private fun calculateReturnDate(selectedDays: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, selectedDays)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
