/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backupper;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Piotr
 */
public class Client {
    public static String ip = "localhost";
    public static int portnum = 1000;
    public static String fileToSave = "plikgraficzny_otrzymany.jpg";
    public final static String fileToSend = "kombi.mp3";
    public static int maxFileSize = 6000000; // no nie jest to optymalne, tylko 6 mb
    
    public static void splitfile(File f) throws IOException {
        int partCounter = 0;
        int chunkSize = 512 * 512;
        byte[] buffer = new byte[chunkSize];

        String fileName = f.getName();

        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytes = 0;
            while ((bytes = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                String filePartName = String.format("%s.%03d", fileName, partCounter++);
                File newFile = new File(f.getParent(), filePartName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytes);
                }
            }
        }
    }
    
    private static void receivefile(Socket ssock, FileOutputStream fos, BufferedOutputStream bos) throws IOException{
        int count = 0;
        // sprobujmy otrzymac plik
        byte[] bytearray = new byte[4096];
        InputStream is = ssock.getInputStream();
        fos = new FileOutputStream(fileToSave);
        bos = new BufferedOutputStream(fos);
        while ((count = is.read(bytearray)) >= 0){
            bos.write(bytearray,0,count);
        }
        bos.flush();
        System.out.println("Plik zostal zapisany pod nazwa " + fileToSave + " (" + count + "b).");
    }
    
    private static void sendfile(FileInputStream fis, BufferedInputStream bis, OutputStream os, Socket csock, File mf) throws IOException{
        int count = 0;
        // ustalamy dlugosc pliku
        byte[] bytearray = new byte[4096];
        fis = new FileInputStream(mf);
        bis = new BufferedInputStream(fis);
        os = csock.getOutputStream();
        while ((count = bis.read(bytearray)) >= 0) {
            os.write(bytearray, 0, count); 
        }
        System.out.println("Wysyłanie pliku " + fileToSend + "(wielkość " + bytearray.length + "b)");
        os.write(bytearray,0,bytearray.length);
        // spuszczamy bajty
        os.flush();
        System.out.println("Zrobione. Z fartem, mordo.");
    }
    
    public static void main(String[] args) throws IOException{
        // ladowanie ustawien klienta z pliku
        Properties props = new Properties();
        InputStream input = null;
        
        try {
            input = new FileInputStream("client.properties");
            props.load(input);
            ip = props.getProperty("ip");
            portnum = Integer.parseInt(props.getProperty("port"));
        } catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket ssock = null;
        
        try {
            File mf = new File(fileToSend);
            //splitfile(mf);
            //File k = new File("kombi.mp3.000");
            ssock = new Socket(ip, portnum);
            System.out.println("Laczenie...");
            System.out.println("Test z otrzymywaniem pliku...");
            receivefile(ssock, fos, bos);
            ssock.close();
            System.out.println("Test z wysyłaniem pliku...");
            ssock = new Socket(ip, portnum);
            sendfile(fis,bis,os,ssock,mf);
            
        } finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (ssock != null) ssock.close();
        }
        
    }
}
