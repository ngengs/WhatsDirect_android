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

import com.ngengs.android.app.whatsdirect.ui.BasePresenter
import com.ngengs.android.app.whatsdirect.ui.BaseView

interface MainContract {
    interface Presenter : BasePresenter {
        fun setCountryCode(countryCode: String)
        fun setNumber(phoneNumber: String)
        fun handleClick()
    }

    interface View : BaseView<Presenter> {
        fun openWhatsApp(phoneNumber: String)
        fun phoneNotValid(phoneNumber: String)
        fun isValid(phoneNumber: String): Boolean
        fun bindValue()
        fun releaseListener()
        fun completeAction()
        fun permissionGranted(): Boolean
        fun getCountryISOeBySIM(): String?
        fun getCountryISOByNetwork(): String?
        fun getCountryISOByLocale(): String?
        fun setCountryISO(countryIso: String)
    }
}