package org.me.gcu.kristensen_svend_s1826091;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import android.util.Log;
//NAME SVEND KRISTENSEN
//STUDENTID : S1820691
public class Downloader {

    public static void DownloadFromUrl(String URL, FileOutputStream fos) {
        try {

            URL url = new URL(URL); //URL of one of the traffic scotlands RSS feeds.
            URLConnection ucon = url.openConnection();
            int lengthOfFile = ucon.getContentLength(); // used to debug and ensure a file was being downloaded

            //set up inputs streams to read file
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);


            //initialize output stream
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            //reading and writing data
            byte data[] = new byte[1024];

            int count;

            while ((count = bis.read(data)) != -1) {

                bos.write(data, 0, count);
            }
            //flush the output stream before close to ensure it does not corrupt.
            bos.flush();
            bos.close();

        } catch (IOException e) {
            Log.d("Downloader", "Error: " + e);
        }
    }
}
