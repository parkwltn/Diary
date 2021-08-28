package com.ussu.memorydiary

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.gson.GsonBuilder
import com.ussu.memorydiary.API.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Game2Activity : AppCompatActivity() {
    private lateinit var AnswerEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)

        AnswerEditText = findViewById(R.id.editTextTextAnswer)

        //서버에서 질문, 답 가져오기
        val BASE_URL = "http://192.168.0.104:8080"
        val id = intent.getStringExtra("id")
        val date = intent.getStringExtra("date")

        var gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(diaryAPI::class.java)
        val callGetQuestionInfo = api.getAnswer2(questionInfo("$id", "$date", "0", "0", "0", 0))

        callGetQuestionInfo.enqueue(object : Callback<questionInfo> {
            override fun onResponse(call: Call<questionInfo>, response: Response<questionInfo>) {
                Log.d(ContentValues.TAG, "성공: ${response.raw()}")
                if (response.body() != null) {
                    var getAnswer = response.body()!!.answer
                    var score = response.body()!!.score
                    var btnAnswer = findViewById<Button>(R.id.btnCheckAnswer)

                    if (response.body()!!.answer == "99") {
                        var gameText = response.body()!!.game_text
                        var intent = Intent(this@Game2Activity, LocationActivity::class.java)
                        intent.putExtra("date", "$date")
                        intent.putExtra("id", "$id")
                        intent.putExtra("gameText", "$gameText")
                        intent.putExtra("score", score)
                        startActivity(intent)
                    } else {
                        btnAnswer.setOnClickListener {
                            //답 입력받기
                            var answer = AnswerEditText.text.toString()

                            //답 비교
                            if (answer == getAnswer) { //정답
                                Toast.makeText(this@Game2Activity, "정답입니다!", Toast.LENGTH_LONG).show()
                                score = score + 1
                                val BASE_URL = "http://192.168.0.104:8080"

                                var gson = GsonBuilder()
                                    .setLenient()
                                    .create()

                                val retrofit = Retrofit.Builder()
                                    .baseUrl(BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build()

                                val api = retrofit.create(memberAPI::class.java)
                                val callSaveScore = api.saveScore(memberInfo("$id", "0", score))

                                callSaveScore.enqueue(object : Callback<memberInfo> {
                                    override fun onResponse(call: Call<memberInfo>, response: Response<memberInfo>) {
                                    }
                                    override fun onFailure(call: Call<memberInfo>, t: Throwable) {
                                        Log.d(ContentValues.TAG, "실패: $t")
                                    }
                                })
                            } else {
                                Toast.makeText(
                                    this@Game2Activity, "오답입니다! 다시 생각해보세요", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } else {
                    AnswerEditText.setText("작성한 일기가 없습니다.")
                }

            }

            override fun onFailure(call: Call<questionInfo>, t: Throwable) {
                Log.d(ContentValues.TAG, "실패: $t")
            }
        })
    }
}