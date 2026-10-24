package com.android.vmcore.hal

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.media.AudioTrack
import androidx.core.content.ContextCompat
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance

class AudioService @JvmOverloads constructor(
    private val mContext: Context,
    private val mVMInstance: VMInstance? = null
) {
    private var mMute: Boolean = false
    private var mNativePtr: Long = 0L
    private val mTrackList: MutableList<AudioTrack> = ArrayList()
    private val mRecordList: MutableList<AudioRecord> = ArrayList()

    init {
        if (mVMInstance != null) {
            mNativePtr = try {
                nativeSetup(mVMInstance.vmId)
            } catch (t: Throwable) {
                KLog.e(TAG, "nativeSetup failed", t)
                0L
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun acquireAudioRecord(outBufSize: IntArray): AudioRecord? {
        if (!isRecordAudioPermissionGranted()) {
            KLog.d(TAG, "[acquireAudioRecord] no permission")
            return null
        }
        return try {
            val minBufferSize = AudioRecord.getMinBufferSize(11025, 2, 2)
            val audioRecord = AudioRecord(1, 11025, 2, 2, minBufferSize)
            audioRecord.startRecording()
            synchronized(mRecordList) {
                mRecordList.add(audioRecord)
            }
            if (outBufSize.isNotEmpty()) {
                outBufSize[0] = minBufferSize
            }
            audioRecord
        } catch (t: Throwable) {
            KLog.e(TAG, "[acquireAudioRecord] exception: ", t)
            null
        }
    }

    private fun acquireAudioTrack(outBufSize: IntArray): AudioTrack? {
        return try {
            val minBufferSize = AudioTrack.getMinBufferSize(44100, 3, 2)
            @Suppress("DEPRECATION")
            val audioTrack = AudioTrack(3, 44100, 3, 2, minBufferSize, 1)
            audioTrack.setVolume(if (mMute) 0.0f else AudioTrack.getMaxVolume())
            audioTrack.play()
            synchronized(mTrackList) {
                mTrackList.add(audioTrack)
            }
            if (outBufSize.isNotEmpty()) {
                outBufSize[0] = minBufferSize
            }
            audioTrack
        } catch (t: Throwable) {
            KLog.e(TAG, "[acquireAudioTrack] exception: ", t)
            null
        }
    }

    private fun clearAudioRecord() {
        synchronized(mRecordList) {
            val it = mRecordList.iterator()
            while (it.hasNext()) {
                try {
                    it.next().release()
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
            mRecordList.clear()
        }
    }

    private fun clearAudioTrack() {
        synchronized(mTrackList) {
            val it = mTrackList.iterator()
            while (it.hasNext()) {
                try {
                    it.next().release()
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
            mTrackList.clear()
        }
    }

    private fun isRecordAudioPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            mContext,
            "android.permission.RECORD_AUDIO"
        ) == PackageManager.PERMISSION_GRANTED
    }

    private external fun nativeDispose(nativePtr: Long)
    private external fun nativeSetup(vmId: Int): Long
    private external fun nativeStartService(nativePtr: Long): Int
    private external fun nativeStopService(nativePtr: Long): Int

    private fun readRecordData(audioRecord: AudioRecord?, buffer: ByteArray, offset: Int, size: Int): Int {
        if (audioRecord == null) return 0
        return try {
            audioRecord.read(buffer, offset, size)
        } catch (t: Throwable) {
            t.printStackTrace()
            0
        }
    }

    private fun releaseAudioRecord(audioRecord: AudioRecord?) {
        if (audioRecord == null) return
        synchronized(mRecordList) {
            mRecordList.remove(audioRecord)
        }
        try {
            audioRecord.release()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun releaseAudioTrack(audioTrack: AudioTrack?) {
        if (audioTrack == null) return
        synchronized(mTrackList) {
            mTrackList.remove(audioTrack)
        }
        try {
            audioTrack.release()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun writeAudioData(audioTrack: AudioTrack?, buffer: ByteArray, offset: Int, size: Int): Int {
        if (audioTrack == null) return 0
        return try {
            audioTrack.write(buffer, offset, size)
        } catch (t: Throwable) {
            t.printStackTrace()
            0
        }
    }

    protected fun finalize() {
        val ptr = mNativePtr
        if (ptr != 0L) {
            try {
                nativeDispose(ptr)
            } catch (_: Throwable) {
            }
            mNativePtr = 0L
        }
    }

    fun isMute(): Boolean = mMute

    fun setMute(mute: Boolean) {
        mMute = mute
        synchronized(mTrackList) {
            for (track in mTrackList) {
                track.setVolume(if (mMute) 0.0f else AudioTrack.getMaxVolume())
            }
        }
    }

    fun start(): Int {
        if (mNativePtr == 0L) return 0
        return try {
            nativeStartService(mNativePtr)
        } catch (t: Throwable) {
            0
        }
    }

    fun stop(): Int {
        clearAudioTrack()
        clearAudioRecord()
        if (mNativePtr == 0L) return 0
        return try {
            nativeStopService(mNativePtr)
        } catch (t: Throwable) {
            0
        }
    }

    fun toggleMute() {
        setMute(!mMute)
    }

    companion object {
        private const val TAG = "AudioService"
    }
}
