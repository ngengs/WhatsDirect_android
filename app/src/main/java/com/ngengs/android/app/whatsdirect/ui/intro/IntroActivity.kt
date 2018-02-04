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

package com.ngengs.android.app.whatsdirect.ui.intro

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.ngengs.android.app.whatsdirect.R
import com.ngengs.android.app.whatsdirect.ui.main.MainActivity
import timber.log.Timber

class IntroActivity : MaterialIntroActivity(), IntroContract.View {

    var mPresenter: IntroContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntroPresenter(this).start()
    }

    override fun createSlide() {
        Timber.d("createSlide() called")
        // Add slide 1
        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.slideBackground1)
                .image(R.drawable.ic_launcher_foreground)
                .buttonsColor(R.color.colorAccent)
                .title(getString(R.string.app_name))
                .description(getString(R.string.intro_desc_1))
                .build())
        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.slideBackground2)
                .buttonsColor(R.color.colorAccent)
                .title(getString(R.string.intro_title_2))
                .description(getString(R.string.intro_desc_2))
                .neededPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE))
                .build())
        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.slideBackground3)
                .buttonsColor(R.color.colorAccent)
                .title(getString(R.string.intro_title_3))
                .description(getString(R.string.intro_desc_3))
                .build())
    }

    override fun onFinish() {
        super.onFinish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun setPresenter(presenter: IntroContract.Presenter) {
        mPresenter = presenter
    }
}