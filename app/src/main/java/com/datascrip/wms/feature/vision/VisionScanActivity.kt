package com.datascrip.wms.feature.vision

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.datascrip.wms.R
import com.datascrip.wms.core.base.BaseActivity
import com.datascrip.wms.widget.vision.CameraSource2
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.scanner_activity.*

class VisionScanActivity: BaseActivity() {

    private val RC_CAMERA  = 228
    val FOCUS_MODE_CONTINUOUS_PICTURE = "continuous-picture"
    val FLASH_MODE_TORCH = "torch"
    val FLASH_MODE_OFF = "off"

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource2
    private lateinit var requireContext: Context

    private var extras: Bundle? = null

    private var isTouchOn: Boolean = false
    private var isScanning: Boolean = true
    private var isActivityRunning: Boolean = true

    override fun getLayoutRes(): Int = R.layout.scanner_activity
    override fun setupView() {
        requireContext = this@VisionScanActivity
        extras = intent.extras
        closed_img.setOnClickListener { finish() }
        flash_img.setOnClickListener { setFlashStatus() }
    }

    override fun subscribeState() {}

    private fun setFlashStatus() {
        if (isTouchOn) {
            flash_img.setImageResource(R.drawable.ic_flash_off)
            cameraSource.flashMode = FLASH_MODE_OFF
        }
        else {
            flash_img.setImageResource(R.drawable.ic_flash_on)
            cameraSource.flashMode = FLASH_MODE_TORCH
        }
        isTouchOn = !isTouchOn
    }

    private fun onRequestPermissionCamera()
    {
        when
        {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> { showVisionScan() }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)  -> requestPermission()
            else -> requestPermission()
        }
    }

    private fun requestPermission(){
        val listPermission : MutableList<String> = mutableListOf()
        listPermission.add(Manifest.permission.CAMERA)
        requestPermissions(listPermission.toTypedArray(), RC_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            RC_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showVisionScan()
                }else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        requestPermission()
                    }else {
                        // Case: Permission denied permanently.
                        // WIll open Permission setting's page.
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", this.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }

            }
        }
    }

    private fun showVisionScan() {
        barcodeDetector = BarcodeDetector.Builder(requireContext).setBarcodeFormats(Barcode.ALL_FORMATS).build()
        cameraSource = CameraSource2.Builder(requireContext, barcodeDetector)
            .setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE)
            .setRequestedPreviewSize(1280, 720)
            .setRequestedFps(90.0f)
            .build()
        val surfaceView = SurfaceView(requireContext)
        surface_view.addView(surfaceView)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceRedrawNeeded(holder: SurfaceHolder) { }
            override fun surfaceChanged(holder: SurfaceHolder,format: Int,width: Int,height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }

            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                cameraSource.start(surfaceView.holder)
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode>{
            override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
                val barcodes: SparseArray<Barcode>? = p0?.detectedItems
                if (barcodes!= null && barcodes.size() >0){
                    check(isScanning && isActivityRunning){return}
                    isScanning=false
                    val result = barcodes.valueAt(0).displayValue
                    Handler(Looper.getMainLooper()).post {
                        initializeResult(result)
                    }
                }
            }
            override fun release() {}
        })
    }

    private fun initializeResult(result: String?) {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        RingtoneManager.getRingtone(requireContext, notification)?.play()

        val intent = Intent().apply {
            putExtra("key", extras?.getString("key"))
            putExtra("isLookup", extras?.getBoolean("isLookup", false))
            putExtra("result", result.toString())
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        isScanning = false
        if(::cameraSource.isInitialized) cameraSource.stop()
    }

    override fun onResume() {
        super.onResume()
        isScanning = true
        isActivityRunning = true
    }

    override fun onPause() {
        super.onPause()
        isScanning = false
        isActivityRunning = false
    }

    override fun onStart() {
        super.onStart()
        onRequestPermissionCamera()
    }

}