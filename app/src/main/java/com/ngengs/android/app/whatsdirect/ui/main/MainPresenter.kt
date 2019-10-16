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

import com.ngengs.android.app.whatsdirect.data.PhoneNumber

class MainPresenter(view: MainContract.View) : MainContract.Presenter {

    private val mView: MainContract.View = view
    private val phoneNumber: PhoneNumber = PhoneNumber

    init {
        this.mView.setPresenter(this)
    }

    override fun start() {
        if (mView.permissionGranted()) {
            var countryISO: String?
            countryISO = mView.getCountryISOByNetwork()
            if (countryISO == null) countryISO = mView.getCountryISOeBySIM()
            if (countryISO == null) countryISO = mView.getCountryISOByLocale()
            if (countryISO != null) mView.setCountryISO(countryISO)
            mView.bindValue()
        }
    }

    override fun stop() {
        mView.releaseListener()
    }

    override fun setCountryCode(countryCode: String) {
        phoneNumber.countryCode = countryCode
    }

    override fun setNumber(phoneNumber: String) {
        var tempPhone = phoneNumber
        tempPhone.trim()
        if (tempPhone.isEmpty()) return
        val regex = Regex("^0|[+()-]")
        tempPhone = regex.replace(tempPhone, "")
        if (this.phoneNumber.countryCode != null && tempPhone.length > this.phoneNumber.countryCode!!.length) {
            val tempCountryCodeInside = tempPhone.substring(0, this.phoneNumber.countryCode!!.length)
            if (tempCountryCodeInside.contains(this.phoneNumber.countryCode!!, true)) {
                tempPhone = tempPhone.removePrefix(this.phoneNumber.countryCode!!)
            }
        }
        this.phoneNumber.number = tempPhone
    }

    override fun handleClick() {
        if (phoneNumber.full == null) {
            mView.phoneNotValid(phoneNumber.toString())
            return
        }
        val fullNumber: String = phoneNumber.full!!
        if (mView.isValid(fullNumber)) {
            mView.openWhatsApp(fullNumber)
        } else {
            mView.phoneNotValid(fullNumber)
        }
    }

    override fun errorOpenWhatsApp(message: String?) {
        mView.showErrorMessage(message ?: "Something wrong")
    }
}