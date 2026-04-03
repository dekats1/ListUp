package com.example.myapplication.ui_components

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.utils.AssetImage
import com.example.myapplication.utils.ListItem

@Composable
fun InfoScreen(item: ListItem) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AssetImage(
                imageName = item.imageName,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
            )
            HtmlLoader(
                htmlName = item.htmlName,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f)
            )
        }
    }
}

@Composable
fun HtmlLoader(htmlName: String, modifier: Modifier = Modifier) {
    var backEnabled by remember { mutableStateOf(false) }
    var webView: WebView? = null

    val context = LocalContext.current

    // Читаем HTML с обработкой ошибок
    val htmlString = remember(htmlName) {
        try {
            Log.d("HtmlLoader", "Открываем файл: html/$htmlName")
            val inputStream = context.assets.open("html/$htmlName")
            val bytes = inputStream.readBytes()
            inputStream.close()
            val content = String(bytes, Charsets.UTF_8)
            Log.d("HtmlLoader", "Файл загружен, размер: ${content.length} символов")
            content
        } catch (e: Exception) {
            Log.e("HtmlLoader", "Ошибка загрузки html/$htmlName: ${e.message}")
            "<html><body><p style='color:red;padding:16px;'>Ошибка загрузки файла: html/$htmlName<br>${e.message}</p></body></html>"
        }
    }

    // Кодируем в Base64 — решает все проблемы с кириллицей
    val encodedHtml = remember(htmlString) {
        Base64.encodeToString(htmlString.toByteArray(Charsets.UTF_8), Base64.NO_PADDING)
    }

    AndroidView(
        modifier = modifier.padding(5.dp),
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                        backEnabled = view.canGoBack()
                    }
                    override fun onPageFinished(view: WebView, url: String?) {
                        Log.d("HtmlLoader", "Страница загружена: $url")
                    }
                }
                settings.javaScriptEnabled = true
                // Base64 вместо loadData — корректно отображает кириллицу
                loadData(encodedHtml, "text/html", "base64")
                webView = this
            }
        },
        update = { wv ->
            webView = wv
        }
    )

    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }
}