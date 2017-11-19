/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backupper;

import java.io.*;

/**
 *
 * @author Piotr
 */
public class ArchivedFile implements Serializable{
    String name;
    String path;
    long size;
    long lastmodified;
    
    ArchivedFile(String n, String p, long s, long lm){
        this.name = n;
        this.path = p;
        this.size = s;
        this.lastmodified = lm;
    }
}
