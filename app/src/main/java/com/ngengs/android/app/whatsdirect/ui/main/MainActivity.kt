/*
 * Copyright 2018 Rizky Kharisma (@ngengs)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ngengs.android.app.whatsdirect.ui.main

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import com.ngengs.android.app.whatsdirect.R
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var mPresenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        MainPresenter(this)
    }

    private fun initView() {
        Timber.d("initView() called")
        button.setOnClickListener {
            mPresenter.setCountryCode(countrySelector.selectedCountryCode)
            mPresenter.setNumber(phoneNumberEditText.text.toString())
            mPresenter.handleClick()
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.stop()
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        mPresenter = presenter
    }

    override fun isValid(phoneNumber: String): Boolean {
        Timber.d("isValid() called")
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)
    }

    override fun openWhatsApp(phoneNumber: String) {
        Timber.d("openWhatsApp() called")
        try {
            val url = "whatsapp://send?phone=$phoneNumber"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
            mPresenter.stop()
        } catch (e: ActivityNotFoundException) {
            Timber.w(e.message)
            mPresenter.errorOpenWhatsApp(baseContext.getString(R.string.error_not_installed))
        }
    }

    override fun phoneNotValid(phoneNumber: String) {
        Timber.e("phoneNotValid: %s", phoneNumber)
        showErrorMessage(baseContext.getString(R.string.error_not_valid))
    }

    override fun bindValue() {
        mPresenter.setCountryCode(countrySelector.selectedCountryCode)
        mPresenter.setNumber(phoneNumberEditText.text.toString())
    }

    override fun releaseListener() {
        button.setOnClickListener(null)
    }

    override fun permissionGranted(): Boolean {
        var alreadyHasPermission = false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alreadyHasPermission = true
            return alreadyHasPermission
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_PERMISSION_PHONE_STATE)
        } else {
            alreadyHasPermission = true
        }
        return alreadyHasPermission
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_PHONE_STATE -> {
                mPresenter.start()
            }
        }
    }

    override fun getCountryISOeBySIM(): String? {
        Timber.d("getCountryISOeBySIM() called")
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.simCountryIso
    }

    override fun getCountryISOByNetwork(): String? {
        Timber.d("getCountryISOByNetwork() called")
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkCountryIso
    }

    override fun getCountryISOByLocale(): String? {
        Timber.d("getCountryISOByLocale() called")
        return Locale.getDefault().country
    }

    override fun setCountryISO(countryIso: String) {
        Timber.d("setCountryISO() called")
        countrySelector.setCountryForNameCode(countryIso)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_PERMISSION_PHONE_STATE = 10
    }
}
