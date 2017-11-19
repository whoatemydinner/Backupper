/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backupper;

import java.io.File;
import javax.swing.*;

/**
 * To jest tak oczywiste, że potraktuję Cię jak poważną osobę i nie napiszę, że to po protu okienko wyboru pliku.
 * @author Piotr
 */
public class FileChooser {
    public static File fileChooser() {
        JButton open = new JButton();
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Wybierz plik");
        if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION){
            //
        }
        return fc.getSelectedFile();
    }
}
