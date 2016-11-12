package com.SiegeNetworks;

import SiegeNetworks.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;


public class Controller {
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

    public Controller() throws IOException {};

    public void onClickBack(ActionEvent actionEvent)
    {
        /*
        Task task = new Task<Void>()
        {
            @Override public Void call()
            {
        */
        try
        {
            int portAddr=Integer.parseInt(input_Port.getText());
            NETConnection_Files newConnection=new NETConnection_Files(portAddr,input_Address.getText());
            newConnection.init(portAddr,input_Address.getText());

            label_StatusBar.setText("Sending some sincere greetings.");
            newConnection.sendSentence("Hi!");

            String tmpString=newConnection.getSentence();
            if (!tmpString.equals("Hi!"))
            {
                label_StatusBar.setText("[ERROR] Server speaks some foreign language.");
                throw new IOException("[ERROR] The received data has unexpected content.");
            }

            label_StatusBar.setText("Cool! Server just said us hello. We want to send a file...");
            newConnection.sendSentence("filerecv");

            tmpString=newConnection.getSentence();
            if (!tmpString.equals("filesend"))
            {
                label_StatusBar.setText("[ERROR] Server speaks some foreign language.");
                return;
            }

            label_StatusBar.setText("Server said he is ready to accept the file...");
            newConnection.loadSendFile(input_filePath.getText());

            newConnection.sendSentence(newConnection.dcm_File.getName());
            tmpString=newConnection.getSentence();
            if (!tmpString.equals("OK"))
            {
                label_StatusBar.setText("[ERROR] Server speaks some foreign language.");
                return;
            }

            newConnection.sendSentence(chbx_DoReceive.isSelected()?"1":"0");

            tmpString=newConnection.getSentence();
            if (!tmpString.equals("OK"))
            {
                label_StatusBar.setText("[ERROR] Server speaks some foreign language.");
                return;
            }
            newConnection.sendFile();
            label_StatusBar.setText("File sent.");
            newConnection.deInit();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    } //;
        //new Thread(task).start();

    public void onClickBrowse(ActionEvent actionEvent)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
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
