/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backupper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Piotr
 */
public class FileHandler {
    public static void receivefile(Socket ssock, FileOutputStream fos, BufferedOutputStream bos, File mf) throws IOException{
        int count = 0;
        // sprobujmy otrzymac plik
        byte[] bytearray = new byte[4096];
        InputStream is = ssock.getInputStream();
        fos = new FileOutputStream(mf);
        bos = new BufferedOutputStream(fos);
        while ((count = is.read(bytearray)) >= 0){
            bos.write(bytearray,0,count);
        }
        bos.flush();
        System.out.println("Plik zostal zapisany pod nazwa " + mf + " (" + count + "b).");
    }
    
    public static void sendfile(FileInputStream fis, BufferedInputStream bis, OutputStream os, Socket csock, File mf) throws IOException{
        int count = 0;
        // ustalamy dlugosc pliku
        byte[] bytearray = new byte[4096];
        fis = new FileInputStream(mf);
        bis = new BufferedInputStream(fis);
        os = csock.getOutputStream();
        while ((count = bis.read(bytearray)) >= 0) {
            os.write(bytearray, 0, count); 
        }
        System.out.println("Wysyłanie pliku " + mf + "(wielkość " + bytearray.length + "b)");
        os.write(bytearray,0,bytearray.length);
        // spuszczamy bajty
        os.flush();
        System.out.println("Zrobione. Z fartem, mordo.");
    }
}
