package com.afifemwe.bookinggedung.ui.creategedung

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.databinding.ActivityCreateGedungBinding
import com.afifemwe.bookinggedung.model.GeneralResponse
import com.afifemwe.bookinggedung.ui.main.customer.CustomerMainActivity
import com.afifemwe.bookinggedung.ui.main.pemilik.PemilikMainActivity
import com.afifemwe.bookinggedung.utils.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.checkbox.MaterialCheckBox
import retrofit2.Call
import retrofit2.Response
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CreateGedungActivity : AppCompatActivity() {

    private lateinit var bind: ActivityCreateGedungBinding

    private var listFasilitas: MutableList<String> = mutableListOf()
    private var listJamOperasional: MutableList<String> = mutableListOf()

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private var photoName = ""

    val service = Api.getApi()!!.create(ApiInterfaces::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_create_gedung)

        createMode()
    }

    private fun createMode() {

        bind.apply {
            btnUploadGambar.setOnClickListener {
                var progressDialog = ProgressDialog(this@CreateGedungActivity)
                progressDialog.setMessage("Memproses Photo ... ")
                progressDialog.show()

                ImagePicker.with(this@CreateGedungActivity)
                    .crop()
                    .start { resultCode, data ->
                        if (resultCode == Activity.RESULT_OK) {
                            ivGambarGedung.setImageURI(data?.data)

                            // Upload Photo To Server
                            val file = ImagePicker.getFile(data)
                            if (file?.path != null) {
                                val fileNameSegment: List<String> = file.path.split("/")
                                photoName = fileNameSegment[fileNameSegment.size - 1]
                                photoName =
                                    """Photo_Gedung_${Calendar.getInstance().timeInMillis}.jpg"""

                                if (NetworkUtility.isInternetAvailable(this@CreateGedungActivity)) {
                                    executor.execute {
                                        val uploadPhoto = UploadPhotoToServer()

                                        Log.i("File Path", file.path.toString())
                                        uploadPhoto.upload(file.path, photoName)
                                        uploadPhoto.setOnPhotoUploadCallback(object : OnUploadCallback {
                                            override fun onSuccessUpload() {
                                                progressDialog.dismiss()

                                                handler.post {

                                                    Toast.makeText(
                                                        this@CreateGedungActivity,
                                                        "Succes Upload Photo",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
//                                        Toast.makeText(this@CreateSurveyActivity, "Success Test", Toast.LENGTH_SHORT).show()
                                                }

                                            }

                                            override fun onFailedUpload() {
                                                progressDialog.dismiss()

                                                handler.post {
                                                    Toast.makeText(
                                                        this@CreateGedungActivity,
                                                        "Upload Failed",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                            }
                                        })
                                    }
                                } else {
                                    NetworkUtility.checkYourConnection(this@CreateGedungActivity)
                                }
                            }
                        }
                    }
            }

            btnSimpan.setOnClickListener {
                simpanData()

//                checkCBFasilitas()
//                Log.i("Fasilitas", listFasilitas.toString())
            }
        }

    }

    private fun simpanData() {
        bind.apply {
            ValidateInput.apply {
                validate(etNamaGedung)
                validate(etKapasitas)
                validate(etHarga)
                validate(etLinkMaps)
            }

            if (ValidateInput.validate(etNamaGedung) && ValidateInput.validate(etKapasitas) && ValidateInput.validate(etHarga) && ValidateInput.validate(etLinkMaps)) {
                val namaGedung  = etNamaGedung.text.toString()
                val kapasitas   = etKapasitas.text.toString()
                val harga       = etHarga.text.toString()
                val linkMaps    = etLinkMaps.text.toString()

                val userPref    = UserPreference(this@CreateGedungActivity)
                val pemilik     = userPref.getUsername()

                checkCBFasilitas()
                checkCBJamOperasional()

                var listFasilitasString = listFasilitas.toString().replace("[", "")
                listFasilitasString = listFasilitasString.toString().replace("]", "")

                var listJamOperasionalString    = listJamOperasional.toString().replace("[", "")
                listJamOperasionalString = listJamOperasionalString.toString().replace("]", "")

                if(NetworkUtility.isInternetAvailable(this@CreateGedungActivity)) {
                    try {
                        val call = service.buatGedungBaru(
                            nama            = namaGedung,
                            gambar          = photoName,
                            kapasitas       = kapasitas,
                            fasilitas       = listFasilitasString,
                            jam_operasional = listJamOperasionalString,
                            maps            = linkMaps,
                            harga           = harga,
                            pemilik         = pemilik.toString()
                        )

                        call.enqueue(object: retrofit2.Callback<GeneralResponse> {
                            override fun onResponse(
                                call: Call<GeneralResponse>,
                                response: Response<GeneralResponse>
                            ) {
                                if (response.body()?.status == "success") {
                                    val i = Intent(this@CreateGedungActivity, PemilikMainActivity::class.java)
                                    startActivity(i)
                                    finish()
                                } else {
                                    Toast.makeText(this@CreateGedungActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                                NetworkUtility.checkYourConnection(this@CreateGedungActivity)
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                        NetworkUtility.checkYourConnection(this@CreateGedungActivity)
                    }
                } else {
                    NetworkUtility.checkYourConnection(this@CreateGedungActivity)
                }
            }
        }

    }

    private fun checkCBFasilitas() {
        bind.apply {
            listFasilitas.clear()

            val listCb = mutableListOf<MaterialCheckBox>(cbFs1, cbFs2, cbFs3, cbFs4, cbFs5)

            for (itemCbFs in listCb) {
                if (itemCbFs.isChecked) {
                    listFasilitas.add(itemCbFs.text.toString().toLowerCase())
                }
            }

            Log.i("CbFS", listFasilitas.toString())
        }

    }
    private fun checkCBJamOperasional() {
        bind.apply {
            listJamOperasional.clear()

            val listCb = mutableListOf<MaterialCheckBox>(cbTime1, cbTime2, cbTime3, cbTime4, cbTime5,
                cbTime6, cbTime7, cbTime8, cbTime9, cbTime10,
                cbTime11, cbTime12, cbTime13, cbTime14)

            for (itemCbTime in listCb) {
                if (itemCbTime.isChecked) {
                    listJamOperasional.add(itemCbTime.text.toString().toLowerCase())
                }
            }

            Log.i("CbTime", listJamOperasional.toString())
        }

    }

}