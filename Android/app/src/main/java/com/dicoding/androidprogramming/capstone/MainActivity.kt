package com.dicoding.androidprogramming.capstone

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dicoding.androidprogramming.capstone.databinding.ActivityMainBinding
import com.dicoding.androidprogramming.capstone.ml.ConvertedModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class MainActivity : AppCompatActivity() {
    lateinit var imgView : ImageView
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        imgView = activityMainBinding.imageView2

        val labels = application.assets.open("label.txt").bufferedReader().use { it.readText() }.split("\n")

        activityMainBinding.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 100)
        }

        activityMainBinding.button2.setOnClickListener {
            val resized = Bitmap.createScaledBitmap(bitmap, 224 * 4, 224, true)

            val model = ConvertedModel.newInstance(this)

            val tbuffer = TensorImage.fromBitmap(resized)
            val byteBuffer = tbuffer.buffer

            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(byteBuffer)


            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val max = getMax(outputFeature0.floatArray)

            val confidence = outputFeature0.floatArray[max].toString()

            val intent = Intent(this@MainActivity, DetectedActivity::class.java)
            intent.putExtra(DetectedActivity.EXTRA_INDEX, max)
            intent.putExtra(DetectedActivity.EXTRA_CLASS, labels[max])
            intent.putExtra(DetectedActivity.EXTRA_CONFIDENCE, confidence)
            startActivity(intent)

            model.close()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Glide.with(this).load(data?.data).into(imgView)

        val uri: Uri? = data?.data

        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
    }

    fun getMax(arr:FloatArray) : Int{
        var ind = 0
        var min = 0.0f

        for(i in 0..1)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i
            }
        }
        return ind
    }
}