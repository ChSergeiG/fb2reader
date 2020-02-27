package ru.chsergeig.fb2reader.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.chsergeig.fb2reader.BookHolder;
import ru.chsergeig.fb2reader.Xmain;
import ru.chsergeig.fb2reader.mapping.FictionBook;
import ru.chsergeig.fb2reader.misc.BookContainer;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class MainWindowController {

    @FXML
    protected MenuBar menubar;
    @FXML
    protected GridPane gridPane;
    @FXML
    protected TextFlow main;
    @FXML
    protected TreeView<BookContainer> tree;

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
        fillTree();
        showCover();
    }

    @FXML
    protected void handleExitButton(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void handleFontDecrease(ActionEvent actionEvent) {
        FictionBook fictionBook = BookHolder.getFictionBook();
        if (null == fictionBook) {
            return;
        }
        updateFontSizeTo(fictionBook.getRootContainer(), BookHolder.fontSize - 2);
    }

    @FXML
    public void handleFontIncrease(ActionEvent actionEvent) {
        FictionBook fictionBook = BookHolder.getFictionBook();
        if (null == fictionBook) {
            return;
        }
        updateFontSizeTo(fictionBook.getRootContainer(), BookHolder.fontSize + 2);
    }

    public void initialize() {
        tree.setCellFactory(tree -> {
            TreeCell<BookContainer> cell = new TreeCell<BookContainer>() {
                @Override
                public void updateItem(BookContainer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText("");
                    } else {
                        setText(item.getTitle());
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    TreeItem<BookContainer> treeItem = cell.getTreeItem();
                    if (treeItem.getChildren().size() == 0) {
                        Platform.runLater(() -> {
                            ObservableList<Node> nodes = main.getChildren();
                            nodes.clear();
                            nodes.addAll(cell.getItem().getContent());
                        });
                    }
                }
            });
            Platform.runLater(() -> {
                cell.prefWidthProperty().bind(tree.widthProperty().subtract(5.0));
            });
            return cell;
        });
        Platform.runLater(() -> {
            main.prefWidthProperty().bind(main.getScene().widthProperty().multiply(0.8));
        });
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

    private void updateFontSizeTo(BookContainer container, double size) {
    }

    private void showCover() {
        Platform.runLater(() -> {
            ObservableList<Node> nodes = main.getChildren();
            FictionBook book = BookHolder.getFictionBook();
            nodes.clear();
            Text title = new Text(book.getBookTitle());
            title.setFont(Font.font("Helvetica", FontWeight.BOLD, BookHolder.fontSize * 2));
            nodes.add(title);
            nodes.addAll(book.getMyHeader().getTexts());
            try {
                ImageView image = new ImageView();
                byte[] bytes = new BASE64Decoder().decodeBuffer(book.getCoverpage().getValue());
                image.setImage(new Image(new ByteArrayInputStream(bytes)));
                nodes.add(image);
            } catch (IOException ignore) {
            }
            nodes.addAll(book.getMyFooter().getTexts());
        });
    }

    private void fillTree() {
        BookContainer rootContainer = BookHolder.getFictionBook().getRootContainer();
        TreeItem<BookContainer> rootItem = new TreeItem<>(rootContainer);
        tree.setRoot(rootItem);
        rootItem.getChildren().addAll(fillTree(rootContainer));
    }

    private List<TreeItem<BookContainer>> fillTree(BookContainer rootContainer) {
        List<TreeItem<BookContainer>> result = new ArrayList<>();
        for (BookContainer child : rootContainer.getChildren()) {
            TreeItem<BookContainer> bookContainerTreeItem = new TreeItem<>(child);
            List<TreeItem<BookContainer>> treeItems = fillTree(child);
            bookContainerTreeItem.getChildren().addAll(treeItems);
            result.add(bookContainerTreeItem);
        }
        return result;
    }

}
