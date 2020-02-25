package ru.chsergeig.fb2reader.mapping;

import org.jsoup.nodes.Element;
import ru.chsergeig.fb2reader.mapping.body.MyFooter;
import ru.chsergeig.fb2reader.mapping.body.MyHeader;
import ru.chsergeig.fb2reader.mapping.common.Author;
import ru.chsergeig.fb2reader.mapping.common.Binary;
import ru.chsergeig.fb2reader.mapping.documentinfo.History;
import ru.chsergeig.fb2reader.mapping.titleinfo.Annotation;
import ru.chsergeig.fb2reader.mapping.titleinfo.Sequence;
import ru.chsergeig.fb2reader.misc.BookContainer;
import ru.chsergeig.fb2reader.util.StructureSupplier;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.chsergeig.fb2reader.util.TextUtils.safeExtractValue;

public class FictionBook {

    private String genre = "";
    private List<Author> bookAuthors = new ArrayList<>();
    private String bookTitle = "";
    private Annotation annotation;
    private String keywords = "";
    private LocalDate date = LocalDate.ofEpochDay(0);
    private Binary coverpage;
    private String lang = "";
    private String srcLang = "";
    private Author translator;
    private Sequence sequence;
    private Author fb2Author;
    private String programUsed = "";
    private LocalDate fb2Date = LocalDate.ofEpochDay(0);
    private URL fb2SrcUrl;
    private String fb2SrcOcr = "";
    private String fb2id = "";
    private String fb2Version = "";
    private History fb2History;
    private String bookName = "";
    private String publisher = "";
    private String city = "";
    private String isbn = "";
    private MyHeader myHeader;
    private BookContainer rootContainer;
    private MyFooter myFooter;
    private Map<String, Binary> binaries = new HashMap<>();

    public FictionBook() {
        initBinaries();
        initDescription();
        initBody();
    }

    private void initDescription() {
        initTitleInfo();
        initDocumentInfo();
        initPublishInfo();
    }

    private void initBody() {
        initMyHeader();
        initBookContainers();
        initMyFooter();
    }

    private void initBinaries() {
        for (Element element : safeExtractValue(StructureSupplier.BINARY::getElements, new ArrayList<Element>())) {
            Binary binary = new Binary(element);
            binaries.put("#" + binary.getId(), binary);
        }
    }

    private void initTitleInfo() {
        genre = safeExtractValue(() -> StructureSupplier.GENRE.getFirstElement().text(), "");
        for (Element element : safeExtractValue(StructureSupplier.BOOK_AUTHOR::getElements, new ArrayList<Element>())) {
            bookAuthors.add(new Author(element));
        }
        bookTitle = safeExtractValue(() -> StructureSupplier.BOOK_TITLE.getFirstElement().text(), "");
        annotation = new Annotation(StructureSupplier.ANNOTATION.getFirstElement());
        keywords = safeExtractValue(() -> StructureSupplier.KEYWORDS.getFirstElement().text(), "");
        date = safeExtractValue(() -> LocalDate.parse(StructureSupplier.DATE.getFirstElement().text()), LocalDate.ofEpochDay(0L));
        coverpage = binaries.get(safeExtractValue(() -> StructureSupplier.COVERPAGE.getFirstElement().attr("l:href")));
        lang = safeExtractValue(() -> StructureSupplier.LANG.getFirstElement().text(), "");
        srcLang = safeExtractValue(() -> StructureSupplier.SRC_LANG.getFirstElement().text(), "");
        translator = new Author(StructureSupplier.TRANSLATOR.getFirstElement());
        sequence = new Sequence(StructureSupplier.SEQUENCE.getFirstElement());
    }

    private void initDocumentInfo() {
        fb2Author = new Author(StructureSupplier.FB2_AUTHOR.getFirstElement());
        programUsed = safeExtractValue(() -> StructureSupplier.FB2_PROGRAM_USED.getFirstElement().text(), "");
        fb2Date = safeExtractValue(() -> LocalDate.parse(StructureSupplier.FB2_DATE.getFirstElement().attr("value")), LocalDate.ofEpochDay(0L));
        fb2SrcUrl = safeExtractValue(URL.class, null, () -> StructureSupplier.FB2_SRC_URL.getFirstElement().text());
        fb2SrcOcr = safeExtractValue(() -> StructureSupplier.FB2_SRC_OCR.getFirstElement().text(), "");
        fb2id = safeExtractValue(() -> StructureSupplier.FB2_ID.getFirstElement().text(), "");
        fb2Version = safeExtractValue(() -> StructureSupplier.FB2_VERSION.getFirstElement().text(), "");
        fb2History = new History(StructureSupplier.FB2_HISTORY.getElements());
    }

    private void initPublishInfo() {
        bookName = safeExtractValue(() -> StructureSupplier.BOOK_NAME.getFirstElement().text(), "");
        publisher = safeExtractValue(() -> StructureSupplier.PUBLISHER.getFirstElement().text(), "");
        city = safeExtractValue(() -> StructureSupplier.CITY.getFirstElement().text(), "");
        isbn = safeExtractValue(() -> StructureSupplier.ISBN.getFirstElement().text(), "");
    }

    private void initMyHeader() {
        myHeader = new MyHeader(StructureSupplier.MY_HEADER.getFirstElement());
    }

    private void initBookContainers() {
        rootContainer = new BookContainer(StructureSupplier.BODY.getFirstElement());
    }

    private void initMyFooter() {

    }

    // region getters
    public String getGenre() {
        return genre;
    }

    public List<Author> getBookAuthors() {
        return bookAuthors;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public String getKeywords() {
        return keywords;
    }

    public LocalDate getDate() {
        return date;
    }

    public Binary getCoverpage() {
        return coverpage;
    }

    public String getLang() {
        return lang;
    }

    public String getSrcLang() {
        return srcLang;
    }

    public Author getTranslator() {
        return translator;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public Author getFb2Author() {
        return fb2Author;
    }

    public String getProgramUsed() {
        return programUsed;
    }

    public LocalDate getFb2Date() {
        return fb2Date;
    }

    public URL getFb2SrcUrl() {
        return fb2SrcUrl;
    }

    public String getFb2SrcOcr() {
        return fb2SrcOcr;
    }

    public String getFb2id() {
        return fb2id;
    }

    public String getFb2Version() {
        return fb2Version;
    }

    public History getFb2History() {
        return fb2History;
    }

    public String getBookName() {
        return bookName;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCity() {
        return city;
    }

    public String getIsbn() {
        return isbn;
    }

    public MyHeader getMyHeader() {
        return myHeader;
    }

    public BookContainer getRootContainer() {
        return rootContainer;
    }

    public MyFooter getMyFooter() {
        return myFooter;
    }

    public Map<String, Binary> getBinaries() {
        return binaries;
    }

    // endregion
}
