package com.gnoam.socket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gnoam.socket.databinding.ActivityMainBinding
import io.socket.client.Socket
import io.socket.emitter.Emitter

class MainActivity : AppCompatActivity() {

    private lateinit var socket: Socket
    var data: String? = null

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        socket = SocketApplication.get()
        socket.connect()

        socket.on("lightOn", light_on)
        socket.off("lightOn", light_off)
    }

    var light_on = Emitter.Listener { args ->
        runOnUiThread({
            binding.message.setText("소켓 on 성공")
            data = args[0].toString()
            binding.receiveText.setText(data)
        })
    }

    var light_off = Emitter.Listener { args ->
        runOnUiThread({
            binding.message.setText("소켓 on 성공")
            data = args[0].toString()
            binding.receiveText.setText(data)
        })
    }
}