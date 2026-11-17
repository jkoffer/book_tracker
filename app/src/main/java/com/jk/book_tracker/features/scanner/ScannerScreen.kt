package com.jk.book_tracker.features.scanner

import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.jk.book_tracker.features.common.LockScreenOrientation
import com.jk.book_tracker.utils.popBackStackIfCurrent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(navController: NavController) {
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackIfCurrent("scanner")
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {}
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                if (hasCameraPermission) {
                    CameraPreview(navController = navController)
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Camera permission is required to scan barcodes.")
                    }
                }
            }
        }
    )
}


@Composable
private fun CameraPreview(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var lastScannedValue by remember { mutableStateOf<String?>(null) }

    val scanner = rememberBarcodeScanner()

    val cameraController = rememberCameraController(
        context,
        lifecycleOwner,
        scanner,
    ) { scannedValue ->
        if (scannedValue != lastScannedValue) {
            lastScannedValue = scannedValue

            navController.navigate("add-book?isbn=${scannedValue}") {
                popUpTo("scanner") {
                    inclusive = true
                }
            }
        }
    }

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    implementationMode =
                        PreviewView.ImplementationMode.COMPATIBLE

                    controller = cameraController
                }
            }
        )

        ScannerVisualEffect()
    }
}

@Composable
private fun rememberBarcodeScanner() = remember {
    val options = BarcodeScannerOptions.Builder()
        .build()

    BarcodeScanning.getClient(options)
}

@Composable
private fun rememberCameraController(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    scanner: BarcodeScanner,
    onBarcodeScanned: (String) -> Unit,
): LifecycleCameraController {
    val executor = remember(context) {
        ContextCompat.getMainExecutor(context)
    }
    val controller = remember(context) {
        LifecycleCameraController(context)
    }

    DisposableEffect(lifecycleOwner, scanner) {
        controller.bindToLifecycle(lifecycleOwner)

        controller.setImageAnalysisAnalyzer(
            executor,
            MlKitAnalyzer(
                listOf(scanner),
                COORDINATE_SYSTEM_VIEW_REFERENCED,
                executor
            ) { result ->
                result?.getValue(scanner)
                    ?.firstOrNull()
                    ?.rawValue
                    ?.let(onBarcodeScanned)
            }
        )

        onDispose {
            scanner.close()
            controller.clearImageAnalysisAnalyzer()
        }
    }

    return controller
}