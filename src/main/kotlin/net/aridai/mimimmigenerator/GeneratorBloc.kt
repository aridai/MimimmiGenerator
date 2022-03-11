package net.aridai.mimimmigenerator

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//  ミミッミジェネレータのBLoC
internal class GeneratorBloc {

    //  ミミッミのセリフ
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = this._message.asStateFlow()

    //  ミッミのセリフが変更されたとき。
    fun onMessageChanged(text: String) {
        this._message.value = text
    }

    //  終了処理を行う。
    fun dispose() = Unit
}
