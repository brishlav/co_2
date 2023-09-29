package com.example.co_2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val REQUEST_CODE = 1
private val returnDates = mutableMapOf<Int, String>()

class MainActivity : AppCompatActivity() {

    private var currentIndex = 0
    private var userCredits = 100 // Initialize user credits
    private val isBookedMap = mutableMapOf<Int, Boolean>()
    private val rentalBooks = listOf(
        RentalBook("Harry Potter and the Philosopher's Stone", 4.5f, "Fiction", 5, R.drawable.book1),
        RentalBook("Harry Potter and the Deathly Hallows", 3.5f, "Action", 10, R.drawable.book2),
        RentalBook("Harry Potter and the Prisoner of Azkaban", 4.1f, "Action", 15, R.drawable.book3),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nextButton: Button = findViewById(R.id.nextButton)
        val borrowButton: Button = findViewById(R.id.borrowButton)
        val creditsTextView: TextView = findViewById(R.id.credits)

        creditsTextView.text = "Credits: $userCredits"

        currentIndex = intent.getIntExtra("currentIndex", 0)
        showBook()

        nextButton.setOnClickListener {
            val nextIndex = (currentIndex + 1) % rentalBooks.size
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("currentIndex", nextIndex)
            startActivity(intent)
            finish()
        }

        borrowButton.setOnClickListener {
            val book = rentalBooks[currentIndex]
            if (isBookedMap[currentIndex] == true || userCredits < book.pricePerDay) {
                Toast.makeText(this, "Cannot borrow the book. Not enough credits or already booked.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, BorrowActivity::class.java)
                intent.putExtra("rentalBook", book)
                startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }

    private fun showBook() {
        val book = rentalBooks[currentIndex]
        val bookNameTextView: TextView = findViewById(R.id.bookNameTextView)
        val bookImageView: ImageView = findViewById(R.id.bookImageView)
        val bookRatingBar: RatingBar = findViewById(R.id.bookRatingBar)
        val bookGenreTextView: TextView = findViewById(R.id.bookGenreTextView)
        val bookPriceTextView: TextView = findViewById(R.id.bookPriceTextView)
        val borrowButton: Button = findViewById(R.id.borrowButton)

        bookNameTextView.text = "${book.name}"
        bookImageView.setImageResource(book.imageResId)
        bookRatingBar.rating = book.rating
        bookGenreTextView.text = "Genre: ${book.genre}"
        bookPriceTextView.text = "${book.pricePerDay} credits per day"
        returnDates[currentIndex]?.let { returnDate ->
            borrowButton.text = "Booked till $returnDate"
            borrowButton.isEnabled = false
        } ?: run {
            borrowButton.text = "Borrow"
            borrowButton.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val creditsTextView: TextView = findViewById(R.id.credits)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val returnDate = data?.getStringExtra("returnDate")
            val selectedDays = data?.getIntExtra("selectedDays", 0) ?: 0
            if (returnDate != null) {
                returnDates[currentIndex] = returnDate
                isBookedMap[currentIndex] = true
                userCredits -= selectedDays * rentalBooks[currentIndex].pricePerDay // Deduct the user's credits
                creditsTextView.text = "Credits: $userCredits"
                showBook()
            }
        }
    }
}
