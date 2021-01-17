import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import java.util.*

class QueueLinearFloodFiller {

    var image: Bitmap? = null
        protected set
    var tolerance = intArrayOf(0, 0, 0)
    protected var width = 0
    protected var height = 0
    protected var pixels: IntArray? = null
    var fillColor = 0
    protected var startColor = intArrayOf(0, 0, 0)
    protected lateinit var pixelsChecked: BooleanArray
    protected lateinit var ranges: Queue<FloodFillRange>

    constructor(img: Bitmap) {
        copyImage(img)
    }

    constructor(img: Bitmap, targetColor: Int, newColor: Int) {
        useImage(img)

        fillColor = newColor
        setTargetColor(targetColor)
    }

    fun setTargetColor(targetColor: Int) {
        startColor[0] = Color.red(targetColor)
        startColor[1] = Color.green(targetColor)
        startColor[2] = Color.blue(targetColor)
    }

    fun setTolerance(value: Int) {
        tolerance = intArrayOf(value, value, value)
    }

    fun copyImage(img: Bitmap) {
        width = img.width
        height = img.height

        image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(image!!)
        canvas.drawBitmap(img, 0.toFloat(), 0.toFloat(), null)

        pixels = IntArray(width * height)

        image!!.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1)
    }

    fun useImage(img: Bitmap) {
        width = img.width
        height = img.height
        image = img

        pixels = IntArray(width * height)

        image!!.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1)
    }

    protected fun prepare() {
        pixelsChecked = BooleanArray(pixels!!.size)
        ranges = LinkedList<FloodFillRange>()
    }

    fun floodFill(x: Int, y: Int) {
        prepare()

        if (startColor[0] == 0) {
            val startPixel = pixels!![width * y + x]
            startColor[0] = startPixel shr 16 and 0xff
            startColor[1] = startPixel shr 8 and 0xff
            startColor[2] = startPixel and 0xff
        }

        LinearFill(x, y)

        var range: FloodFillRange

        while (ranges.size > 0) {
            range = ranges.remove()

            var downPxIdx = width * (range.Y + 1) + range.startX
            var upPxIdx = width * (range.Y - 1) + range.startX
            val upY = range.Y - 1
            val downY = range.Y + 1

            for (i in range.startX..range.endX) {

                if (range.Y > 0 && !pixelsChecked[upPxIdx]
                    && CheckPixel(upPxIdx)
                )
                    LinearFill(i, upY)

                if (range.Y < height - 1 && !pixelsChecked[downPxIdx]
                    && CheckPixel(downPxIdx)
                )
                    LinearFill(i, downY)

                downPxIdx++
                upPxIdx++
            }
        }

        image!!.setPixels(pixels, 0, width, 1, 1, width - 1, height - 1)
    }

    protected fun LinearFill(x: Int, y: Int) {
        var lFillLoc = x
        var pxIdx = width * y + x

        while (true) {
            pixels?.set(pxIdx, fillColor)

            pixelsChecked[pxIdx] = true


            lFillLoc--
            pxIdx--

            if (lFillLoc < 0 || pixelsChecked[pxIdx] || !CheckPixel(pxIdx)) {
                break
            }
        }

        lFillLoc++

        var rFillLoc = x

        pxIdx = width * y + x

        while (true) {
            pixels?.set(pxIdx, fillColor)

            pixelsChecked[pxIdx] = true

            rFillLoc++
            pxIdx++

            if (rFillLoc >= width || pixelsChecked[pxIdx] || !CheckPixel(pxIdx)) {
                break
            }
        }

        rFillLoc--

        val r = FloodFillRange(lFillLoc, rFillLoc, y)

        ranges.offer(r)
    }

    protected fun CheckPixel(px: Int): Boolean {
        val red = pixels!![px].ushr(16) and 0xff
        val green = pixels!![px].ushr(8) and 0xff
        val blue = pixels!![px] and 0xff

        return (red >= startColor[0] - tolerance[0]
                && red <= startColor[0] + tolerance[0]
                && green >= startColor[1] - tolerance[1]
                && green <= startColor[1] + tolerance[1]
                && blue >= startColor[2] - tolerance[2] && blue <= startColor[2] + tolerance[2])
    }

    protected inner class FloodFillRange(var startX: Int, var endX: Int, var Y: Int)
}