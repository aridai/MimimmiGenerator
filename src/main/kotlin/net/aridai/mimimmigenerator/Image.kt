package net.aridai.mimimmigenerator

import org.w3c.dom.Image
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//  画像を読み込む。
internal suspend fun loadImage(src: String): Image = suspendCoroutine { continuation ->
    val img = Image()
    img.onload = { continuation.resume(img) }
    img.onerror = { _, _, _, _, _ -> continuation.resumeWithException(Exception("画像読み込みエラー")) }
    img.src = src
}
