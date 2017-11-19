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
public class Server {
    // plik do wyslania do klienta (poki co w te strone)
    public final static String fileToSend = "pliktekstowyserwera.txt";
    public static String fileToSave = "pliktekstowyklienta_otrzymany.txt";
    public static String ip = "localhost";
    public static int portnum = 1000;
    public static int maxFileSize = 6000000;
    
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
    
    private static void receivefile(Socket csock, FileOutputStream fos, BufferedOutputStream bos) throws IOException{
        int bytesRead;
        int currentByte = 0;
        // sprobujmy otrzymac plik
        byte[] bytearray = new byte[maxFileSize];
        InputStream is = csock.getInputStream();
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
    
    public static void main(String[] args) throws IOException{
        // ladowanie ustawien serwera z pliku
        Properties props = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("server.properties");
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
        // maly debug ;)
        System.out.println(ip);
        System.out.println(portnum);
        
        // sprobujemy wyslac plik
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket ssock = null;
        Socket csock = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        
        try {
            ssock = new ServerSocket(portnum);
            while(true) {
                System.out.println("Czekom na polaczenie");
                try {
                    csock = ssock.accept();
                    System.out.println("Zaakceptowane połączenie od klienta: "+ csock);
                    System.out.println("Test wysylania pliku...");
                    //sendfile(fis, bis, os, csock);
                    csock.close();
                    csock = ssock.accept();
                    System.out.println("Test odbierania pliku...");
                    receivefile(csock, fos, bos);
                    csock.close();
                } finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (csock!=null) csock.close();
                }
            }
        } finally {
            if (ssock != null) ssock.close();
        }
        
        
    }
}
