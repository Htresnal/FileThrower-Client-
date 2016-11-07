package com.SiegeNetworks;

import SiegeNetworks.NETConnection_Files;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;


public class Controller {
    NETConnection_Files newConnection;
    @FXML
    javafx.scene.control.TextField input_Address;

    @FXML
    javafx.scene.control.TextField input_Port;

    @FXML
    javafx.scene.control.CheckBox chbx_DoReceive;

    @FXML
    javafx.scene.control.ProgressIndicator progr_Upload;

    @FXML
    javafx.scene.control.Label label_StatusBar;

    @FXML
    javafx.scene.control.Button btn_Browse;

    @FXML
    javafx.scene.control.TextField input_filePath;

    public Controller() throws IOException {}

    public void onClickBack(ActionEvent actionEvent) {
        int portNumber = Integer.parseInt(input_Port.getText());
        try {
            newConnection=new NETConnection_Files(portNumber,input_Address.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Platform.runLater ( () -> label_StatusBar.setText("FUCKR"));
        /*
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    label_StatusBar.setText("FUCKR");
                }
        });
        */

        try
        {
            System.out.println("Sending some sincere greetings.");
            newConnection.sendSentence("Hi!");

            String tmpString=newConnection.getSentence();
            if (!tmpString.equals("Hi!"))
            {
                throw new IOException("[ERROR] Server speaks some foreign language.");
            }

            System.out.println("Cool! Server just said us hello. We want to send a file...");
            newConnection.sendSentence("filerecv");

            tmpString=newConnection.getSentence();
            if (!tmpString.equals("filesend"))
            {
                System.out.println("[ERROR] Server speaks some foreign language.");
                return;
            }

            System.out.println("Cool! Server just said us hello. We want to send a file...");
            newConnection.sendSentence("filesend");

            tmpString=newConnection.getSentence();
            if (!tmpString.equals("filerecv"))
            {
                System.out.println("[ERROR] Server speaks some foreign language.");
                return;
            }
            System.out.println("Server said he is ready to accept the file...");
            newConnection.createSendFile(args[2]);
            long fileSize = newConnection.getFile().length();
            String fileName = newConnection.getFile().getName();
            System.out.println("Sending file data: fileName: " + fileName + ", Size:" + fileSize);
            newConnection.sendFile(); // sendFile(args[2]) can be used, but is avoided to get to the file name\size.

            tmpString=newConnection.getSentence();
            if (!tmpString.equals("OK"))
            {
                System.out.println("[ERROR] Server speaks some foreign language.");
                return;
            }
            System.out.println("File sent.");
            newConnection.deInit();
        }
        catch(IOException e)
        {
            throw e;
        }
    }

    public void onClickBrowse(ActionEvent actionEvent)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("All Files", "*.*"),
        new FileChooser.ExtensionFilter("Text Files", "*.txt"),
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
        new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
        File selectedFile = fileChooser.showOpenDialog(btn_Browse.getScene().getWindow());
        if (selectedFile != null) {
           input_filePath.setText(selectedFile.getAbsolutePath());
        }
    }
}
