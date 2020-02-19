package ru.chsergeig.fb2reader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class Xmain extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException {

        Main.main(null);

        this.primaryStage = primaryStage;

        Thread.setDefaultUncaughtExceptionHandler(this::showExceptionDialog);
        Parent root = FXMLLoader.load(Xmain.class.getClassLoader().getResource("reader.fxml"));
        Scene scene = new Scene(root, 300, 300);
        scene.setFill(Color.WHITE);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    private void showExceptionDialog(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        String message = throwable.getMessage();
        Label label = new Label(thread.getName());
        TextArea textArea = new TextArea();
        textArea.setText(message);
        textArea.setMinSize(900, 500);
        VBox dialogPaneContent = new VBox();
        dialogPaneContent.getChildren().addAll(label, textArea);
        alert.setResizable(true);
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.showAndWait();
    }
}
