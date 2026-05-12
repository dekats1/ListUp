package com.example.myapplication.ui_components

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Base64
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.utils.AssetImage
import com.example.myapplication.utils.ListItem

@Composable
fun InfoScreen(item: ListItem) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Card(
        modifier = Modifier.fillMaxSize().padding(5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        if (isLandscape) {
            Row(modifier = Modifier.fillMaxSize()) {
                AssetImage(
                    imageName = item.imageName,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxHeight().weight(0.4f)
                )
                HtmlLoader(
                    htmlName = item.htmlName,
                    modifier = Modifier.fillMaxHeight().weight(0.6f)
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                AssetImage(
                    imageName = item.imageName,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxWidth().weight(0.35f)
                )
                HtmlLoader(
                    htmlName = item.htmlName,
                    modifier = Modifier.fillMaxWidth().weight(0.65f)
                )
            }
        }
    }
}

@Composable
fun HtmlLoader(htmlName: String, modifier: Modifier = Modifier) {
    var backEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val htmlString = remember(htmlName) {
        try {
            val inputStream = context.assets.open("html/$htmlName")
            val content = String(inputStream.readBytes(), Charsets.UTF_8)
            inputStream.close()
            content
        } catch (e: Exception) {
            "<html><body><p style='color:red;padding:16px;'>Ошибка: ${e.message}</p></body></html>"
        }
    }

    // Храним WebView в remember — он НЕ пересоздаётся при повороте
    val webView = remember(htmlName) {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    // Все ссылки открываем внутри WebView
                    return false
                }

                override fun onPageFinished(view: WebView, url: String?) {
                    backEnabled = view.canGoBack()
                }
            }

            loadDataWithBaseURL(
                "https://localhost/",
                htmlString,
                "text/html",
                "UTF-8",
                null
            )
        }
    }

    AndroidView(
        factory = { webView },  // передаём готовый экземпляр, factory вызывается 1 раз
        modifier = modifier.padding(5.dp),
        update = { /* ничего не делаем — WebView уже настроен */ }
    )

    BackHandler(enabled = backEnabled) {
        if (webView.canGoBack()) webView.goBack()
    }
}