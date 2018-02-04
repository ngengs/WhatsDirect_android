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

package com.ngengs.android.app.whatsdirect.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ngengs.android.app.whatsdirect.R
import com.ngengs.android.app.whatsdirect.ui.intro.IntroActivity
import com.ngengs.android.app.whatsdirect.ui.main.MainActivity

class SplashActivity : AppCompatActivity(), SplashContract.View {

    var mPresenter: SplashContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        SplashPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter!!.stop()
        mPresenter = null
    }

    override fun isFirstTime(): Boolean {
        val sharedPreferences = getSharedPreferences("WhatsDirectPref", Context.MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("is_first_time", true)
        if (isFirstTime) {
            sharedPreferences.edit().putBoolean("is_first_time", false).apply()
        }
        return isFirstTime
    }

    override fun runApp() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun runIntro() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun setPresenter(presenter: SplashContract.Presenter) {
        mPresenter = presenter
    }
}
