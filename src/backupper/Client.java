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
    public final static String fileToSend = "pliktekstowyklienta.txt";
    public static int maxFileSize = 6000000; // no nie jest to optymalne, tylko 6 mb
    
    private static void receivefile(Socket ssock, FileOutputStream fos, BufferedOutputStream bos) throws IOException{
        int bytesRead;
        int currentByte = 0;
        // sprobujmy otrzymac plik
        byte[] bytearray = new byte[maxFileSize];
        InputStream is = ssock.getInputStream();
        fos = new FileOutputStream(fileToSave);
        bos = new BufferedOutputStream(fos);
        bytesRead = is.read(bytearray,0,bytearray.length);
        currentByte = bytesRead;
            
        do {
            bytesRead = is.read(bytearray, currentByte, (bytearray.length-currentByte));
            if (bytesRead >= 0) currentByte += bytesRead;
        } while (bytesRead < -1);
            
        bos.write(bytearray, 0, currentByte);
        bos.flush();
        System.out.println("Plik zostal zapisany pod nazwa " + fileToSave + " (" + currentByte + "b).");
    }
    
    private static void sendfile(FileInputStream fis, BufferedInputStream bis, OutputStream os, Socket csock) throws IOException{
        // tutaj wysylamy plik od teraz, serio
        File mf = new File(fileToSend);
        // ustalamy dlugosc pliku
        byte[] bytearray = new byte[(int)mf.length()];
        fis = new FileInputStream(mf);
        bis = new BufferedInputStream(fis);
        bis.read(bytearray,0,bytearray.length);
        os = csock.getOutputStream();
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
            ssock = new Socket(ip, portnum);
            System.out.println("Laczenie...");
            System.out.println("Test z otrzymywaniem pliku...");
            receivefile(ssock, fos, bos);
            ssock.close();
            System.out.println("Test z wysyłaniem pliku...");
            ssock = new Socket(ip, portnum);
            sendfile(fis,bis,os,ssock);
            ssock.close();
        } finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (ssock != null) ssock.close();
        }
        
    }
}
