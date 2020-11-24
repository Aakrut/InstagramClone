package com.ex.instagramclone.util

import java.io.File

class FileSearch {

    fun getDirectoryPaths(directory: String?): ArrayList<String>? {
        val pathArray: ArrayList<String> = ArrayList()
        val file = File(directory)
        val listfiles: Array<File> = file.listFiles()
        for (i in listfiles.indices) {
            if (listfiles[i].isDirectory()) {
                pathArray.add(listfiles[i].getAbsolutePath())
            }
        }
        return pathArray
    }


    fun getFilePaths(directory: String?): ArrayList<String>? {
        val pathArray: ArrayList<String> = ArrayList()
        val file = File(directory)
        val listfiles: Array<File> = file.listFiles()
        for (i in listfiles.indices) {
            if (listfiles[i].isFile()) {
                pathArray.add(listfiles[i].getAbsolutePath())
            }
        }
        return pathArray
    }
}