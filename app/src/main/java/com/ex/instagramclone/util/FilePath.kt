package com.ex.instagramclone.util

import android.os.Environment

class FilePath {

    //storage/emulated/0
    val PATH_ROOT_DIR : String = Environment.getStorageDirectory().path

    val PICTURES : String = "$PATH_ROOT_DIR/Pictures"

    val CAMERA : String = "$PATH_ROOT_DIR/DCIM/camera"

}