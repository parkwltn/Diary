package com.ussu.memorydiary

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.ussu.memorydiary.API.diaryAPI
import com.ussu.memorydiary.API.gameText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationActivity : AppCompatActivity() {
    private lateinit var CB0: CheckBox
    private lateinit var CB1: CheckBox
    private lateinit var CB2: CheckBox
    private lateinit var CB3: CheckBox
    private lateinit var CB4: CheckBox
    private lateinit var CB5: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val gameText = intent.getStringExtra("gameText")

        var btnWhere = findViewById<Button>(R.id.btnSaveWhere)

        CB0 = findViewById(R.id.CB0)
        CB1 = findViewById(R.id.CB1)
        CB2 = findViewById(R.id.CB2)
        CB3 = findViewById(R.id.CB3)
        CB4 = findViewById(R.id.CB4)
        CB5 = findViewById(R.id.CB5)

        if (gameText != null) {
            val gameTextList = gameText.split(" ") //List
            if (gameTextList[0] != null) {
                CB0.setText(gameTextList[0])
            }
            if (gameTextList[1] != null) {
                CB1.setText(gameTextList[1])
            }
            if (gameTextList[2] != null) {
                CB2.setText(gameTextList[2])
            }
            if (gameTextList[3] != null) {
                CB3.setText(gameTextList[3])
            }
            if (gameTextList[4] != null) {
                CB4.setText(gameTextList[4])
            }
            if (gameTextList[5] != null) {
                CB4.setText(gameTextList[5])
            }

            var locationList = mutableListOf<String>()

            btnWhere.setOnClickListener {
                if (CB0.isChecked) {
                    locationList.add(CB0.getText().toString())
                }
                if (CB1.isChecked) {
                    locationList.add(CB1.getText().toString())
                }
                if (CB2.isChecked) {
                    locationList.add(CB2.getText().toString())
                }
                if (CB3.isChecked) {
                    locationList.add(CB3.getText().toString())
                }
                if (CB4.isChecked) {
                    locationList.add(CB4.getText().toString())
                }
                if (CB5.isChecked) {
                    locationList.add(CB5.getText().toString())
                }

                val BASE_URL = "http://192.168.0.104:8080"
                var locationString = locationList.joinToString(" ")

                var gson = GsonBuilder()
                    .setLenient()
                    .create()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

                val api = retrofit.create(diaryAPI::class.java)
                val callGetGameText = api.getGameText((gameText(locationString)))
                callGetGameText.enqueue(object : Callback<gameText> {
                    override fun onResponse(call: Call<gameText>, response: Response<gameText>) {
                        Toast.makeText(this@LocationActivity, "잘 저장됨!", Toast.LENGTH_LONG).show()
                        Log.d(ContentValues.TAG, "성공: ${response.raw()}")
                    }

                    override fun onFailure(call: Call<gameText>, t: Throwable) {
                        Log.d(ContentValues.TAG, "실패: $t")
                    }
                })
            }
        }
    }
}