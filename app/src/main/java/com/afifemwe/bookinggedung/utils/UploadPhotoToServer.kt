package com.afifemwe.bookinggedung.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.model.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

class UploadPhotoToServer() {
    var encodedString: String? = null
    var urlPhoto: String? = null

    lateinit var onUploadCallback: OnUploadCallback

    fun setOnPhotoUploadCallback(onUploadCallback: OnUploadCallback) {
        this.onUploadCallback = onUploadCallback
    }


    //when upload button is clicked
    fun upload(imgPath: String?, fileName: String? ) {
        //when image is selected from gallery
        if (imgPath != null && imgPath.isNotEmpty()) {
            //convert image to string using base64
            encodeImageToString(imgPath, fileName)
            //when image is not selected from gallery
        } else {
            Log.e("Error", "You must select image from gallery before you try to upload")
        }
    }


    private fun encodeImageToString(imgPath: String?, fileName: String?) {

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            try {
                val options: BitmapFactory.Options = BitmapFactory.Options()
                options.inSampleSize = 3
                val bitmap = BitmapFactory.decodeFile(imgPath, options)
                val stream = ByteArrayOutputStream()
                // Must compress the image to reduce image size to make upload easey
                bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream)
                val byte_arr = stream.toByteArray()

                //encode image to stringx
                encodedString = Base64.encodeToString(byte_arr, 0)
                handler.post {
                    makeHTTPCallUpload(fileName)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    //make http call to upload image to php server
    private fun makeHTTPCallUpload(fileName: String?) {
        var status: Boolean? = null
        val service: ApiInterfaces = Api.getApi()!!.create(ApiInterfaces::class.java)
        //uploadImage();
        val call: Call<GeneralResponse> = service.uploadPhoto(encodedString, fileName)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                Log.d("UPLOAD", response.toString())

                onUploadCallback.onSuccessUpload()
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                Log.d("UPLOAD GAGAL", t.toString())

                onUploadCallback.onFailedUpload()
            }
        })
    }
}

interface OnUploadCallback{
    fun onSuccessUpload()
    fun onFailedUpload()
}