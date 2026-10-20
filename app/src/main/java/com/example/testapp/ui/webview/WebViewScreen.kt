package com.example.testapp.ui.webview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Message
import android.view.View
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.testapp.ScreenOrientation
import com.example.testapp.setOrientation
import java.net.URISyntaxException


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WebViewScreen(viewModel: WebViewViewModel) {
    val url = viewModel.url
    val activity = LocalActivity.current as ComponentActivity
    val systemBars = remember { WindowCompat.getInsetsController(activity.window, activity.window.decorView) }

    var webView by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var popup by remember { mutableStateOf<WebView?>(null) }
    var fullscreenView by remember { mutableStateOf<View?>(null) }
    var fullscreenCallback by remember { mutableStateOf<WebChromeClient.CustomViewCallback?>(null) }
    var filesCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }
    var cameraRequest by remember { mutableStateOf<PermissionRequest?>(null) }

    val filesLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        filesCallback?.onReceiveValue(selectedFiles(result))
        filesCallback = null
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraRequest?.let {
            if (granted) it.grant(arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) else it.deny()
        }
        cameraRequest = null
    }

    fun closePopup() {
        val child = popup
        popup = null
        child?.destroy()
    }

    fun exitFullscreen() {
        if (fullscreenView == null) return
        fullscreenView = null
        fullscreenCallback?.onCustomViewHidden()
        fullscreenCallback = null
        setOrientation(activity, ScreenOrientation.Portrait)
        systemBars.show(WindowInsetsCompat.Type.statusBars())
    }

    val webClient = remember {
        object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean =
                openOutside(view, request.url)

            override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
                if (view == webView) canGoBack = view.canGoBack()
            }
        }
    }

    val chromeClient = remember {
        object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message
            ): Boolean {
                closePopup()
                val child = WebView(view.context)
                child.setUp(webClient, this)
                (resultMsg.obj as WebView.WebViewTransport).webView = child
                resultMsg.sendToTarget()
                popup = child
                return true
            }

            override fun onCloseWindow(window: WebView) {
                if (window == popup) closePopup()
            }

            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                filesCallback?.onReceiveValue(null)
                filesCallback = filePathCallback
                val intent = fileChooserParams.createIntent()
                if (fileChooserParams.mode == FileChooserParams.MODE_OPEN_MULTIPLE) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                return try {
                    filesLauncher.launch(intent)
                    true
                } catch (_: ActivityNotFoundException) {
                    filesCallback = null
                    false
                }
            }

            override fun onPermissionRequest(request: PermissionRequest) {
                if (PermissionRequest.RESOURCE_VIDEO_CAPTURE !in request.resources) {
                    request.deny()
                    return
                }
                val hasCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED
                if (hasCamera) {
                    request.grant(arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE))
                } else {
                    cameraRequest = request
                    cameraLauncher.launch(Manifest.permission.CAMERA)
                }
            }

            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                if (fullscreenView != null) {
                    callback.onCustomViewHidden()
                    return
                }
                fullscreenView = view
                fullscreenCallback = callback
                setOrientation(activity, ScreenOrientation.Landscape)
                systemBars.hide(WindowInsetsCompat.Type.statusBars())
            }

            override fun onHideCustomView() = exitFullscreen()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.imeAnimationTarget)
                .clipToBounds()
        ) {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
                WebView(context).apply {
                    setUp(webClient, chromeClient)
                    loadUrl(url)
                    webView = this
                }
            })

            popup?.let { child ->
                key(child) {
                    AndroidView(modifier = Modifier.fillMaxSize(), factory = { child })
                }
            }
        }

        fullscreenView?.let { view ->
            key(view) {
                AndroidView(modifier = Modifier.fillMaxSize(), factory = { view })
            }
        }
    }

    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }

    BackHandler(enabled = popup != null) {
        val child = popup ?: return@BackHandler
        if (child.canGoBack()) child.goBack() else closePopup()
    }

    BackHandler(enabled = fullscreenView != null) {
        exitFullscreen()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        CookieManager.getInstance().flush()
    }

    DisposableEffect(Unit) {
        onDispose {
            exitFullscreen()
            closePopup()
            CookieManager.getInstance().flush()
            webView?.destroy()
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebView.setUp(client: WebViewClient, chromeClient: WebChromeClient) {
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.javaScriptCanOpenWindowsAutomatically = true
    settings.setSupportMultipleWindows(true)
    settings.mediaPlaybackRequiresUserGesture = false
    settings.userAgentString = settings.userAgentString.replace("; wv", "")

    val cookies = CookieManager.getInstance()
    cookies.setAcceptCookie(true)
    cookies.setAcceptThirdPartyCookies(this, true)

    webViewClient = client
    webChromeClient = chromeClient
}

private fun openOutside(view: WebView, uri: Uri): Boolean {
    if (uri.scheme == "http" || uri.scheme == "https") return false
    val intent = try {
        if (uri.scheme == "intent") {
            Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                component = null
                selector = null
            }
        } else {
            Intent(Intent.ACTION_VIEW, uri)
        }
    } catch (_: URISyntaxException) {
        return true
    }
    try {
        view.context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        intent.getStringExtra("browser_fallback_url")?.let(view::loadUrl)
    }
    return true
}

private fun selectedFiles(result: ActivityResult): Array<Uri>? {
    if (result.resultCode != Activity.RESULT_OK) return null
    val data = result.data ?: return null
    val clip = data.clipData
    if (clip != null) return Array(clip.itemCount) { clip.getItemAt(it).uri }
    return data.data?.let { arrayOf(it) }
}
