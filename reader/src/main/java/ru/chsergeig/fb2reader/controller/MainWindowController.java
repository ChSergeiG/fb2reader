package ru.chsergeig.fb2reader.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.chsergeig.fb2reader.BookHolder;
import ru.chsergeig.fb2reader.Xmain;
import ru.chsergeig.fb2reader.elements.MainFlowPane;
import ru.chsergeig.fb2reader.mapping.cache.BookCache;
import ru.chsergeig.fb2reader.mapping.fictionbook.FictionBook;
import ru.chsergeig.fb2reader.misc.BookContainer;
import ru.chsergeig.fb2reader.util.CacheUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    protected MenuBar menubar;
    @FXML
    protected GridPane gridPane;
    @FXML
    protected TreeView<BookContainer> tree;
    @FXML
    protected Menu lastBooks;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public MainFlowPane flowPane;

    @FXML
    protected void handleSelectFilePressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FictionBook", "*.fb2", "*.zip"));
        File file = fileChooser.showOpenDialog(null);
        if (null == file) {
            return;
        }
        CacheUtils.getBookCache().addEntry(file.toPath(), file.getAbsolutePath(), LocalDateTime.now());
        BookHolder.setBook(file.toPath());
        fillTree();
        BookContainer.showCover(flowPane);
    }

    @FXML
    protected void handleExitButton(ActionEvent event) {
        CacheUtils.writeCacheToFile();
        Platform.exit();
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

    @FXML
    public void handleShowInfoDialog() {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                TreeItem<BookContainer> treeItem = cell.getTreeItem();
                if (null == cell.getItem() || null == cell.getItem().getParent()) {
                    BookContainer.showCover(flowPane);
                    return;
                }
                if (treeItem.getChildren().size() == 0) {
                    Platform.runLater(() -> flowPane.showParagraphs(cell.getItem().getContent()));
                }
                scrollPane.vvalueProperty().set(0.0d);
            });
            Platform.runLater(() -> cell.prefWidthProperty().bind(tree.widthProperty().subtract(5.0)));
            return cell;
        });
        Platform.runLater(() -> flowPane.prefWidthProperty().bind(flowPane.getScene().widthProperty().multiply(0.75)));
        ObservableList<MenuItem> items = lastBooks.getItems();
        items.clear();
        BookCache bookCache = CacheUtils.getBookCache();
        if (bookCache.books.isEmpty()) {
            MenuItem menuItem = new MenuItem("(no books)");
            menuItem.setDisable(true);
            items.add(menuItem);
            return;
        }
        bookCache.books.forEach(entry -> {
            MenuItem item = new MenuItem(entry.bookTitle);
            item.setOnAction(event -> {
                BookHolder.setBook(Paths.get(entry.fileName));
                fillTree();
                BookContainer.showCover(flowPane);
            });
            items.add(item);
        });
        bookCache.books.stream()
                .sorted(Comparator.comparing(o -> o.lastLoadedDate))
                .max(Comparator.naturalOrder())
                .ifPresent(entry -> {
                    BookHolder.setBook(Paths.get(entry.fileName));
                    fillTree();
                    BookContainer.showCover(flowPane);
                });
    }

    private void updateFontSizeTo(BookContainer container, double size) {
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
