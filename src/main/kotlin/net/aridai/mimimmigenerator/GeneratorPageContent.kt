package net.aridai.mimimmigenerator

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import org.jetbrains.compose.web.attributes.height
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.width
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.Image

private const val canvasId: String = "mimimmi-canvas"
private const val outputImgId: String = "output-img"

//  コンテンツ
@Composable
internal fun GeneratorPageContents(bloc: GeneratorBloc) {
    val message: String by bloc.message.collectAsState()

    //  ヘッダ部分
    PageHeader()

    //  Canvas描画部分
    MimimmiCanvas(message = message)

    //  入力フィールド
    MessageField(msg = message, onMsgChanged = bloc::onMessageChanged)

    //  出力ボタン
    OutputButton(
        onButtonClicked = {
            //  クリック時にCanvasの描画イメージをimg要素に出力する。
            outputCanvasImage(
                canvas = document.getElementById(canvasId) as HTMLCanvasElement,
                img = document.getElementById(outputImgId) as HTMLImageElement,
            )
        },
    )

    //  出力先
    Img(
        src = "",
        attrs = {
            id(outputImgId)
            style {
                margin(8.px)
                maxWidth(320.px)
                display(DisplayStyle.Block)
            }
        },
    )
}

//  ヘッダ部分
@Composable
private fun PageHeader() {
    Nav(
        attrs = {
            classes("navbar", "navbar-dark", "bg-dark")
            style {
                width(100.vw)
                height(auto)
                color(rgb(255, 255, 255))
            }
        },
    ) {
        Div(attrs = { classes("container-fluid") }) {
            Span(attrs = { classes("navbar-brand") }) {
                Text("ミミッミジェネレータ")
            }
        }
    }
}

//  Canvas描画部分
@Composable
private fun MimimmiCanvas(message: String) {
    Canvas(
        attrs = {
            id(canvasId)
            width(1280)
            height(720)
            style {
                width(100.percent)
                height(auto)
                maxWidth(640.px)
                margin(16.px)
                padding(0.px)
                property("aspect-ratio", 16.0 / 9.0)
            }
        },
    ) {
        var currentMessage: String? by remember { mutableStateOf(null) }

        LaunchedEffect(Unit) {
            val image = loadImage(src = "mimimmi.png")
            val messageFlow = snapshotFlow { currentMessage }.filterNotNull()

            //  ミミッミのセリフが変化したタイミングで再描画を走らせる。
            messageFlow.collect { msg ->
                val canvas = document.getElementById(canvasId) as HTMLCanvasElement
                drawMimimmiImage(canvas = canvas, img = image, text = msg)
            }
        }

        SideEffect {
            if (currentMessage != message) currentMessage = message
        }
    }
}

//  メッセージの入力フィールド
@Composable
private fun MessageField(msg: String, onMsgChanged: (String) -> Unit) {
    TextArea(
        value = msg,
        attrs = {
            classes("form-control")
            placeholder("ミミッミのセリフ")
            style {
                width(100.percent)
                maxWidth(640.px)
                margin(16.px)
            }
            onInput { event -> onMsgChanged.invoke(event.value) }
        },
    )
}

//  出力ボタン
@Composable
private fun OutputButton(onButtonClicked: () -> Unit) {
    Button(
        attrs = {
            classes("btn", "btn-primary")
            style { margin(8.px) }
            onClick { onButtonClicked.invoke() }
        },
    ) {
        Text("↓ 画像出力 (スマホ用)")
    }
}

//  ミミッミの画像を描画する。
private fun drawMimimmiImage(canvas: HTMLCanvasElement, img: Image, text: String) {
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

//  Canvasの描画イメージをimgに出力する。
private fun outputCanvasImage(canvas: HTMLCanvasElement, img: HTMLImageElement) {
    val base64Img = canvas.toDataURL(type = "image/png")
    img.src = base64Img
}
