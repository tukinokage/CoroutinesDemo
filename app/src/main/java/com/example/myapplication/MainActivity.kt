package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var submitBtn: Button;
    private lateinit var timeTextView: TextView;
    private lateinit var job: Job;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        submitBtn = findViewById(R.id.text_activity_submit)
        timeTextView = findViewById(R.id.textactivity_textview)
        initListener()
    }

    fun initListener(){
        if(::submitBtn.isInitialized){
            submitBtn.setOnClickListener {
                if(::job.isInitialized){
                    job.cancel();
                    //在子线程，但是这个时候协程不一定结束。再调用join方法，这里表示阻塞等待协程结束。
                    //job.cancel();
                }

                job = GlobalScope.launch(Dispatchers.Main) {
                    var maxTime = 60;
                    var curTime = 0;

                    withContext(Dispatchers.IO){

                        while (curTime < maxTime){
                            delay(1000L)
                            Log.d("test2:", curTime.toString())
                            curTime ++
                            refreshTextView(curTime)

                            /* //不开启新协程
                             withContext(Dispatchers.Main){
                                 Log.d("test1:", curTime.toString())
                                 timeTextView.text = curTime.toString() + "s"
                             }*/
                        }
                    }

                    withContext(Dispatchers.Main){
                        Log.d("test3:", curTime.toString())
                        timeTextView.text = "暂停"
                    }

                }
            }
        }

    }

    //挂起
    suspend fun refreshTextView(curTime: Int) =  withContext(Dispatchers.Main){
        Log.d("test1:", curTime.toString())
        timeTextView.text = curTime.toString() + "s"
    }
}
