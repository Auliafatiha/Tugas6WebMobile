package com.example.twittermbdb

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PostinganInput : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_postingan)

        val database = DBPostingan(this)
        val postingview = findViewById<PostinganInputView>(R.id.postinganInputView)
        postingview.onPostSaved = {isi ->
            val result = database.insertpost(isi)
            if (result != -1L){
                Toast.makeText(this, "Postingan disimpan", Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(this, "Gagal menyimpan!", Toast.LENGTH_SHORT).show()

        }
        postingview.requestFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(postingview, InputMethodManager.SHOW_IMPLICIT)




//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        val view = currentFocus
        if (view is PostinganInputView){
            return view.dispatchKeyEvent(event)
        }
        return super.dispatchKeyEvent(event)
    }
}