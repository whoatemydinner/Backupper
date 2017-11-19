/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backupper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Piotr
 */
public class Client {
    public static void main(String[] args){
        // ladowanie ustawien klienta z pliku
        Properties props = new Properties();
        InputStream input = null;
        String ip = "localhost";
        int portnum = 1000;
        
        try {
            input = new FileInputStream("client.properties");
            props.load(input);
            ip = props.getProperty("ip");
            portnum = Integer.parseInt(props.getProperty("ip"));
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
        
        
    }
}
