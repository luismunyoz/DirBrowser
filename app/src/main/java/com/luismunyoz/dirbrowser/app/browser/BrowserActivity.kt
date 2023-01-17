package com.luismunyoz.dirbrowser.app.browser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.luismunyoz.dirbrowser.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
    }
}