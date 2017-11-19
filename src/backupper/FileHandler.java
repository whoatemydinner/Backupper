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
public class FileHandler {
    public static void sendFileName(String s, Socket ssock) throws IOException{
        PrintStream ps = new PrintStream(ssock.getOutputStream());
        ps.print(s);
        /*OutputStreamWriter osw;
        System.out.println(s);
        try {
            osw = new OutputStreamWriter(ssock.getOutputStream(), "UTF-8");
            osw.write(s, 0, s.length());
            System.out.println("kys");
            osw.flush();
        } catch (Exception e) {
            System.err.print(e);
        }
        */
    }
    
    public static String receiveFileName(Socket csock) throws IOException{
        String s;
        Scanner sc = new Scanner(csock.getInputStream());
        System.out.println("1");
        s = sc.next();
        System.out.println("2");
        return s;
    }
    
    public static void receivefile(Socket ssock/*, FileOutputStream fos, BufferedOutputStream bos, File mf*/) throws IOException{
        // póki nie udało się naprawić interfejsu z wysyłaniem nazw plików losuje nazwy XDDDD
        Random generator = new Random(); 
        int i = generator.nextInt(10) + 20;
        int count = 0;
        File mf = new File("" + i);
        // sprobujmy otrzymac plik
        byte[] bytearray = new byte[4096];
        InputStream is = ssock.getInputStream();
        FileOutputStream fos = new FileOutputStream(mf);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        while ((count = is.read(bytearray)) >= 0){
            bos.write(bytearray,0,count);
        }
        bos.flush();
        System.out.println("Plik otrzymany. Trzymaj się ramy, to się nie posramy.");
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
        os.write(bytearray,0,bytearray.length);
        // spuszczamy bajty
        os.flush();
        System.out.println("Zrobione. Z fartem, mordo.");
    }
}
