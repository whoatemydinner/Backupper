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
    public static String ip = "localhost";
    public static int portnum = 1000;
    public static String currentFileName = "";
    
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
        //System.out.println(ip);
        //System.out.println(portnum);
        
        /*
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket ssock = null;
        Socket csock = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        
        */
        
        
        //try {
            ServerSocket ssock = new ServerSocket(portnum);
            System.out.println("Witam, serwer z tej strony, czekam na połączenia.");
            while(true) {
                Socket csock = ssock.accept();
                new Thread(new ServerThread(csock)).start();
                
                /*
                try {
                    csock = ssock.accept();
                    /*System.out.println("Zaakceptowane połączenie od klienta: "+ csock);
                    System.out.println("Co chcesz teraz zrobić?");
                    System.out.println("[1] Wyslac plik");
                    System.out.println("[2] Odebrac plik");
                    Scanner scan = new Scanner(System.in);
                    int opt = scan.nextInt();
                    if (opt == 1){
                        File mf = FileChooser.fileChooser();
                        System.out.println("Test wysylania pliku...");
                        FileHandler.sendfile(fis, bis, os, csock, mf);
                    //} else {
                        File gf = new File(fileToSave);
                        System.out.println("Test odbierania pliku...");
                        FileHandler.receivefile(csock, fos, bos, gf);
                    //}
                    csock.close();
                    
                } finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (csock!=null) csock.close();
                }*/
            }
        //} finally {
        //    if (ssock != null) ssock.close();
        }
        
        
    }

