package com.example.arad_january.data.socket

import android.app.Activity
import android.content.Context

interface SocketRepository {
        fun initAndConnectSocket(activity: Activity, context: Context)
        fun initSocket(activity: Activity, context: Context)
        fun setRxJavaEooroHandler()
        fun setIpPort(ip : String, port : String)
        fun setBind()
        fun setUnBind()
        fun setOnDestory()
        fun sendMessage(header: String, body: String)
}