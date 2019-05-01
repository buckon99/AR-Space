package com.csc309.arspace

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.csc309.arspace.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.out.println("test")
        System.out.println("Test")
        setContentView(R.layout.activity_main)
    }
}
