package net.aridai.mimimmigenerator

import androidx.compose.runtime.DisposableEffect
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable

fun main() {
    val bloc = GeneratorBloc()

    renderComposable(rootElementId = "root") {
        DisposableEffect(Unit) {
            onDispose { bloc.dispose() }
        }

        Div(
            attrs = {
                classes("container")
                style {
                    width(100.vw)
                    display(DisplayStyle.Flex)
                    flexFlow(FlexDirection.Column, FlexWrap.Nowrap)
                    alignItems(AlignItems.Center)
                    justifyContent(JustifyContent.Center)
                }
            },
        ) {
            GeneratorPageContents(bloc = bloc)
        }
    }
}
