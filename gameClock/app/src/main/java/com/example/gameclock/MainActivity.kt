package com.example.gameclock

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gameclock.databinding.ActivityMainBinding

// Global variables that will be available throughout the program.
const val initialTime: Long = 330
var topTimeLeft = initialTime
var bottomTimeLeft = initialTime
const val oneSecondInMilliseconds: Long =  1000
var topClicked = false
var bottomClicked= false

//class to convert time
class TimeConverter(time: Long){
    //minutes and seconds properties
    var minutes = time / 60
    var seconds = time % 60

    //function to convert seconds to minutes and seconds
    fun display(): String
    {
        return "${(minutes).toString().padStart(2, '0')} " + "min : ${(seconds).toString().padStart(2, '0')} sec"
    }//end timeConverter()
}

class MainActivity : AppCompatActivity() {

    //create binding object
    private lateinit var binding: ActivityMainBinding

    // This is a variable to hold a special timer object. We set it here once so that it is
    // available throughout the program. It will be reset when the buttons are pressed though.
    private var topTimer: CountDownTimer = object : CountDownTimer(0, 0) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {}
    }

    private var bottomTimer: CountDownTimer = object : CountDownTimer(0, 0) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        //boilerplate code
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize binding object
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //create variable references to views
        val text: TextView = binding.textView
        val topButton: Button = binding.topButton
        val bottomButton: Button = binding.bottomButton
        val resetButton: Button = binding.resetButton
        val input: EditText = binding.input
        val inputButton: Button = binding.inputButton

        //create instances of time class to convert to minutes and seconds
        var myTime = TimeConverter(initialTime)

        //set initial states for view attributes
        topButton.text = myTime.display()
        bottomButton.text = myTime.display()
        topButton.setBackgroundColor(Color.LTGRAY)
        bottomButton.setBackgroundColor(Color.LTGRAY)

        //code will run when the top button is pressed
        topButton.setOnClickListener {

            //make sure bottom button is enabled
            bottomButton.isEnabled = true
            bottomButton.isClickable = true

            //set up timer that will run when the top button is clicked
            bottomTimer = object :
                CountDownTimer(bottomTimeLeft * oneSecondInMilliseconds, oneSecondInMilliseconds) {
                // This is a special function that is part of the timer. It runs every time the
                // timer's interval passes. In this case, it is set to run every second.
                override fun onTick(millisUntilFinished: Long) {
                    // Update the label to show the remaining time and then remove one second from
                    // the timeLeft variable so that if the timer has to be stopped and started
                    // again, we know how much time to put on the new timer.
                    val bottomTime = TimeConverter((millisUntilFinished / oneSecondInMilliseconds))
                    bottomButton.text = bottomTime.display()
                    bottomTimeLeft--
                }

                // This special function is part of the timer. It runs when the amount of time left
                // in the timer has reached zero and thus the timer is done.
                override fun onFinish() {
                    bottomButton.setBackgroundColor(Color.RED)
                    text.text = getString(R.string.top_button_gameover)
                }
            }

            //update boolean variable
            topClicked = true

            //conditional runs when the top button has been clicked
            if (topClicked) {
                //change colors to show the timer on the bottom clock is running
                topButton.setBackgroundColor(Color.GRAY)
                bottomButton.setBackgroundColor(Color.TRANSPARENT)
                bottomButton.setTextColor(Color.BLACK)

                //disable the top button until the bottom button is clicked
                topButton.isEnabled = false
                topButton.isClickable = false
            }

            //start the timer
            bottomTimer.start()

            //conditional to stop the other timer when this button is clicked
            if (bottomClicked) {
                topTimer.cancel()
                bottomClicked = false
            }

        }

        //we will write the same code for the bottom button
        bottomButton.setOnClickListener {

            //make sure the top button is enabled
            topButton.isEnabled = true
            topButton.isClickable = true

            //set up timer that will run when the bottom button is clicked
            topTimer = object :
                CountDownTimer(topTimeLeft * oneSecondInMilliseconds, oneSecondInMilliseconds) {
                // This is a special function that is part of the timer. It runs every time the
                // timer's interval passes. In this case, it is set to run every second.
                override fun onTick(millisUntilFinished: Long) {
                    // Update the label to show the remaining time and then remove one second from
                    // the timeLeft variable so that if the timer has to be stopped and started
                    // again, we know how much time to put on the new timer.
                    val topTime = TimeConverter((millisUntilFinished / oneSecondInMilliseconds))
                    topButton.text = topTime.display()
                    topTimeLeft--
                }

                // This special function is part of the timer. It runs when the amount of time left
                // in the timer has reached zero and thus the timer is done.
                override fun onFinish() {
                    topButton.setBackgroundColor(Color.RED)
                    text.text = getString(R.string.bottom_button_gameover)
                }
            }
            //update boolean variable
            bottomClicked = true

            //conditional runs when the bottom button has been clicked
            if (bottomClicked) {
                //change colors to show the timer on the bottom clock is running
                bottomButton.setBackgroundColor(Color.GRAY)
                topButton.setBackgroundColor(Color.TRANSPARENT)
                topButton.setTextColor(Color.BLACK)

                //disable the bottom button until the top button is clicked
                bottomButton.isEnabled = false
                bottomButton.isClickable = false
            }

            //start the timer
            topTimer.start()

            //conditional to stop the other timer when this button is clicked
            if (topClicked){
                bottomTimer.cancel()
                topClicked = false
            }
        }

        //code to run when the reset button is clicked
        resetButton.setOnClickListener {
            //cancel both timers to stop them from running
            topTimer.cancel()
            bottomTimer.cancel()

            //reset the time left to the original time and display it in the button text
            topTimeLeft = initialTime
            bottomTimeLeft = initialTime
            val resetTime = TimeConverter(topTimeLeft)
            topButton.text = resetTime.display()
            bottomButton.text = resetTime.display()

            //reset color of each button
            topButton.setBackgroundColor(Color.LTGRAY)
            bottomButton.setBackgroundColor(Color.LTGRAY)

            //ensure both buttons are enabled
            topButton.isEnabled = true
            topButton.isClickable = true
            bottomButton.isEnabled = true
            bottomButton.isClickable = true
        }

        //code will run when the inputButton is pressed
        inputButton.setOnClickListener {
            //cancel both timers to stop them from running
            topTimer.cancel()
            bottomTimer.cancel()

            //initialize variables for user input. Convert to long
            val longInput = input.text.toString().toInt().toLong()

            //reset the time left to the original time and display it in the button text
            val inputTime = TimeConverter(longInput)
            topButton.text = inputTime.display()
            bottomButton.text = inputTime.display()

            //update values for time left
            topTimeLeft = longInput
            bottomTimeLeft = longInput

            //reset color of each button
            topButton.setBackgroundColor(Color.LTGRAY)
            bottomButton.setBackgroundColor(Color.LTGRAY)

            //ensure both buttons are enabled
            topButton.isEnabled = true
            topButton.isClickable = true
            bottomButton.isEnabled = true
            bottomButton.isClickable = true
        }
    }
}