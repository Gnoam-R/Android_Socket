package com.gnoam.socket.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gnoam.socket.R
import com.gnoam.socket.databinding.ActivityMainBinding
import io.socket.client.Socket

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var socket: Socket
    var data: String? = null

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fcv_socket, SocketFragment())
            commit()
        }
    }
}