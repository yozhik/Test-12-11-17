package com.example.serhii.githubio;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Serhii on 11/12/2017.
 */

public class CacheManager {

    public static void WriteToCacheFile(String fileName, String data, Context context)
    {
        FileOutputStream outputStream;
        File f = new File(context.getCacheDir(), fileName);

        try {
            outputStream = new FileOutputStream(f);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String ReadFromCacheFile(String fileName, Context context)
    {
        FileInputStream inputStream;
        StringBuffer fileContent = new StringBuffer();
        File f = new File(context.getCacheDir(), fileName);

        try {
            inputStream = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            int readBytes;
            while ((readBytes = inputStream.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, readBytes));
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }
}