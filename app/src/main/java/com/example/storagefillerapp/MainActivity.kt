package com.example.storagefillerapp

import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Available storage
    var AvailableMB by remember {
        mutableStateOf(getAvailableInternalMemorySize()) }

    Column {
        Box(modifier = Modifier
            .width(400.dp)
            .height(70.dp)
            .border(2.dp, Color.Blue))
        {
            Text(text = "Storage Filler App",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center))
        }

        Box(modifier = Modifier
            .width(400.dp)
            .height(150.dp)
            .border(2.dp, Color.Blue))
        {
            Text (text = "This app attemps to fill the diskspace by creating " +
                    "a large file up to an arbitrary threshold set by the app ( approx. 50 MB). " +
                    "Click the Add File button to add the file, click the Remove file to " +
                    "delete the file." +
                    " The row below shows the current avaiable storage and it is updated" +
                    " when a file is added or deleted.",
            )
        }

        Box(modifier = Modifier
            .width(400.dp)
            .height(60.dp)
            .border(2.dp, Color.Blue)) {

            Row {

                Text(text = "The Current Available Storage is: ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold)

                Text(text = "$AvailableMB MB",
                    fontSize = 20.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold)
            }
        }

        Box(modifier = Modifier
            .width(400.dp)
            .height(50.dp)
            .border(2.dp, Color.Blue)) {
            Row (modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround) {

                // Add a file button
                Button(onClick = {
                    // Write a file
                    writeFile(AvailableMB)

                    //Update the available space
                    AvailableMB = getAvailableInternalMemorySize()

                }) {
                    Text(text = "Add File")
                }

                // Delete a file button
                Button(onClick = {
                    val file = File("/storage/emulated/0/Download/test.txt")
                    if (file.exists()) {
                        try {
                            file.delete()
                        } catch (e: IOException) {
                            println("File Delete Error!")
                        }
                        // Update the available diskspace
                        AvailableMB = getAvailableInternalMemorySize()
                    }
                 }) {
                    Text(text = "Delete File")
                }
            }

        }
        Box(modifier = Modifier
            .width(400.dp)
            .height(400.dp)
            .border(2.dp, Color.Blue))
        {
           Column {
               Text(text = "Known limitations:")
               Text(text = " 1)It does not always fill the disk to the exact specified threshold (50 MB)." +
                       " This could be due to rouding and other resource usage. ")
               Text(text = " 2) Add File is not optimized for performance, so it might take a bit longer" +
                       " than expected.")
           }


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
    val spaceThreshold = 50

    try {
        // set the maximum file size
        var maxFileSize = freeSpace - spaceThreshold
        val fileName = "/storage/emulated/0/Download/test.txt"
        val file = File(fileName)
        val oneMB = 1024 * 1024
        var buffer = ByteArray(oneMB)
        if (maxFileSize > 0) {
//            file.writeBytes(buffer)
            for (i in 1..maxFileSize) {
                file.appendBytes(buffer)
            }
        }
    } catch (e:IOException) {
        println("File Error!")
    }
}

