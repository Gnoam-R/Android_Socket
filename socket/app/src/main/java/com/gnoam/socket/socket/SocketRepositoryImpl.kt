package com.example.arad_january.data.socket

import android.app.Activity
import android.content.*
import android.os.IBinder
import android.util.Log
import com.gnoam.socket.service.ServiceManager
import com.gnoam.socket.util.PreferenceUtil
import com.uber.rxdogtag.RxDogTag
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException

class SocketRepositoryImpl () : SocketRepository {
    lateinit private var mActivity : Activity
    lateinit private var mContext : Context
    lateinit private var mServiceClass : Class<*>
    private var mServiceManager: ServiceManager? = null

    private var isServerConnected = false

    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun initAndConnectSocket(activity: Activity, context: Context){
        initSocket(activity, context)
        setIpPort("192.168.11.19","8181")
        setBind()
    }

    override fun initSocket(activity: Activity, context: Context) {
        mActivity = activity
        mContext = context
        mServiceClass = ServiceManager::class.java
        prefs = PreferenceUtil(mContext.applicationContext)
        RxDogTag.install()      // 디버깅 및 에러 추적
    }

    override fun setIpPort(ip: String, port: String) {
        prefs.setString("ip", ip)
        prefs.setString("port", port)
    }

    override fun setRxJavaEooroHandler() {
        RxJavaPlugins.setErrorHandler { e ->
            var error = e
            if (error is UndeliverableException) {
                error = e.cause
            }
            if (error is IOException || error is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (error is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (error is NullPointerException || error is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            if (error is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
        }
    }
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            Log.e("mServiceConnection", "onServiceConnected: ")
            mServiceManager = (service as ServiceManager.LocalBinder).getService()
            mServiceManager?.addListener(mServiceManagerListener)
            if (!isServerConnected) {
                mServiceManager?.onTcpConnect()
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.e("mServiceConnection", "onServiceDisConnected: ")
            mServiceManager?.removeListener()
            mServiceManager = null
        }

        override fun onNullBinding(name: ComponentName?) {
            Log.e("mServiceConnection", "onNull: ")
        }
    }

    // 리스너
    private val mServiceManagerListener: ServiceManager.Listener =
        object : ServiceManager.Listener {
            override fun onReceviData(data: String?) {
                Log.e(ContentValues.TAG, "onReceviData:" + data)
                val maxLen = 2000 // 2000 bytes 마다 끊어서 출력
                val len: Int = data!!.length
                if (len > maxLen) {
                    var idx = 0
                    var nextIdx = 0
                    while (idx < len) {
                        nextIdx += maxLen
                        idx = nextIdx
                    }
                }

                Log.e(ContentValues.TAG, "onReceviData: " + data)
                val header = data.split("§")[0]
                val body = data.split("§")[1]
                val api = header.split("&")
                val command = api[0].split("=")[1]
                val action = api[1].split("=")[1]
                val method = api[2].split("=")[1]
                val result = body.split("&")

            }

            override fun onConnected(isConnected: Boolean) {
                when {
                    isConnected -> {
                        Log.e(ContentValues.TAG, "onConnected: true")
                        // interface 함수 실행
                        isServerConnected = true
                    }
                    else -> {
                        Log.e(ContentValues.TAG, "onConnected: false")
                        isServerConnected = false
                    }
                }
            }
        }

    override fun setBind() {
        mActivity.bindService(
            Intent(mContext, mServiceClass),
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun setUnBind() {
        mActivity.unbindService(
            mServiceConnection
        )
    }

    override fun sendMessage(header: String, body: String) {
        mServiceManager?.sendMessage(header, body)
    }

    override fun setOnDestory() {
        if (mServiceManager != null) {
            mContext.unbindService(mServiceConnection)
            mServiceManager?.removeListener()
            mServiceManager = null
            isServerConnected = false
        }
    }
}