package com.dicoding.androidprogramming.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.androidprogramming.capstone.databinding.ActivityDetectedBinding

class DetectedActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_INDEX ="extra_index"
        const val EXTRA_CLASS = "extra_class"
        const val EXTRA_CONFIDENCE = "extra_confidence"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityDetectedBinding = ActivityDetectedBinding.inflate(layoutInflater)
        setContentView(activityDetectedBinding.root)

        val detectedClassIndex = intent.getIntExtra(EXTRA_INDEX, 0)
        val detectedClassName = intent.getStringExtra(EXTRA_CLASS)
        val detectedConfidence = intent.getStringExtra(EXTRA_CONFIDENCE)

        activityDetectedBinding.tvDetectedClass.text = detectedClassName
        activityDetectedBinding.tvConfidence.text = detectedConfidence

        if (detectedClassIndex == 0){
            activityDetectedBinding.tvDescriptionDetail.text = "This is description for cat"
        }
        else if (detectedClassIndex ==1){
            activityDetectedBinding.tvDescriptionDetail.text = "This is description for dog"
        }

    }
}