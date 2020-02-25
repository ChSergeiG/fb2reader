package ru.chsergeig.fb2reader.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.chsergeig.fb2reader.BookHolder;
import ru.chsergeig.fb2reader.Xmain;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class MainWindowController {

    @FXML
    protected GridPane gridPane;

    @FXML
    protected TextFlow main;

    @FXML
    protected MenuBar menubar;

    @FXML
    protected TreeView<String> tree;

    @FXML
    protected void handleSelectFilePressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FictionBook", "*.fb2"));
        File ff = null;
        try {
            ff = new File(Xmain.class.getClassLoader().getResource("123.fb2").toURI());
        } catch (URISyntaxException ignore) {
        }
        fileChooser.setInitialDirectory(ff.getParentFile());
        fileChooser.setInitialFileName("123.fb2");
        File file = fileChooser.showOpenDialog(null);
        if (null == file) {
            return;
        }
        BookHolder.setBook(file);
        showInfoDialog();

        main.getChildren().clear();
        main.getChildren().addAll(BookHolder.getFictionBook().getAnnotation().getTexts());
//        tree.setRoot(new TreeItem<>("root"));
//        TreeItem<String> root = tree.getRoot();
//        root.getChildren().add(new TreeItem<>("1"));
//        root.getChildren().add(new TreeItem<>("2"));
//        root.getChildren().add(new TreeItem<>("3"));
//        root.getChildren().get(1).getChildren().add(new TreeItem<>("2-1"));
//        root.getChildren().get(1).getChildren().add(new TreeItem<>("2-2"));
//        root.getChildren().get(1).getChildren().add(new TreeItem<>("2-3"));
    }

    @FXML
    protected void handleExitButton(ActionEvent event) {
        System.exit(0);
    }

    private void showInfoDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Xmain.class.getClassLoader().getResource("info_alert.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ignore) {
        }
    }

}
