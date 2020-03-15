package com.example.downloadsong

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_paly.*
import java.io.File


class PalyActivity : AppCompatActivity() {

    private  var myMediaPlayer= MediaPlayer()
    var pos = 0

    private val seekForwardTime = 10* 1000 // default 10 second

    private val seekBackwardTime = 10 * 1000 // default 10 second


    private lateinit var mySong: ArrayList<File>

    private lateinit var updateSeekbar: Thread

    private var sName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paly)
        init()
    }

    private fun init() {
        updateSeekBar()
    }

    private fun getSong() {

    }

    private fun play() {

    }

    private fun updateSeekBar() {

        updateSeekbar = Thread() {
                val duration = myMediaPlayer.duration
                var currentDuration = 0
                while (currentDuration < duration) {
                    try {
                        currentDuration = myMediaPlayer.currentPosition
                        sbPlay.progress = currentDuration

                    } catch (e: InterruptedException) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (myMediaPlayer != null) {
                myMediaPlayer.stop()
                myMediaPlayer.release()
            }
            val i = intent
            val bundle = i.extras
            mySong = bundle?.getParcelableArrayList<Parcelable>("Song") as ArrayList<File>
            sName = mySong.get(pos).name.toString()
            var songName = i.getStringExtra("SongName")
            tvSongName.text = songName
            tvSongName.isSelected = true

            pos = bundle.getInt("Position", 0)

            var uri = Uri.parse(mySong.get(pos).toString())
            myMediaPlayer = MediaPlayer.create(applicationContext, uri)
            myMediaPlayer.start()
            sbPlay.max = myMediaPlayer.duration

            updateSeekbar.start()

            sbPlay.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        myMediaPlayer.seekTo(seekBar!!.progress)
                }
            })
        ivPause.setOnClickListener {
            sbPlay.max = myMediaPlayer.duration
            if (myMediaPlayer.isPlaying) {
                myMediaPlayer.pause()
            }
        }

        ivPlay.setOnClickListener {
            if (myMediaPlayer.isPlaying){

            }else{
                myMediaPlayer.start()
            }
        }

        ivNext.setOnClickListener {
            myMediaPlayer.stop()
            myMediaPlayer.release()
            pos = ((pos + 1) % mySong.size)

            val u = Uri.parse(mySong.get(pos).toString())

            myMediaPlayer = MediaPlayer.create(applicationContext, u)
            sName = mySong.get(pos).name.toString()
            tvSongName.text = sName

            myMediaPlayer.start()
            updateSeekbar.start()
        }
        ivprevious.setOnClickListener {
            myMediaPlayer.stop()
            myMediaPlayer.release()
            pos = if (((pos - 1) < 0)) (mySong.size - 1) else (pos - 1)

            val u = Uri.parse(mySong.get(pos).toString())

            myMediaPlayer = MediaPlayer.create(applicationContext, u)
            sName = mySong.get(pos).name.toString()
            tvSongName.text = sName

            myMediaPlayer.start()
            updateSeekbar.start()
        }

        iv10Reward.setOnClickListener {
            rewindSong()
        }
        iv10Forward.setOnClickListener {
            forwardSong()
        }

    }

    override fun onStop() {
        super.onStop()
        myMediaPlayer.stop()
        myMediaPlayer.release()
    }

    fun forwardSong() {
        if (myMediaPlayer != null) {
            val currentPosition: Int = myMediaPlayer.getCurrentPosition()
            if (currentPosition + seekForwardTime <= myMediaPlayer.getDuration()) {
                myMediaPlayer.seekTo(currentPosition + seekForwardTime)
            } else {
                myMediaPlayer.seekTo(myMediaPlayer.getDuration())
            }
        }
    }


    fun rewindSong() {
        if (myMediaPlayer != null) {
            val currentPosition: Int = myMediaPlayer.getCurrentPosition()
            if (currentPosition - seekBackwardTime >= 0) {
                myMediaPlayer.seekTo(currentPosition - seekBackwardTime)
            } else {
                myMediaPlayer.seekTo(0)
            }
        }
    }
}
