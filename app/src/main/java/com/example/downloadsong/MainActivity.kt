package com.example.downloadsong

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    var item=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        permission()
    }

    private fun permission() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)


                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                display()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }
                }).check()

    }


    private fun findSong(file: File):ArrayList<File>{
        val arrayList=ArrayList<File>()
        val files=file.listFiles()

        for (singleFile in files!!){
            if(singleFile.isDirectory() && !singleFile.isHidden){
                arrayList.addAll(findSong(singleFile))
            }else{
                if (singleFile.name.endsWith(".mp3")||singleFile.name.endsWith(".amr")||singleFile.name.endsWith(".wav")){
                    arrayList.add(singleFile)
                }
            }
        }
        return arrayList
    }

    private fun display(){
        val mySongs =findSong(Environment.getExternalStorageDirectory())
        item.addAll(mySongs.map { it.name.replace(".mp3","").replace(".amr","").replace(".wav","") })
        adapter(mySongs)
    }

    private fun adapter(song:ArrayList<File>){
        val myAdapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item)
        lvAllSong.adapter=myAdapter

        lvAllSong.setOnItemClickListener { parent, view, position, id ->
            startActivity(Intent(this,PalyActivity::class.java)
                .putExtra("Song",song)
                .putExtra("SongName",lvAllSong.getItemAtPosition(position).toString())
                .putExtra("Position",position))
        }
    }

}
