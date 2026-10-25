import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class BookCatalogApp extends Application {
    @Override 
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Book Catalog Application");

            TableView<Book> table = new TableView<>();
            // ISBN Column
            TableColumn<Book, String> isCol = new TableColumn<>("ISBN");
            isCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
            isCol.setMinWidth(120);

            // Title Column
            TableColumn<Book, String> titleCol = new TableColumn<>("Title");
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            titleCol.setMinWidth(180);

            // Author Column
            TableColumn<Book, String> authorCol = new TableColumn<>("Author");
            authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
            authorCol.setMinWidth(150);

            // Available Column
            TableColumn<Book, Boolean> availableCol = new TableColumn<>("Available");
            availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
            availableCol.setMinWidth(100);

            // ObservableList
            ObservableList<Book> booklist = FXCollections.observableArrayList(
                    new Book("978-0134685991", "Java How to Program", "Deitel", true),
                    new Book("978-0596009205", "Head First Java", "Kathy Sierra", false),
                    new Book("978-0132350884", "Clean Code", "Robert C. Martin", true),
                    new Book("978-0131103627", "The C Programming Language", "Brian W. Kernighan", true),
                    new Book("978-0203633854", "Design Patterns", "Erich Gamma", false),
                    new Book("978-0135957059", "The Pragmatic Programmer", "David Thomas", true),
                    new Book("978-0321125217", "Domain-Driven Design", "Eric Evans", true),
                    new Book("978-0137081073", "The Clean Coder", "Robert C. Martin", false),
                    new Book("978-0265338412", "Introduction to Algorithms", "Thomas H. Cormen", true),
                    new Book("978-0134494166", "Effective Java", "Joshua Bloch", true),
                    new Book("978-1491957660", "Designing Data-Intensive Applications", "Martin Kleppmann", false),
                    new Book("978-0321356680", "Effective C++", "Scott Meyers", true),
                    new Book("978-0596517748", "JavaScript: The Good Parts", "Douglas Crockford", false),
                    new Book("978-1449331818", "Learning Python", "Mark Lutz", true),
                    new Book("978-1617294945", "Spring in Action", "Craig Walls", true),
                    new Book("978-1593276034", "Eloquent JavaScript", "Marijn Haverbeke", false),
                    new Book("978-0321146533", "Refactoring", "Martin Fowler", true),
                    new Book("978-1118008188", "Data Structures and Algorithms in Java", "Michael T. Goodrich", false),
                    new Book("978-0136083238", "Artificial Intelligence: A Modern Approach", "Stuart Russell", true),
                    new Book("978-0321573513", "C++ Primer", "Stanley B. Lippman", true));
            table.setItems(booklist);

            // VBox with Spacing, Padding, and background styling for a neat look
            VBox vBox = new VBox(10);
            vBox.setPadding(new Insets(15, 15, 15, 15));
            vBox.setStyle("-fx-background-color: #f4f6f8;");
            vBox.getChildren().add(table);

            Scene scene = new Scene(vBox, 600, 560, Color.BLANCHEDALMOND);
            table.getColumns().setAll(isCol, titleCol, authorCol, availableCol);

            // Text Fields
            TextField isbnField = new TextField();
            isbnField.setPromptText("Enter ISBN");
            TextField titleField = new TextField();
            titleField.setPromptText("Enter Title");
            TextField authorField = new TextField();
            authorField.setPromptText("Enter Author");

            // CheckBox for the boolean value
            CheckBox availableBox = new CheckBox("Available");

            // Toggle Availability Button
            Button togglebtn = new Button("Toggle Availability");
            togglebtn.setOnAction(e -> {
                Book selectedBook = table.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    selectedBook.setAvailable(!selectedBook.getAvailable());
                    table.refresh();
                }
            });

            // Add Button 
            Button btn = new Button();
            btn.setText("Add a book");
            btn.setOnAction(e -> {
                String isbn = isbnField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                boolean isAvailable = availableBox.isSelected();

                for (Book b : booklist) {
                    if (b.getIsbn().equals(isbn)) {
                        System.out.println("Book already exists!");
                        return;
                    }
                }
                booklist.add(new Book(isbn, title, author, isAvailable));
            });

            // Delete Button
            Button deletebtn = new Button("Delete a Book");
            deletebtn.setOnAction(e -> {
                Book selecteBook = table.getSelectionModel().getSelectedItem();
                if (selecteBook != null) {
                    booklist.remove(selecteBook);
                }
            });

            // Save Button
            Button saveBtn = new Button("Save to File");
            saveBtn.setOnAction(e -> saveBooksToCSV(booklist, "BookCatalog.txt"));

            // Load Button
            Button loadBtn = new Button("Load from File");
            loadBtn.setOnAction(e -> loadBooksFromCSV(booklist, "BookCatalog.txt"));

            // Search List Field
            TextField searchfField = new TextField();
            searchfField.setPromptText("Search by title or author");
            searchfField.textProperty().addListener((observable, oldVlue, newValue) -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    table.setItems(booklist);
                } else {
                    ObservableList<Book> filteredList = booklist
                            .filtered(book -> book.getTitle().toLowerCase().contains(newValue.toLowerCase()) ||
                                    book.getAuthor().toLowerCase().contains(newValue.toLowerCase()));
                    table.setItems(filteredList);
                }
            });

            // Add All controls to vBox
            vBox.getChildren().addAll(
                    searchfField,
                    isbnField,
                    titleField,
                    authorField,
                    availableBox,
                    btn,
                    deletebtn,
                    togglebtn,
                    saveBtn,
                    loadBtn
            );

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBooksToCSV(ObservableList<Book> books, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Book book : books) {
                String line = book.getIsbn() + ","
                        + book.getTitle() + ","
                        + book.getAuthor() + ","
                        + book.getAvailable();

                bw.write(line);
                bw.newLine();
            }
            System.out.println("Books saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBooksFromCSV(ObservableList<Book> books, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            books.clear(); // مسح القائمة الحالية قبل تحميل البيانات الجديدة
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String isbn = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    boolean available = Boolean.parseBoolean(parts[3]);
                    books.add(new Book(isbn, title, author, available));
                }
            }
            System.out.println("Books loaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}