package com.example.businesscards.ui.main

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.businesscards.R
import com.example.businesscards.core.extensions.appComponent
import com.example.businesscards.core.extensions.isPermissionsGranted
import com.example.businesscards.core.extensions.showToast
import com.example.businesscards.databinding.ActivityMainBinding
import com.example.businesscards.di.common.BaseActivity

class MainActivity : BaseActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }
    private val permissionRequester =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            updatePermissionState()
        }

    private val navController: NavController by lazy { findNavController(binding.navHostFragment.id) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appComponent.inject(this)
        permissionRequester.launch(arrayOf(Manifest.permission.NFC))
//        navController.navigate(R.id.ac)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun updatePermissionState() {
        if (isPermissionsGranted(Manifest.permission.NFC)) {
            if (!isNfcSupportedAndUpToDate()) {
                showToast("Your device haven't got AR services")
                finish()
            }
        }
    }

    private fun isNfcSupportedAndUpToDate(): Boolean {
//        return when (ArCoreApk.getInstance().checkAvailability(this)) {
//            Availability.SUPPORTED_INSTALLED -> true
//            Availability.SUPPORTED_APK_TOO_OLD, Availability.SUPPORTED_NOT_INSTALLED -> {
//                try {
//                    // Request ARCore installation or update if needed.
//                    when (ArCoreApk.getInstance().requestInstall(this, true)) {
//                        InstallStatus.INSTALL_REQUESTED -> {
//                            Log.i(TAG, "ARCore installation requested.")
//                            false
//                        }
//                        InstallStatus.INSTALLED -> true
//                    }
//                } catch (e: UnavailableException) {
//                    Log.e(TAG, "ARCore not installed", e)
//                    false
//                }
//            }
//            Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE -> {
//                showToast("Sorry. This device is not supported for AR")
//                false
//            }
//            Availability.UNKNOWN_CHECKING,
//            Availability.UNKNOWN_ERROR,
//            Availability.UNKNOWN_TIMED_OUT-> {
//                showToast("Sorry. Unknown error acquires.")
//                false
//            }
//        }
        return true
    }
}