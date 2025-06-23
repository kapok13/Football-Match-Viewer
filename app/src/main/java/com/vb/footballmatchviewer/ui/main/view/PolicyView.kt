package com.vb.footballmatchviewer.ui.main.view

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

const val POLICY_VIEW_ROUTE = "POLICY_VIEW"

@Composable
fun PolicyView(innerPaddings: PaddingValues) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl("https://www.google.com")
        }
    }
    AndroidView(
        factory = { webView },
        modifier = Modifier
            .padding(innerPaddings)
            .fillMaxSize()
    )
}
