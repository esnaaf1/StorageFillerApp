package com.example.storagefillerapp

import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.storagefillerapp.ui.theme.StorageFillerAppTheme
import java.io.File
import java.io.IOException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

//            ActivityCompat.requestPermissions(this,
//                new String)
            StorageFillerAppTheme {

                ShowAvailableSpace()


            }
        }
    }
}

@Composable
fun ShowAvailableSpace( modifier: Modifier = Modifier) {

    var AvailableMB by remember {
        mutableStateOf(getAvailableInternalMemorySize()) }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {


        Row ( modifier = Modifier
            .fillMaxWidth()){

//            AvailableBytes = getAvailableInternalMemorySize() /1024/1024

            Text(
                text = "The Available Internal Storage is $AvailableMB MB",
                modifier = modifier
            )
        }

        Button(onClick = {
//           var filesize = writeFile(getAvailableInternalMemorySize())
//            AvailableBytes -= filesize
              writeFile(AvailableMB)
//            writeTextFile()
            AvailableMB = getAvailableInternalMemorySize()

        }) {
            Text(text = "Fill The Storage")
        }
    }
}

fun getAvailableInternalMemorySize(): Long {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    val blockSize = stat.blockSizeLong
    val availableBlocks = stat.availableBlocksLong
    // return the available space in Mega Bytes
    return (availableBlocks * blockSize)/1024/1024
}

fun writeFile(freeSpace: Long) {
    /* We want to set an arbitrary limit of ~50 MB of free
     disk space.
     */

    try {
        // set the maximum file size
        var maxFileSize = freeSpace - 50
        val fileName = "/storage/emulated/0/Download/test.txt"
        val file = File(fileName)
        val oneMB = 1024 * 1024
        var buffer = ByteArray(oneMB)
        if (maxFileSize > 0) {
            file.writeBytes(buffer)
            for (i in 1..maxFileSize) {
                file.appendBytes(buffer)
            }
        }
    } catch (e:IOException) {
        println("File Error!")
    }
}

