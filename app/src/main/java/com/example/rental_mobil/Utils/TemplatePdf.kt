package com.example.rental_mobil.Utils
// Invoice preview drawn using canvas
import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.rental_mobil.Model.Car
import com.example.rental_mobil.Model.Customer
import com.example.rental_mobil.Model.History
import com.example.rental_mobil.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class TemplatePdf @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint()
    private val textPaint = Paint()
    private val contentBgPaint = Paint()
    private val titleBgPaint = Paint()
    private var currencySymbol: String
    private var yMargin = 10f
    private var xMargin = 10f
    private var xPadding = 5f.dpToPx()
    private var yPadding = 5f.dpToPx()
    private var textSpacing = 5f
    private var yPointer = 0f
    private var dataCustomer: Customer? = null
    private var dataCar: Car? = null
    private var dataHistory: History? = null
    init {
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        textPaint.color = ContextCompat.getColor(context, R.color.black)
        textPaint.isAntiAlias = true
        textPaint.style = Paint.Style.FILL_AND_STROKE
        contentBgPaint.color = ContextCompat.getColor(context, R.color.purple_200)
        contentBgPaint.isAntiAlias = true
        contentBgPaint.style = Paint.Style.FILL_AND_STROKE
        titleBgPaint.color = ContextCompat.getColor(context, R.color.purple_500)
        titleBgPaint.isAntiAlias = true
        titleBgPaint.style = Paint.Style.FILL_AND_STROKE
        currencySymbol = "Rp"
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawHeader(canvas)
            drawPelangganInfo(canvas, dataCustomer)
            drawDue(canvas, dataHistory)
            drawClientInfo(canvas, dataCar,dataHistory)
            drawItemHeader(canvas)
            drawItems(canvas, dataCar,dataHistory)
            drawTotal(canvas, dataHistory)
            save()
            restore()
        }
    }
    private fun drawHeader(canvas: Canvas?, title: String = "LAPORAN") {
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.white))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))
        textAlignCentre(textPaint)
        yPointer = 60f
        val rect =
            RectF(
                xMargin.dpToPx(),
                yMargin.dpToPx(),
                width.toFloat() - xMargin.dpToPx(),
                yPointer.dpToPx()
            )
        canvas?.drawRect(rect, titleBgPaint)
        canvas?.drawText(title, 0, title.length, rect.centerX(), rect.centerY() + 15, textPaint)
    }
    private fun drawHeaderTable(canvas: Canvas?, title: String = "LAPORAN") {
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.white))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))
        textAlignCentre(textPaint)
        val width = canvas!!.width

        yPointer = 60f
        val rect =
            RectF(
                xMargin.dpToPx(),
                yMargin.dpToPx(),
                canvas?.width!!.toFloat() - xMargin.dpToPx(),
                yPointer.dpToPx()
            )
        canvas?.drawRect(rect, titleBgPaint)
        canvas?.drawText(title, 0, title.length, rect.centerX(), rect.centerY() + 15, textPaint)
    }
    private fun drawPelangganInfo(
        canvas: Canvas?, data: Customer?
    ) {
        setPaintColor(contentBgPaint, getColor(R.color.white))
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL))
        textAlignLeft(textPaint)
        val top = yPointer + yMargin
        val bottom = top + 65
        yPointer = bottom
        val rect = RectF(
            xMargin.dpToPx(),
            top.dpToPx(),
            width.toFloat() - xMargin.dpToPx(),
            bottom.dpToPx()
        )
        canvas?.drawRect(rect, contentBgPaint)
        val textBounds = Rect()
        textPaint.getTextBounds(data!!.nama, 0, data.nama.length, textBounds)
        val yBound = textBounds.height().toFloat() + (textSpacing + 2).dpToPx()
        val left = xMargin + xPadding
        val textY = rect.top + yPadding
        canvas?.drawText(
            data.nama,
            0,
            data.nama.length,
            left.dpToPx(),
            textY + (yBound),
            textPaint
        )
        canvas?.drawText(
            data.hp,
            0,
            data.hp.length,
            left.dpToPx(),
            textY + (yBound * 2),
            textPaint
        )
        canvas?.drawLine(
            left.dpToPx(),
            yPointer.dpToPx(),
            width.toFloat() - xMargin.dpToPx(),
            yPointer.dpToPx(),
            textPaint
        )
    }
    private fun drawDue(canvas: Canvas?, data: History?) {
        setPaintColor(contentBgPaint, getColor(R.color.white))
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))
        val top = yPointer + yMargin
        val bottom = top + 35
        yPointer = bottom
        val rect = RectF(
            xMargin.dpToPx(),
            top.dpToPx(),
            width.toFloat() - xMargin.dpToPx(),
            bottom.dpToPx()
        )
        canvas?.drawRect(rect, contentBgPaint)
        val textBounds = Rect()
        val invoiceDate = data?.created
        textPaint.getTextBounds(invoiceDate!!, 0, invoiceDate.length, textBounds)
        val yBound = textBounds.height().toFloat() + textSpacing.dpToPx()
        val textY = rect.top + yPadding
        val invoiceNumber: String = "#INV" + Int.MAX_VALUE
        val left = xMargin + xPadding
        canvas?.drawText(
            invoiceNumber,
            0,
            invoiceNumber.length,
            left.dpToPx(),
            textY + yBound,
            textPaint
        )
        textAlignRight(textPaint)
        val date = "Date: $invoiceDate"
        canvas?.drawText(
            date,
            0,
            date.length,
            width.toFloat() - xMargin.dpToPx(),
            textY + yBound,
            textPaint
        )
        canvas?.drawLine(
            left.dpToPx(),
            yPointer.dpToPx(),
            width.toFloat() - xMargin.dpToPx(),
            yPointer.dpToPx(),
            textPaint
        )
    }
    private fun drawClientInfo(
        canvas: Canvas?, data: Car?, dataHistory:History?
    ) {
        setPaintColor(contentBgPaint, getColor(R.color.white))
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC))
        textAlignLeft(textPaint)
        val top = yPointer + yMargin
        val bottom = top + 110
        yPointer = bottom
        val rect = RectF(
            xMargin.dpToPx(),
            top.dpToPx(),
            width.toFloat() - xMargin.dpToPx(),
            bottom.dpToPx()
        )
        canvas?.drawRect(rect, contentBgPaint)
        val textBounds = Rect()
        textPaint.getTextBounds(data?.merk, 0, data?.merk!!.length, textBounds)
        val yBound = textBounds.height().toFloat() + (textSpacing + 2).dpToPx()
        val left = xMargin + xPadding
        val textY = rect.top + yPadding
        canvas?.drawText(
            "Info Mobil",
            0,
            "Info Mobil".length,
            left.dpToPx(),
            textY + yBound,
            textPaint
        )
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL))
        canvas?.drawText(
            data!!.merk,
            0,
            data.merk.length,
            left.dpToPx(),
            textY + (yBound * 2),
            textPaint
        )
        canvas?.drawText(
            data!!.plat,
            0,
            data.plat.length,
            left.dpToPx(),
            textY + (yBound * 3),
            textPaint
        )
        canvas?.drawText(
            "Mulai Rental:${dataHistory!!.mulai_rental}",
            0,
            "Mulai Rental:${dataHistory!!.mulai_rental}".length,
            left.dpToPx(),
            textY + (yBound * 4),
            textPaint
        )

        canvas?.drawText(
            "Selesai Rental:${dataHistory!!.selesai_rental}",
            0,
            "Selesai Rental:${dataHistory!!.selesai_rental}".length,
            left.dpToPx(),
            textY + (yBound * 5),
            textPaint
        )
    }
    private fun drawItemHeader(canvas: Canvas?) {
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.white))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))
        textAlignLeft(textPaint)
        val top = yPointer + yMargin
        val bottom = top + 40
        val right = width.toFloat()
        yPointer = bottom
        val rect = RectF(xMargin.dpToPx(), top.dpToPx(), right - xMargin.dpToPx(), bottom.dpToPx())

        canvas?.drawRect(rect, titleBgPaint)
        val nama_penyewa="Nama penyewa"
        val no_hp="No HP"
        val plat_mobil="Plat mobil"
        val merk_mobil="Merk mobil"
        val mulai_rental="Sewa"
        val selesai_rental="Kembali"
        val harga_sewa="Harga Sewa"
        val harga_denda="Denda"
        val total="Total"
//        val price = resources.getString(R.string.price)
        val headerTopMargin = 10f

        canvas?.drawText(
            harga_sewa,
            0,
            harga_sewa.length,
            xMargin * 2.dpToPx(),
            rect.centerY() + headerTopMargin,
            textPaint
        )
        textAlignRight(textPaint)

        canvas?.drawText(
            harga_denda,
            0,
            harga_denda.length,
            (width / 2).toFloat(),
            rect.centerY() + headerTopMargin,
            textPaint
        )
    }


    private fun drawItemHeaderTablePelangganDanMobil(canvas: Canvas?) {
        setPaintColor(contentBgPaint,getColor(R.color.white))
        titleBgPaint.strokeWidth = 5f
        titleBgPaint.style = Paint.Style.STROKE
        titleBgPaint.color = Color.BLACK
        titleBgPaint.strokeCap = Paint.Cap.SQUARE

        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))
        textAlignLeft(textPaint)
        val top = yPointer + yMargin
        val bottom = top + 40
        val right = canvas?.width!!.toFloat()
        val width = canvas!!.width

        yPointer = bottom
        val rect = RectF(xMargin.dpToPx(), top.dpToPx(), right - xMargin.dpToPx(), bottom.dpToPx())

        canvas?.drawRect(rect, titleBgPaint)
        val nama_penyewa="Nama penyewa"
        val plat_mobil="Plat mobil"
        val mulai_rental="Sewa"
        val selesai_rental="Kembali"
        val harga_sewa="Harga Sewa"
        val harga_denda="Denda"
        val total="Total"
        val headerTopMargin = 10f

        canvas?.drawText(
            nama_penyewa,
            0,
            nama_penyewa.length,
            xMargin * 2.dpToPx(),
            rect.centerY() + headerTopMargin,
            textPaint
        )
        textAlignRight(textPaint)

        canvas?.drawText(
            plat_mobil,
            0,
            plat_mobil.length,
            (width / 2).toFloat()+60,
            rect.centerY() + headerTopMargin,
            textPaint
        )

         canvas?.drawText(
             harga_sewa,
            0,
             harga_sewa.length,
             width.toFloat() - xMargin * 2.dpToPx(),
            rect.centerY() + headerTopMargin,
            textPaint
        )
    }

    private fun drawItemHeaderTable(canvas: Canvas?) {
        titleBgPaint.strokeWidth = 5f
        titleBgPaint.style = Paint.Style.STROKE
        titleBgPaint.color = Color.BLACK
        titleBgPaint.strokeCap = Paint.Cap.SQUARE

        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))
        textAlignLeft(textPaint)
        val top = yPointer + yMargin
        val bottom = top + 40
        val right = canvas?.width!!.toFloat()
        val width = canvas!!.width

        yPointer = bottom
        val rect = RectF(xMargin.dpToPx(), top.dpToPx(), right - xMargin.dpToPx(), bottom.dpToPx())

        canvas?.drawRect(rect, titleBgPaint)
        val nama_penyewa="Nama penyewa"
        val no_hp="No HP"
        val plat_mobil="Plat mobil"
        val merk_mobil="Merk mobil"
        val mulai_rental="Sewa"
        val selesai_rental="Kembali"
        val harga_sewa="Harga Sewa"
        val harga_denda="Denda"
        val total="Total"
        val headerTopMargin = 10f

        canvas?.drawText(
            mulai_rental,
            0,
            mulai_rental.length,
            xMargin * 2.dpToPx(),
            rect.centerY() + headerTopMargin,
            textPaint
        )
        textAlignRight(textPaint)

        canvas?.drawText(
            selesai_rental,
            0,
            selesai_rental.length,
            (width / 2).toFloat(),
            rect.centerY() + headerTopMargin,
            textPaint
        )

        canvas?.drawText(
            total,
            0,
            total.length,
            width.toFloat() - xMargin * 2.dpToPx(),
            rect.centerY() + headerTopMargin,
            textPaint
        )
    }

    private fun drawItemsTableRental(canvas: Canvas?, dataHistory: MutableList<History>) {
        contentBgPaint.strokeWidth = 5f
        contentBgPaint.style = Paint.Style.STROKE
        contentBgPaint.color = Color.BLACK
        contentBgPaint.strokeCap = Paint.Cap.SQUARE

        setPaintColor(contentBgPaint, Color.BLACK)
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL))
        textAlignLeft(textPaint)
        val textBounds = Rect()
        textPaint.getTextBounds("Kembali", 0, "Kembali".length, textBounds)
        val xBound = textBounds.width().toFloat()
        val yBound = textBounds.height().toFloat() + (textSpacing + 2).dpToPx()
        val top = yPointer + yMargin
        val bottom = top + calculateNormalizedTableHeight(dataHistory.size, yBound.toInt())
        val right = canvas?.width!!.toFloat()- xMargin.dpToPx()
        val width = canvas!!.width
        yPointer = bottom
        val rect = RectF(xMargin.dpToPx(), top.dpToPx(), right, bottom.dpToPx())
        canvas?.drawRect(rect, contentBgPaint)
        val textY = rect.top + yPadding
        var yBoundFactor = 1
        var index =0
        val stkPaint = Paint()
        stkPaint.style = Paint.Style.STROKE
        stkPaint.strokeWidth = 8f
        stkPaint.color = Color.WHITE

        for (item in dataHistory) {
            textAlignLeft(textPaint)
            canvas?.drawText(
                item.mulai_rental,
                0,
                item.mulai_rental.length,
                xMargin * 2.dpToPx(),
                textY + (yBound * yBoundFactor),
                textPaint
            )

            textAlignRight(textPaint)
            val selesai_rental = item.selesai_rental
            canvas?.drawText(
                selesai_rental,
                0,
                selesai_rental.length,
                (width / 2) + (xBound+8),
                textY + (yBound * yBoundFactor),
                textPaint
            )
            val price: String = currencySymbol + item.total
            canvas?.drawText(
                price,
                0,
                price.length,
                width.toFloat() - xMargin * 2.dpToPx(),
                textY + (yBound * yBoundFactor),
                textPaint
            )
            yBoundFactor++
            index++
        }

    }

    private fun drawItemHeaderDenda(canvas: Canvas?) {
        titleBgPaint.strokeWidth = 5f
        titleBgPaint.style = Paint.Style.STROKE
        titleBgPaint.color = Color.BLACK
        titleBgPaint.strokeCap = Paint.Cap.SQUARE

        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))
        textAlignLeft(textPaint)
        val top = yPointer + yMargin
        val bottom = top + 40
        val right = (canvas?.width!!.toFloat()/4)
        val width = (canvas!!.width/4)

        yPointer = bottom
        val rect = RectF(xMargin.dpToPx(), top.dpToPx(), right+xMargin*2, bottom.dpToPx())

        canvas?.drawRect(rect, titleBgPaint)
        val harga_denda="Denda"
        val total="Total"
        val headerTopMargin = 10f

        canvas?.drawText(
            harga_denda,
            0,
            harga_denda.length,
            xMargin * 2.dpToPx(),
            rect.centerY() + headerTopMargin,
            textPaint
        )

    }

    private fun drawItemsDenda(canvas: Canvas?, dataHistory: MutableList<History>) {
        contentBgPaint.strokeWidth = 5f
        contentBgPaint.style = Paint.Style.STROKE
        contentBgPaint.color = Color.BLACK
        contentBgPaint.strokeCap = Paint.Cap.SQUARE

        setPaintColor(contentBgPaint, Color.BLACK)
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL))
        textAlignLeft(textPaint)
        val textBounds = Rect()
        textPaint.getTextBounds("Denda", 0, "Denda".length, textBounds)
        val xBound = textBounds.width().toFloat()
        val yBound = textBounds.height().toFloat() + (textSpacing + 2).dpToPx()
        val top = yPointer + yMargin
        val bottom = top + calculateNormalizedTableHeight(dataHistory.size, yBound.toInt())
        val right = canvas?.width!!.toFloat()
        val width = canvas!!.width
        yPointer = bottom
        val rect = RectF(xMargin.dpToPx(), top.dpToPx(), (right/4) + xMargin.dpToPx(), bottom.dpToPx())
        canvas?.drawRect(rect, contentBgPaint)
        val textY = rect.top + yPadding
        var yBoundFactor = 1
        var index =0
        for (item in dataHistory) {
            textAlignLeft(textPaint)
            val denda= if(item.denda.isEmpty()) "0" else item.denda
            canvas?.drawText(
                "RM $denda",
                0,
                "RM $denda".length,
                xMargin * 2.dpToPx(),
                textY + (yBound * yBoundFactor),
                textPaint
            )
            yBoundFactor++
            index++
        }

    }
    private fun drawItemsTableMobilDanPelanggan(canvas: Canvas?, dataCustomer:MutableList<Customer>, dataCar:MutableList<Car>) {
        contentBgPaint.strokeWidth = 5f
        contentBgPaint.style = Paint.Style.STROKE
        contentBgPaint.color = Color.BLACK
        contentBgPaint.strokeCap = Paint.Cap.SQUARE

        setPaintColor(contentBgPaint, Color.BLACK)
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL))
        textAlignLeft(textPaint)
        val textBounds = Rect()
        textPaint.getTextBounds("Plat Mobil", 0, "Plat Mobil".length, textBounds)
        val xBound = textBounds.width().toFloat()
        val yBound = textBounds.height().toFloat() + (textSpacing + 2).dpToPx()
        val top = yPointer + yMargin
        val bottom = top + calculateNormalizedTableHeight(dataCustomer.size, yBound.toInt())
        val right = canvas?.width!!.toFloat()- xMargin.dpToPx()
        val width = canvas!!.width
        yPointer = bottom
        val rect = RectF(xMargin.dpToPx(), top.dpToPx(), right, bottom.dpToPx())
        canvas?.drawRect(rect, contentBgPaint)
        val textY = rect.top + yPadding
        var yBoundFactor = 1
        var index =0
        for (item in dataCustomer) {
            textAlignLeft(textPaint)
            canvas?.drawText(
                item.nama,
                0,
                item.nama.length,
                xMargin * 2.dpToPx(),
                textY + (yBound * yBoundFactor),
                textPaint
            )
            textAlignRight(textPaint)

            canvas?.drawText(
                dataCar[index].plat,
                0,
                dataCar[index].plat.length,
                (width / 2) + (xBound-xMargin*8),
                textY + (yBound * yBoundFactor),
                textPaint
            )

            val price: String = currencySymbol + dataCar[index].harga
            canvas?.drawText(
                price,
                0,
                price.length,
                width.toFloat() - xMargin * 2.dpToPx(),
                textY + (yBound * yBoundFactor),
                textPaint
            )
            yBoundFactor++
            index++
        }

    }


    private fun drawItems(canvas: Canvas?, dataCar: Car?, data: History?) {
        setPaintColor(contentBgPaint, getColor(androidx.appcompat.R.color.material_grey_100))
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL))
        textAlignLeft(textPaint)
        val textBounds = Rect()
        textPaint.getTextBounds(data!!.waktu_denda, 0, data.waktu_denda.length, textBounds)
        val xBound = textBounds.width().toFloat()
        val yBound = textBounds.height().toFloat() + (textSpacing + 2).dpToPx()
        val top = yPointer + yMargin
        val bottom = top + calculateNormalizedTableHeight(1, yBound.toInt())
        val right = width.toFloat()
        yPointer = bottom
        val rect = RectF(xMargin.dpToPx(), top.dpToPx(), right - xMargin.dpToPx(), bottom.dpToPx())
        canvas?.drawRect(rect, contentBgPaint)
        val textY = rect.top + yPadding
        var yBoundFactor = 1
        textAlignLeft(textPaint)
        canvas?.drawText(
            "RM ${dataCar!!.harga}",
            0,
            "RM ${dataCar.harga}".length,
            xMargin * 2.dpToPx(),
            textY + (yBound * yBoundFactor),
            textPaint
        )
        textAlignRight(textPaint)
        val denda = data!!.waktu_denda
        canvas?.drawText(
            denda,
            0,
            denda.length,
            (width / 2) + xBound,
            textY + (yBound * yBoundFactor),
            textPaint
        )

    }
    private fun drawTotal(canvas: Canvas?, data: History?) {
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))
        textAlignRight(textPaint)
        val total = "Total  " + currencySymbol + data!!.total
        val top = yPointer + yMargin * 3
        val bottom = top + 50
        yPointer = bottom
        canvas?.drawText(
            total,
            0,
            total.length,
            width.toFloat() - (xMargin * 2).dpToPx(),
            top.dpToPx(),
            textPaint
        )
    }
    private fun drawTotalTable(canvas: Canvas?, data: MutableList<History>) {
        setTextSize(textPaint, 16f)
        setPaintColor(textPaint, getColor(R.color.black))
        setTypFace(textPaint, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))
        textAlignRight(textPaint)
        var total =0
        for (item in data) {
            total+=item.total.toInt()
        }
        val totalText = "Total Keseluruhan: $currencySymbol$total"
        val top = yPointer + yMargin * 3
        val textBounds = Rect()
        textPaint.getTextBounds(totalText, 0, totalText.length, textBounds)
        val yBound = textBounds.height().toFloat() + (textSpacing + 2).dpToPx()
        val bottom = top + calculateNormalizedTableHeight(data.size, yBound.toInt())
        yPointer = bottom
        canvas?.drawText(
            totalText,
            0,
            totalText.length,
            canvas.width.toFloat() - (xMargin * 2).dpToPx(),
            top.dpToPx(),
            textPaint
        )
    }

    private fun calculateNormalizedTableHeight(size: Int, textBoundHeight: Int): Int {
        return size * textBoundHeight
    }
    private fun getColor(colorId: Int) = ContextCompat.getColor(context, colorId)
    private fun setTextSize(paint: Paint, size: Float) {
        paint.textSize = size.spToPx()
    }
    private fun setPaintColor(paint: Paint, color: Int) {
        paint.color = color
    }
    private fun setTypFace(paint: Paint, typeface: Typeface) {
        paint.typeface = typeface
    }
    private fun textAlignCentre(paint: Paint) {
        paint.textAlign = Paint.Align.CENTER
    }
    private fun textAlignLeft(paint: Paint) {
        paint.textAlign = Paint.Align.LEFT
    }
    private fun textAlignRight(paint: Paint) {
        paint.textAlign = Paint.Align.RIGHT
    }
    private fun Float.dpToPx() = this * resources.displayMetrics.density
    private fun Int.dpToPx() = (this * resources.displayMetrics.density).toInt()
    private fun Float.spToPx() = this * resources.displayMetrics.scaledDensity
    private fun Int.pxToDp() = (this * 160) / resources.displayMetrics.densityDpi
    fun setData(dataCustomer: Customer?, dataHistory: History?, dataCar: Car?) {
        this.dataCustomer = dataCustomer
        this.dataHistory = dataHistory
        this.dataCar = dataCar
    }
    fun setBackgroundPaint(templateId: Int?) {
        when (templateId) {
            1 -> titleBgPaint.color = ContextCompat.getColor(context, R.color.purple_200)
//            2 -> titleBgPaint.color = ContextCompat.getColor(context, R.color.blue_template)
//            3 -> titleBgPaint.color = ContextCompat.getColor(context, R.color.crimson_template)
//            4 -> titleBgPaint.color = ContextCompat.getColor(context, R.color.teak_template)
//            else -> {
//                titleBgPaint.color = ContextCompat.getColor(context, R.color.teak_template)
//            }
        }
    }


    fun generatePdf(dataCustomer: Customer? = null,
                    dataCar: Car? = null,
                    dataHistory: History? = null, file: File, width:Int, height:Int) {
        val pdfDocument = PdfDocument()
        val page = pdfDocument.startPage(pageInfo(width, height, 2))
        designPage(page, pdfDocument, dataCustomer, dataCar, dataHistory, file)
    }
    fun generateTablePdf(dataCustomer: MutableList<Customer>,
                         dataCar: MutableList<Car>,
                         dataHistory: MutableList<History>, file: File, width:Int, height:Int) {
        val pdfDocument = PdfDocument()
        val page = pdfDocument.startPage(pageInfo(width, height, 2))
        designPageTable(page, pdfDocument, dataCustomer, dataCar, dataHistory, file)
    }

    private fun pageInfo(pageWidth: Int, pageHeight: Int, pageNumber: Int): PdfDocument.PageInfo {
        return PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
    }
    // drawing in pdf page canvas. Reuse the functions used in
// Simple template. Note: Remove dpToPx() while drawing in PDF page // canvas.
    private fun designPage(
        page: PdfDocument.Page,
        pdfDocument: PdfDocument, dataCustomer: Customer? = null,
        dataCar: Car? = null,
        dataHistory: History? = null,
        file: File
    ) {
        val canvas: Canvas = page.canvas

        setBackgroundPaint(1)
        drawHeader(canvas)
        drawPelangganInfo(canvas, dataCustomer)
        drawDue(canvas, dataHistory)
        drawClientInfo(canvas, dataCar,dataHistory)
        drawItemHeader(canvas)
        drawItems(canvas, dataCar,dataHistory)
        drawTotal(canvas, dataHistory)
        pdfDocument.finishPage(page)
        writeContentToFile(file, pdfDocument)
    }
    private fun designPageTable(
        page: PdfDocument.Page,
        pdfDocument: PdfDocument, dataCustomer: MutableList<Customer>,
        dataCar: MutableList<Car>,
        dataHistory: MutableList<History>,
        file: File
    ) {
        val canvas: Canvas = page.canvas

        setBackgroundPaint(1)
        drawHeaderTable(canvas)
        drawItemHeaderTablePelangganDanMobil(canvas)
        drawItemsTableMobilDanPelanggan(canvas, dataCustomer, dataCar)
        drawItemHeaderTable(canvas)
        drawItemsTableRental(canvas, dataHistory)
        drawItemHeaderDenda(canvas)
        drawItemsDenda(canvas, dataHistory)
        drawTotalTable(canvas, dataHistory)

        pdfDocument.finishPage(page)
        writeContentToFile(file, pdfDocument)
    }

    // save the file as PDF in local storage
    private fun writeContentToFile(file: File, pdfDocument: PdfDocument) {
        val fos = FileOutputStream(file)
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(fos)

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(context, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (e:IOException) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        pdfDocument.close()
    }

}