package com.example.contentproviderr;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class Sevicerr {
    public Sevicerr() {
    }

    ArrayList<HashMap<String,String>> getPlayList(String rootPath) {
        ArrayList<HashMap<String,String>> fileList = new ArrayList<>();
        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", file.getAbsolutePath());
                    song.put("file_name", file.getName());
                    fileList.add(song);
                }
           }
            Log.d("XXX", fileList.size()+"");
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }
    /*ArrayList<HashMap<String,String>> songList=getPlayList("/storage/sdcard1/");
        if(songList!=null){
        for(int i=0;i<songList.size();i++){
            String fileName=songList.get(i).get("file_name");
            String filePath=songList.get(i).get("file_path");
            //here you will get list of file name and file path that present in your device
            log.e("file details "," name ="+fileName +" path = "+filePath);
        }
    }*/
    /*Lưu ý: sử dụng "/storage/dcard1/" để đọc tệp từ sdCard
    và sử dụng Environment.getExternalStorageDirectory(). GetAbsolutePath() để đọc tệp từ bộ nhớ điện thoại*/
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
