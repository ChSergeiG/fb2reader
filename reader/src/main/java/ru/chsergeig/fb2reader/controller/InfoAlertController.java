package ru.chsergeig.fb2reader.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ru.chsergeig.fb2reader.BookHolder;
import ru.chsergeig.fb2reader.misc.BookInfoTableRow;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoAlertController implements Initializable {

    @FXML
    protected TableView<BookInfoTableRow> tableView;

    @FXML
    protected Button close;

    @FXML
    protected void handleClosePressed(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.setItems(BookHolder.getCurrentBookInfo());
    }

}
