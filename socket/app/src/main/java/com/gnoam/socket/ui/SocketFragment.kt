package com.gnoam.socket.ui

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.arad_january.data.socket.SocketRepository
import com.example.arad_january.data.socket.SocketRepositoryImpl
import com.gnoam.socket.R
import com.gnoam.socket.databinding.FragmentSocketBinding

class SocketFragment : Fragment(R.layout.fragment_socket), View.OnClickListener {
    private val TAG = "SocketFragment"

    lateinit var binding: FragmentSocketBinding
    lateinit var mContext : Context
    lateinit var mActivity : Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = context as Activity
    }

    private val socketRepository: SocketRepository = SocketRepositoryImpl()

    // socket 코드
    var isbinding = false
    var isKill = false
    lateinit var ip: String
    lateinit var port: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSocketBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // socket 코드
//        mActivity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Log.e(TAG,"check" + socketRepository.setRxJavaEooroHandler())
        socketRepository.initSocket(mActivity,mContext)

        binding.serverStart.setOnClickListener(this)
        binding.serverStop.setOnClickListener(this)
        binding.rotateStart.setOnClickListener(this)
        binding.rotateEnd.setOnClickListener(this)
        binding.stepOne.setOnClickListener(this)
        binding.stepTwo.setOnClickListener(this)
        binding.stepThree.setOnClickListener(this)
        binding.stop.setOnClickListener(this)
        binding.turnLeft.setOnClickListener(this)
        binding.turnRight.setOnClickListener(this)
        binding.parkingStart.setOnClickListener(this)
        binding.parkingEnd.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.server_start -> {
                // 소켓 연결 시작
                if (!isbinding) {
                    ip = binding.editIp.text.toString()
                    port = binding.editPort.text.toString()
                    socketRepository.setIpPort(ip,port)
                    socketRepository.setBind()
                    isbinding = true
                    Log.e(ContentValues.TAG, "isbinding: " + isbinding)
                }
            }

            R.id.server_stop -> {
                // 소켓 연결 중지
                if (isbinding) {
                    socketRepository.setUnBind()
                    isbinding = false
                    Log.e(ContentValues.TAG, "isbinding: " + isbinding)
                }
            }

            R.id.rotate_start -> {
                // 180도 회전 시작
                socketRepository.sendMessage(
                    "api-command=ecorner&api-action=rotate-half&api-method=post&sub-system-id=1",
                    "onoff=1"
                )
            }
            R.id.rotate_end -> {
                // 180도 회전 끝
                socketRepository.sendMessage(
                    "api-command=ecorner&api-action=rotate-half&api-method=post&sub-system-id=1",
                    "onoff=0"
                )
            }
            R.id.step_one -> {
                // 1단계
                socketRepository.sendMessage(
                    "api-command=status&api-action=init&api-method=post&sub-system-id=1",
                    ""
                )
            }
            R.id.step_two -> {
                // 2단계
                socketRepository.sendMessage(
                    "api-command=status&api-action=welcome&api-method=post&sub-system-id=1",
                    ""
                )
            }
            R.id.step_three -> {
                // 3단계
                socketRepository.sendMessage(
                    "api-command=status&api-action=attach&api-method=post&sub-system-id=1",
                    "attach=1"
                )
            }
            R.id.stop -> {
                // 정차
                Handler().postDelayed({
                    socketRepository.sendMessage(
                        "api-command=ecorner&api-action=speed&api-method=post&sub-system-id=1",
                        "level=0&dir=forward"
                    )
                }, 500L)
            }
            R.id.turn_left -> {
                // 좌회전
                socketRepository.sendMessage(
                    "api-command=ecorner&api-action=speed&api-method=post&sub-system-id=1",
                    "level=2&dir=left"
                )
            }
            R.id.turn_right -> {
                // 우회전
                socketRepository.sendMessage(
                    "api-command=ecorner&api-action=speed&api-method=post&sub-system-id=1",
                    "level=2&dir=right"
                )
            }
            R.id.parking_start -> {
                // 오토파킹 시작
                Handler().postDelayed({
                    socketRepository.sendMessage(
                        "api-command=ecorner&api-action=parallel-park&api-method=post&sub-system-id=1",
                        "onoff=1"
                    )
                }, 5000L)

            }
            R.id.parking_end -> {
                // 오토파킹 끝

                Handler().postDelayed({
                    socketRepository.sendMessage(
                        "api-command=ecorner&api-action=parallel-park&api-method=post&sub-system-id=1",
                        "onoff=0"
                    )
                }, 18000L)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socketRepository.setOnDestory()
        if (isKill) {
            mActivity.moveTaskToBack(true)
//            mActivity.finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
}

