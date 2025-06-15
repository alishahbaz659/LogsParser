package com.example.LogsParser.controller;

import com.example.LogsParser.factory.ServiceFactory;
import com.example.LogsParser.model.LogMessage;
import com.example.LogsParser.model.Pipeline;
import com.example.LogsParser.service.LogParsingService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
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

    private LogParsingService logParsingService;
    private ServiceFactory serviceFactory;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeServices();
        setupUI();
    }

    private void initializeServices() {
        serviceFactory = createServiceFactory();
        logParsingService = serviceFactory.createLogParsingService();
    }

    protected ServiceFactory createServiceFactory() {
        return new ServiceFactory();
    }


    private void setupUI() {
        statusLabel.setText("Ready - Load a file or paste log content");
    }

    @FXML
    private void handleLoadFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Log File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.log")
            );

            File file = fileChooser.showOpenDialog(loadFileButton.getScene().getWindow());
            if (file != null) {
                String content = Files.readString(file.toPath());
                inputArea.setText(content);
                updateStatus("File loaded: " + file.getName());
            }
        } catch (Exception e) {
            showError("Failed to load file", e.getMessage());
        }
    }

    @FXML
    private void handleParseLogs() {
        String inputText = inputArea.getText().trim();
        if (inputText.isEmpty()) {
            showError("No Input", "Please enter log content or load a file first.");
            return;
        }
        try {
            updateStatus("Parsing logs...");
            List<Pipeline> pipelines = logParsingService.parseLogContent(inputText);
            if (pipelines.isEmpty()) {
                outputArea.setText("No valid pipelines found. Check your input format.\n\nExpected format:\npipeline_id id encoding [body] next_id");
                updateStatus("No pipelines found");
                return;
            }
            String formattedOutput = formatPipelinesOutput(pipelines);
            outputArea.setText(formattedOutput);
            updateStatus("Parsed " + pipelines.size() + " pipelines successfully");
        } catch (Exception e) {
            showError("Parsing Error", "Failed to parse logs: " + e.getMessage());
            updateStatus("Parsing failed");
        }

    }

    private String formatPipelinesOutput(List<Pipeline> pipelines) {
        StringBuilder output = new StringBuilder();
        output.append("Output\n");
        output.append("─".repeat(50)).append("\n");

        for (Pipeline pipeline : pipelines) {
            output.append("Pipeline ").append(pipeline.getId()).append("\n");

            for (LogMessage msg : pipeline.getMessages()) {
                output.append("    ").append(msg.getId()).append("| ").append(msg.getContent()).append("\n");
            }
        }

        return output.toString();
    }

    @FXML
    private void handleClear() {
        inputArea.clear();
        outputArea.clear();
        updateStatus("Cleared - Ready for new input");
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
