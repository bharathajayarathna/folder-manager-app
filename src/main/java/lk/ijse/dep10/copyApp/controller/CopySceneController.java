package lk.ijse.dep10.copyApp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class CopySceneController {

    @FXML
    private Button btnCopy;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnMove;
    @FXML
    private Button btnSource;

    @FXML
    private Button btnTarget;

    @FXML
    private TextField txtSource;

    @FXML
    private TextField txtTarget;
    private ArrayList<File> fileArrayList = new ArrayList<File>();
    private JFileChooser chooser;
    private JFileChooser chooserTgt;

    @FXML
    void btnCopyOnAction(ActionEvent event) throws IOException {

        for (int i = 0; i < fileArrayList.size(); i++) {
            File src = new File(fileArrayList.get(i).toURI());

            String str1 = chooser.getSelectedFile().getParentFile().toString() + "/";
            String str = fileArrayList.get(i).getParentFile().toString().replaceAll(str1, "");
            String str3 = (chooserTgt.getSelectedFile() + "/" + str);
            File test = new File(chooserTgt.getSelectedFile().toString(), str);
            File targetFile = new File(str3, src.getName());
            File sourceFile = new File(fileArrayList.get(i).toURI());
            try {
                targetFile.getParentFile().mkdirs();

                FileInputStream fis = new FileInputStream(sourceFile);
                FileOutputStream fos = new FileOutputStream(targetFile);

                byte[] buffer = new byte[1024 * 10];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                fis.close();
                fos.close();
            } catch (IOException e) {
                System.err.println("An error occurred while copying the file: " + e.getMessage());
            }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (chooser.getSelectedFile().isDirectory()) {
            deleteDirectory(chooser.getSelectedFile());
        } else {
            chooser.getSelectedFile().delete();
        }
    }

    private void deleteDirectory(File sourceFile) {
        File[] files = sourceFile.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirectory(file);
                file.delete();
            } else {
                file.delete();
            }
        }
        sourceFile.delete();
    }

    @FXML
    void btnMoveOnAction(ActionEvent event) throws IOException {
        for (int i = 0; i < fileArrayList.size(); i++) {
            File src = new File(fileArrayList.get(i).toURI());
            File desktopFolder = src.getParentFile();
            String str1 = chooser.getSelectedFile().getParentFile().toString() + "/";
            String str = fileArrayList.get(i).getParentFile().toString().replaceAll(str1, "");
            String str3 = (chooserTgt.getSelectedFile() + "/" + str);
            File test = new File(chooserTgt.getSelectedFile().toString(), str);
            test.mkdirs();
            File target = new File(str3, src.getName());
            src.renameTo(target);
            src.getParentFile().delete();
        }

    }

    @FXML
    void btnSourceOnAction(ActionEvent event) {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showOpenDialog(null);
        txtSource.setText(chooser.getSelectedFile().toString());
        findFiles(chooser.getSelectedFile());

    }

    private void findFiles(File path) {
        File[] files = path.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                findFiles(f);
            }
            if (fileArrayList.add(f)) ;
        }
    }

    @FXML
    void btnTargetOnAction(ActionEvent event) {
        chooserTgt = new JFileChooser();
        chooserTgt.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooserTgt.showOpenDialog(null);
        txtTarget.setText(chooserTgt.getSelectedFile().toString());
        findFiles(chooserTgt.getSelectedFile());
    }

}
