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
    public static File currentFile;
    public static ArrayList<ArchivedFile> archived = new ArrayList<ArchivedFile>();
    
    /**
     * Metoda implementująca samo załadowanie listy z pliku do pamięci, dzęiki czemu wiemy, które pliki archiwizujemy.
     * @throws IOException 
     */
    public static void loadList() throws IOException{
        try {
        FileInputStream fis = new FileInputStream("list.tmp");
        ObjectInputStream ois = new ObjectInputStream(fis);
        archived = (ArrayList<ArchivedFile>) ois.readObject();
        ois.close();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Metoda aktualizująca listę archiwizowanych plików.
     * @throws IOException 
     */
    public static void updateList() throws IOException{
        FileOutputStream fos = new FileOutputStream("list.tmp");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(archived);
        oos.close();
    }
    
    /**
     * Metoda czyszcząca listę archiwizowanych plików. TODO: Usuwanie pojedynczych plików z listy, co nie powinno być problemem, bo to lista po prostu.
     */
    public static void emptyList(){
        archived.clear();
    }
    
    /**
     * Dodawanie pliku do listy.
     * @param af 
     */
    public static void addToList(ArchivedFile af){
        archived.add(af);
    }
    
    /**
     * Wysyłanie całej listy plików. Mozna włożyć proste sprawdzanie, że jak wszystkie parametry od serwera się zgadzają (tylko trzeba je uzyskać...) to można plik pominąć.
     * @throws UnknownHostException 
     */
    public static void sendFiles() throws UnknownHostException{
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket ssock = null;
        
        Iterator itr=Client.archived.iterator(); 
        while(itr.hasNext()){
            int i = 0;
            try{
                ssock = new Socket(ip, portnum);
                System.out.println(ip + " " + portnum);
                ArchivedFile af = (ArchivedFile)itr.next();
                File cf = new File(af.path);
                os = ssock.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                //pw.println("FILENAME");
                //FileHandler.sendFileName(af.name, ssock);
                //os = ssock.getOutputStream();
                pw.println("BRACEFORFILES");
                FileHandler.sendfile(fis, bis, os, ssock, cf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        
        //try {
            // odpalamy okno klienta, zapraszam do ClientWindow po szczegóły
            new ClientWindow().setVisible(true);
            
        /*} finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (ssock != null) ssock.close();
        }*/
        
    }
}
