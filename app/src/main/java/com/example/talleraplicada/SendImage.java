package com.example.talleraplicada;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class SendImage {

    public static final String IP_SERVER = "192.168.1.12";

    public static final int PORT_SERVER = 7000;

    public static boolean SUCCESFUL_CONNECTION = true;

    public boolean sendImage(final String filepath, final String ipAddress, final String option){


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                try {

                    Socket connect = new Socket(ipAddress, PORT_SERVER);

                    DataOutputStream dos = new DataOutputStream(connect.getOutputStream());

                    String base64Image = getImageInBase64String(filepath);

                    String[] imageParts = splitStringInTwoHalf(base64Image);

                    String entero = option+"."+imageParts.length;

                    dos.write(entero.getBytes());

                    for(int i = 0; i < imageParts.length; i++){
                        dos.write(imageParts[i].getBytes());
                    }

                    dos.close();

                    connect.close();

                    SUCCESFUL_CONNECTION = true;

                } catch (IOException e) {
                    e.printStackTrace();
                    SUCCESFUL_CONNECTION = false;
                }

                return null;
            }

        }.execute();

        return SUCCESFUL_CONNECTION;
    }

    private String[] splitStringInTwoHalf(String str){

        int substringSize = 10000;

        int totalSubstrings =
                (int) Math.ceil( (double)str.length()/substringSize );

        //use ArrayList or String array
        String[] strSubstrings = new String[totalSubstrings];

        int index = 0;
        for(int i=0; i < str.length(); i = i + substringSize){

            strSubstrings[index++] =
                    str.substring(i, Math.min(i + substringSize, str.length()));
        }

        return strSubstrings;


    }


    private byte[] getImageInBytes(String filepath){

        File imagefile = new File(filepath);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Bitmap bm = BitmapFactory.decodeStream(fis);

        byte[] image = getBytesFromBitmap(bm);

        return image;

    }

    private String getImageInBase64String(String filepath){

        File imagefile = new File(filepath);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Bitmap bm = BitmapFactory.decodeStream(fis);

        String imageInString = getString64(bm);

        return imageInString;

    }

    // convert from bitmap to byte array
    private byte[] getBytesFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        return stream.toByteArray();

    }

    private String getString64(Bitmap bitmap){
        // get the base 64 string
        String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
                Base64.NO_WRAP);

        return imgString;
    }

}
