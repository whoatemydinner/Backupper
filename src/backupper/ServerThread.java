/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backupper;

import java.io.*;
import java.net.*;

/**
 *
 * @author Piotr
 */
public class ServerThread implements Runnable {
    private Socket csock;
    
    public ServerThread(Socket csock){
        this.csock=csock;
    }
    
    @Override
    public void run(){
        try {
            System.out.println("Wątek uruchomiony.");
            while (true) {
            InputStream is = csock.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
  //          OutputStream os = csock.getOutputStream();
   //         PrintWriter pw = new PrintWriter(os, true);
   /**
    * Teoretycznie - serwer nasłuchuje na słowa-klucze (komendy) i potem odbiera i robi co ma robić z danymi. Niestety przesyłanie nazw plików (FILENAME) nie działa, a samych plików tak pół na pół.
    */
            String clientmessage = br.readLine();
            if (clientmessage!=null){
                if (clientmessage.equals("FILENAME")){
                    String lol;
                    System.out.println("Nazwa pliku jest otrzymywana...");
                    lol = FileHandler.receiveFileName(csock);
                    System.out.println("Nazwa pliku otrzymana.");
                }
                if (clientmessage.equals("BRACEFORFILES")){
                    System.out.println("Następuje otrzymywanie pliku.");
                    FileHandler.receivefile(csock);
                }
            }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
