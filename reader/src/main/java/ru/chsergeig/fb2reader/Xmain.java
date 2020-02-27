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
import java.util.Arrays;
import java.util.stream.Collectors;

public class Xmain extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {

//        ImageIO.read(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(book.select("binary").first().text().replaceAll("\\s*", ""))));

        this.primaryStage = primaryStage;

        Thread.setDefaultUncaughtExceptionHandler(this::showExceptionDialog);
        Parent root = FXMLLoader.load(Xmain.class.getClassLoader().getResource("main_window.fxml"));
        Scene scene = new Scene(root, 300, 300);
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add(Xmain.class.getClassLoader().getResource("css/application.css").toString());

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void showExceptionDialog(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        StringBuilder sb = new StringBuilder();
        do {
            if (sb.length() != 0) {
                sb.append("Caused by:\n");
            }
            sb.append(getExceptionInfo(throwable));
            sb.append("\n");
            throwable = throwable.getCause();
        } while (throwable.getCause() != null && throwable != throwable.getCause());
        Label label = new Label(thread.getName());
        TextArea textArea = new TextArea();
        textArea.setText(sb.toString());
        textArea.setMinSize(900, 500);
        VBox dialogPaneContent = new VBox();
        dialogPaneContent.getChildren().addAll(label, textArea);
        alert.setResizable(true);
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.showAndWait();
    }

    private String getExceptionInfo(Throwable throwable) {
        return throwable.getMessage() + "\n"
                + Arrays.stream(throwable.getStackTrace())
                .map(e -> "\t" + e.toString())
                .collect(Collectors.joining("\n"));
    }

}
