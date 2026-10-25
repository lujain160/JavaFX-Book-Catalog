// A: Create a class named book and use appropriate constructors, getters/setters, and encapsulation
public  class Book {
    private String isbn;
    private String title;
    private String author;
    private boolean available;

    // Getter
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean getAvailable() {
        return available;
    }

    // Setter
    public void  setIsbn(String isbn) {
        this.isbn=isbn;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setAuthor(String author) {
        this.author=author;
    }

    public void setAvailable(boolean available) {
       this.available=available;
    }

    public Book(){
        this.isbn=null;
        this.title=null;
        this.author=null;
        this.available=false;
    }

    public Book(String isbn, String title, String author, boolean available){
        this.isbn=isbn;
        this.title=title;
        this.author=author;
        this.available=available;
    }}