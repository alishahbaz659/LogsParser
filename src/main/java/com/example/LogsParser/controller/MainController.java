package com.example.LogsParser.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button loadFileButton;
    @FXML
    private TextArea inputArea;
    @FXML
    private TextArea outputArea;
    @FXML
    private Label statusLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
    }

    private void setupUI() {
        statusLabel.setText("Ready - Load a file or paste log content");
    }

    @FXML
    private void handleLoadFile() {

    }

    @FXML
    private void handleParseLogs() {

    }

    @FXML
    private void handleClear() {

    }
}
