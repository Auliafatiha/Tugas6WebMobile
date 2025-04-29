package com.example.twitterclone

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.twittermbdb.R


class OutputPostingan(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    val paintdasar = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL

    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tabPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val paintpost = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL

    }

    private val button = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE

    }
    private val paintteksupgrade = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 24f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private val ButtonAdd = Rect()

    val mail = ContextCompat.getDrawable(context, R.drawable.mail)
    val search = ContextCompat.getDrawable(context, R.drawable.search)
    val notif = ContextCompat.getDrawable(context, R.drawable.bell)
    val home = ContextCompat.getDrawable(context, R.drawable.home)
    val group = ContextCompat.getDrawable(context, R.drawable.group)
    val poto = ContextCompat.getDrawable(context, R.drawable.wanita)
    val add = ContextCompat.getDrawable(context, R.drawable.add)
    val twitter = ContextCompat.getDrawable(context, R.drawable.twitter)


    init {
        titlePaint.apply {
            textSize = 50f
            typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            textAlign = Paint.Align.LEFT
        }

        tabPaint.apply {
            textSize = 35f
            typeface = Typeface.DEFAULT
        }


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.drawRect(0f,0f,width - 0f,height -0f,paintdasar)


        // Header
        paint.color = Color.BLACK
        canvas.drawRect(0f, 0f, width.toFloat(), 150f, paint)

        canvas.save()
        val sizeikon = 30f
        val iconx = 200f
        val icony = 180f
        val iconright = iconx + sizeikon
        val iconbottom = icony + sizeikon
        val radiusprofil = 80f

        val pathprofil = Path()
        pathprofil.reset()
        pathprofil.addCircle(iconx,icony,radiusprofil,Path.Direction.CW)
        canvas.clipPath(pathprofil)

        poto?.let {
            it.setBounds((iconx - radiusprofil).toInt(), (icony - radiusprofil).toInt(), (iconright + radiusprofil).toInt(), (iconbottom + radiusprofil).toInt())
            it.draw(canvas)
        }
        canvas.restore()

        val jarakikon = (width/3f)-50f
        val ikonsize = 70f
        val twtx = iconbottom + jarakikon
        val twty = icony
        twitter?.setBounds(twtx.toInt(),twty.toInt(),(twtx + ikonsize).toInt(),(twty + ikonsize).toInt())
        twitter?.draw(canvas)

        val buttonpanjang = 160f
        val buttonlebar = 80f

        val buttonleft = (twtx + jarakikon)
        val buttontop = icony
        val buttonright = buttonleft + buttonpanjang
        val buttonbottom = buttontop + buttonlebar


        val buttonrect = RectF(buttonleft,buttontop,buttonright,buttonbottom)
        canvas?.drawRoundRect(buttonrect,30f,30f,button)

        val textleft = (buttonleft + buttonright) / 2
        val texttop = (buttontop + buttonbottom) / 2f - (paintteksupgrade.descent() + paintteksupgrade.ascent()) / 2f
        canvas.drawText("Upgrade",textleft,texttop,paintteksupgrade)


        // "For You" dan "Following"
        tabPaint.color = Color.LTGRAY
        canvas.drawText("For you", 110f, (iconbottom + 200f), tabPaint)
        tabPaint.color = Color.DKGRAY
        canvas.drawText("Following", 800f, (iconbottom + 200f), tabPaint)

        // Tab indicator (biru untuk aktif)
        paint.color = Color.BLUE
        canvas.drawRect(90f, (iconbottom + 210f), 250f, (iconbottom + 215f), paint)

        // Konten Kosong
        paintpost.color = Color.WHITE
        paintpost.textSize = 40f
        paintpost.textAlign = Paint.Align.CENTER
        canvas.drawText("Belum ada postingan", width / 2f, height / 2f, paintpost)


        // Bottom Navigation
        val spacing = width / 5

        val lebarikon = 80
        val homex = 100f
        val homey = height - 130f
        home?.setBounds(homex.toInt(),homey.toInt(),(homex + lebarikon).toInt(),(homey + lebarikon).toInt())
        home?.draw(canvas)

        val searchx = homex + spacing
        search?.setBounds(searchx.toInt(),homey.toInt(),(searchx + lebarikon).toInt(),(homey + lebarikon).toInt())
        search?.draw(canvas)

        val notifx = searchx + spacing
        notif?.setBounds(notifx.toInt(),homey.toInt(),(notifx + lebarikon).toInt(),(homey + lebarikon).toInt())
        notif?.draw(canvas)

        val mailx = notifx + spacing
        mail?.setBounds(mailx.toInt(),homey.toInt(),(mailx + lebarikon).toInt(),(homey + lebarikon).toInt())
        mail?.draw(canvas)

        val groupx = mailx + spacing
        group?.setBounds(groupx.toInt(),homey.toInt(),(groupx + lebarikon).toInt(),(homey + lebarikon).toInt())
        group?.draw(canvas)

        val sizeadd = 120f
        val addx = mailx + 100f
        val addy = homey - 220f
        val addright = addx + sizeadd
        val addbottom = addy + sizeadd
        add?.setBounds(addx.toInt(),addy.toInt(),(addx + sizeadd).toInt(),(addy + sizeadd).toInt())
        add?.draw(canvas)

        ButtonAdd.set(addx.toInt(), addy.toInt(), addright.toInt(), addbottom.toInt())


    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Tambahkan interaksi jika perlu (contoh: pindah tab, klik ikon)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x.toInt()
                val y = event.y.toInt()
//                if (ButtonAdd.contains(x, y)){
//                    val intent = Intent(context, PostActivity::class.java)
//                    context.startActivity(intent)
//
//                }


            }

        }

        return true
    }
}
