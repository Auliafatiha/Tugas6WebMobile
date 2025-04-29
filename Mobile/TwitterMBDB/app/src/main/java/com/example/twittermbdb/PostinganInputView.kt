package com.example.twittermbdb

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View

class PostinganInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    val inputfield = "teksPostingan"
    private var teksPostingan: String = ""
    private val tombolPostRect = RectF()
    var onPostSaved: ((String) -> Unit)? = null

    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
    }

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 60f
    }

    private val paintButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
    }

    private val paintButtonText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 50f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("TES_VIEW", "onDraw dijalankan - width: $width, height: $height")


        // Background merah
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)

        // Tampilkan teks postingan
        canvas.drawText(teksPostingan, 50f, 200f, paintText)

        // Gambar tombol POST
        val btnLeft = width - 350f
        val btnTop = 50f
        val btnRight = width - 100f
        val btnBottom = 150f
        tombolPostRect.set(btnLeft, btnTop, btnRight, btnBottom)

        canvas.drawRoundRect(tombolPostRect, 30f, 30f, paintButton)
        val textX = (btnLeft + btnRight) / 2
        val textY = (btnTop + btnBottom) / 2 - (paintButtonText.descent() + paintButtonText.ascent()) / 2
        canvas.drawText("POST", textX, textY, paintButtonText)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val c = event?.unicodeChar ?: 0
        if (c != 0) {
            teksPostingan += c.toChar()
            invalidate()
            return true
        } else if (keyCode == KeyEvent.KEYCODE_DEL && teksPostingan.isNotEmpty()) {
            teksPostingan = teksPostingan.dropLast(1)
            invalidate()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (tombolPostRect.contains(event.x, event.y)) {
                onPostSaved?.invoke(teksPostingan)
                teksPostingan = ""
                invalidate()
                return true
            }
        }
        return true
    }
}
