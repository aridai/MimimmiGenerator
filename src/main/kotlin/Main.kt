import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun main() {
    window.onload = { GlobalScope.launch { run() } }
}

suspend fun run() {
    val img = loadImage(src = "mimimmi.png")
    val canvas = document.getElementById("canvas") as HTMLCanvasElement
    val input = document.getElementById("mimimmi-text") as HTMLTextAreaElement
    val outputButton = document.getElementById("output-button") as HTMLButtonElement
    val outputImg = document.getElementById("output-img") as HTMLImageElement

    input.oninput = { draw(canvas, img, text = input.value) }
    outputButton.onclick = { output(canvas, outputImg) }

    draw(canvas, img, text = input.defaultValue)
}

//  描画処理を行う。
fun draw(canvas: HTMLCanvasElement, img: Image, text: String) {
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.clearRect(x = 0.0, y = 0.0, w = canvas.width.toDouble(), h = canvas.height.toDouble())

    //  ミミッミの画像を描画する。
    context.drawImage(img, dx = 0.0, dy = 0.0)

    //  ミミッミのセリフを描画する。
    context.fillStyle = "#000000"
    context.font = "48px sans-serif"

    val x = 50.0
    val y = 520.0
    val lineHeight = 64
    for ((index, line) in text.lineSequence().withIndex()) {
        val offsetY = lineHeight * index
        context.fillText(line, x = x, y = y + offsetY)
    }
}

//  画像出力を行う。
fun output(canvas: HTMLCanvasElement, img: HTMLImageElement) {
    val base64Img = canvas.toDataURL(type = "image/png")
    img.src = base64Img
}

//  画像を読み込む。
suspend fun loadImage(src: String): Image = suspendCoroutine { continuation ->
    val img = Image()
    img.onload = { continuation.resume(img) }
    img.onerror = { _, _, _, _, _ -> continuation.resumeWithException(Exception()) }
    img.src = src
}
