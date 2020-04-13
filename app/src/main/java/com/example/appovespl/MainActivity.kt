package com.example.appovespl

import android.annotation.TargetApi
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
//import com.android.volley.Request
//import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

import java.io.IOException;
import java.net.URL
//import androidx.core.app.ComponentActivity
//import androidx.core.app.ComponentActivity.ExtraData
//import androidx.core.content.ContextCompat.getSystemService
//import android.icu.lang.UCharacter.GraphemeClusterBreak.T
//import androidx.core.app.ComponentActivity
//import androidx.core.app.ComponentActivity.ExtraData
//import androidx.core.content.ContextCompat.getSystemService
//import android.icu.lang.UCharacter.GraphemeClusterBreak.T
//import android.R.string
//import androidx.core.app.ComponentActivity
//import androidx.core.app.ComponentActivity.ExtraData
//import androidx.core.content.ContextCompat.getSystemService
//import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Looper
//import androidx.core.app.ComponentActivity
//import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible


class MainActivity : AppCompatActivity() {

    // Declaring auth
    private lateinit var auth: FirebaseAuth
    var progressBar:ProgressBar? = null
    // For storing custom token
    private var customToken: String? = null
//    private lateinit var tokenReceiver: TokenBroadcastReceiver


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportActionBar != null)
            supportActionBar?.hide()
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar!!.visibility = View.GONE
//        tokenReceiver = object : TokenBroadcastReceiver() {
//            override fun onNewToken(token: String?) {
//                Log.d(TAG, "onNewToken:$token")
//                setCustomToken(token.toString())
//            }
//        }

        Log.i("LOG","Start Application")

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener{
            btnLogin.isClickable = false
            val nik = inputNik.text.toString()
            val password = inputPassword.text.toString()
            Log.i("LOG","PRESSED")

            if(nik.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please Insert NIK and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                runOnUiThread {
                    progressBar!!.visibility = View.VISIBLE
                    progressBar!!.translationZ = 1.0f
                }
                APICall(nik,password)
            }
        }
    }

    fun signIn(){
        customToken?.let {
            auth.signInWithCustomToken(it)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        println("Success")
                        Log.d(TAG, "signInWithCustomToken:success")
                        val user = auth.currentUser
                        runOnUiThread {
                            progressBar!!.visibility = View.GONE
                            btnLogin.isClickable = true
                        }
                        Toast.makeText(baseContext, "Successfully Logged In",
                            Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        intent.putExtra("nik",inputNik.text.toString())
                        startActivity(intent)
                    } else {
                        println("False")
                        Log.w(TAG, "signInWithCustomToken:failure", task.exception)

                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        runOnUiThread{
                            progressBar!!.visibility = View.GONE
                            btnLogin.isClickable = true
                        }
                    }
                }
        }
    }

    fun toastInvalid(){
        Toast.makeText(baseContext, "Invalid Credentials", Toast.LENGTH_SHORT).show()
    }

    fun APICall(nik: String,pwd : String) {
        println("TEST")

//            val url = "https://hrindomaret.com/api/getsqlsrvTest"
        val json = "application/json; charset=utf-8".toMediaTypeOrNull()

        val url = "https://hrindomaret.com/api/login"

        val request = Request.Builder().url(url).build()

        val cred = JSONObject()
        cred.put("emplid",nik)
        cred.put("password",pwd)

        val formbody2 = cred.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val formBody = FormBody.Builder()
            .add("nik",nik)
//                .add("password",pwd)
            .build()

        val post = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        val post2 = Request.Builder()
            .url(url)
            .post(formbody2)
            .build()


        val client = OkHttpClient()
//            client.newCall(request).enqueue(object: Callback{
//                override fun onResponse(call: Call, response: Response) {
//                    val resp = response.body?.string()
//
//                    println(resp)
//                }
//
//                override fun onFailure(call: Call, e: IOException) {
//                    println("FAIL")
//                }
//            })

        client.newCall(post2).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val resp = response.body?.string()
                println(resp)
                if(JSONObject(resp).has("error")){
                    Handler(Looper.getMainLooper()).post(Runnable {
                        Toast.makeText(
                            baseContext,
                            "Invalid Credentials",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                    runOnUiThread{
                        progressBar!!.visibility = View.GONE
                        btnLogin.isClickable = true
                    }
                } else {
                    val jresponse = JSONObject(resp)
                    val token = jresponse.getString("token")
                    customToken = token
                    println(resp)
                    signIn()
//                        runOnUiThread{
//                            btnLogin.isClickable = true
//                            progressBar!!.visibility = View.GONE
//                            Toast.makeText(
//                                baseContext,
//                                "Please check your internet connection",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
                }
//                    val jsonArray = JSONArray(resp)
//                    val jsonObject: JSONObject = jsonArray.getJSONObject(0)
//                    val token = jsonObject.get("token").toString()


            }


            override fun onFailure(call: Call, e: IOException) {
                println("FAIL")
            }
        })


    }





//        var LoginButton = findViewById(R.layout.activity_main) as Button


    companion object {
        private const val TAG = "CustomAuthActivity"
    }

}


