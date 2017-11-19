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
            File gf = new File(fileToSave);
            FileHandler.receivefile(ssock, fos, bos, gf);
            ssock.close();
            System.out.println("Test z wysyłaniem pliku...");
            ssock = new Socket(ip, portnum);
            FileHandler.sendfile(fis,bis,os,ssock,mf);
            
        } finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (ssock != null) ssock.close();
        }
        
    }
}
