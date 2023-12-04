package com.example.myapplication

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.github.sumimakito.awesomeqr.option.background.StillBackground
import com.github.sumimakito.awesomeqr.option.color.Color


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
//        const val DEFAULT_QRCODE_VERSION: Int = 6
//        const val DEFAULT_LOGO_SCALE_FACTOR = 0.25f  // 40% of qrCodes size
//        const val DEFAULT_PATTERN_SCALE = 0.9f
        val renderOption = RenderOption().apply {
            content = "Special, thus awesome." // content to encode
            size = 1000 // size of the final QR code image
            borderWidth = 20 // width of the empty space around the QR code
            patternScale = 0.35f // specify a scale for patterns
            roundedPatterns = true // if true, blocks will be drawn as dots instead
            clearBorder = false// if true, the background will NOT be drawn on the border area

        }
        val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.kfc)
        val color = Color().apply {
            light = -0x1
            dark = getDominantColor(backgroundImage)
            background = -0x1
            auto = true
        }
        renderOption.color=color

// Blend the QR code with the background manually

        // A blend background (to draw a QR code onto an area of a still image)
        val background = StillBackground().apply {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.kfc)// assign a bitmap as the background
            //clippingRect = Rect(0, 0, 200, 200) // crop the background before applying
            alpha = 0.90f // alpha of the background to be drawn
            //borderRadius = 200
        }
       renderOption.background=background



        val result = AwesomeQrRenderer.renderAsync(renderOption, { result ->
            runOnUiThread {
                if (result.bitmap != null) {
                    val generatedBitmap: Bitmap? = result.bitmap
                    generatedBitmap?.let {
                        // Display the generated QR code bitmap in the ImageView
                        val ima: ImageView = findViewById(R.id.img)
                        ima.setImageBitmap(it)
                    }
                }
                else {

                }
            }
        }, { exception ->
            exception.printStackTrace()
            // Oops, something went wrong.
        })

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDominantColor(backgroundImage: Bitmap?): Int {
        val newBitmap = backgroundImage?.let { Bitmap.createScaledBitmap(it, 8, 8, true) }
        var red = 0
        var green = 0
        var blue = 0
        var c = 0
        var r: Int
        var g: Int
        var b: Int
        if (backgroundImage != null) {
            for (y in 0 until (newBitmap?.height ?:backgroundImage.height )) {
                for (x in 0 until (newBitmap?.height ?: backgroundImage.height)) {
                    val color = newBitmap?.getPixel(x, y)
                    r = color!! shr 16 and 0xFF
                    g = color shr 8 and 0xFF
                    b = color and 0xFF
                    if (r > 200 || g > 200 || b > 200) continue
                    red += r
                    green += g
                    blue += b
                    c++
                }
            }
        }
        newBitmap?.recycle()
        return if (c == 0) {
            // got a bitmap with no pixels in it
            // avoid the "divide by zero" error
            -0x1000000
        } else {
            red = Math.max(0, Math.min(0xFF, red / c))
            green = Math.max(0, Math.min(0xFF, green / c))
            blue = Math.max(0, Math.min(0xFF, blue / c))
            0xFF shl 24 or (red shl 16) or (green shl 8) or blue
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}