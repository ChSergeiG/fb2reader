package ru.chsergeig.fb2reader.mapping.fictionbook;

import jodd.jerry.Jerry;
import ru.chsergeig.fb2reader.mapping.fictionbook.body.MyFooter;
import ru.chsergeig.fb2reader.mapping.fictionbook.body.MyHeader;
import ru.chsergeig.fb2reader.mapping.fictionbook.common.Author;
import ru.chsergeig.fb2reader.mapping.fictionbook.common.Binary;
import ru.chsergeig.fb2reader.mapping.fictionbook.documentinfo.History;
import ru.chsergeig.fb2reader.mapping.fictionbook.titleinfo.Annotation;
import ru.chsergeig.fb2reader.mapping.fictionbook.titleinfo.Sequence;
import ru.chsergeig.fb2reader.misc.BookContainer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static ru.chsergeig.fb2reader.util.StructureSupplier.ANNOTATION;
import static ru.chsergeig.fb2reader.util.StructureSupplier.BINARY;
import static ru.chsergeig.fb2reader.util.StructureSupplier.BODY;
import static ru.chsergeig.fb2reader.util.StructureSupplier.BOOK_AUTHOR;
import static ru.chsergeig.fb2reader.util.StructureSupplier.BOOK_NAME;
import static ru.chsergeig.fb2reader.util.StructureSupplier.BOOK_TITLE;
import static ru.chsergeig.fb2reader.util.StructureSupplier.CITY;
import static ru.chsergeig.fb2reader.util.StructureSupplier.COVERPAGE;
import static ru.chsergeig.fb2reader.util.StructureSupplier.DATE;
import static ru.chsergeig.fb2reader.util.StructureSupplier.FB2_AUTHOR;
import static ru.chsergeig.fb2reader.util.StructureSupplier.FB2_DATE;
import static ru.chsergeig.fb2reader.util.StructureSupplier.FB2_HISTORY;
import static ru.chsergeig.fb2reader.util.StructureSupplier.FB2_ID;
import static ru.chsergeig.fb2reader.util.StructureSupplier.FB2_PROGRAM_USED;
import static ru.chsergeig.fb2reader.util.StructureSupplier.FB2_SRC_OCR;
import static ru.chsergeig.fb2reader.util.StructureSupplier.FB2_SRC_URL;
import static ru.chsergeig.fb2reader.util.StructureSupplier.FB2_VERSION;
import static ru.chsergeig.fb2reader.util.StructureSupplier.GENRE;
import static ru.chsergeig.fb2reader.util.StructureSupplier.ISBN;
import static ru.chsergeig.fb2reader.util.StructureSupplier.KEYWORDS;
import static ru.chsergeig.fb2reader.util.StructureSupplier.LANG;
import static ru.chsergeig.fb2reader.util.StructureSupplier.MY_FOOTER;
import static ru.chsergeig.fb2reader.util.StructureSupplier.MY_HEADER;
import static ru.chsergeig.fb2reader.util.StructureSupplier.PUBLISHER;
import static ru.chsergeig.fb2reader.util.StructureSupplier.SEQUENCE;
import static ru.chsergeig.fb2reader.util.StructureSupplier.SRC_LANG;
import static ru.chsergeig.fb2reader.util.StructureSupplier.TRANSLATOR;
import static ru.chsergeig.fb2reader.util.Utils.safeExtractValue;

public class FictionBook {

    private String genre = "";
    private List<Author> bookAuthors = new ArrayList<>();
    private String bookTitle = "";
    private Annotation annotation;
    private String keywords = "";
    private String date = "";
    private Binary coverpage;
    private String lang = "";
    private String srcLang = "";
    private Author translator;
    private Sequence sequence;
    private Author fb2Author;
    private String programUsed = "";
    private String fb2Date = "";
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
        Iterator<?> binariesItr = safeExtractValue(() -> BINARY.getElements().iterator(), Collections.emptyIterator());
        while (binariesItr.hasNext()) {
            Binary binary = new Binary((Jerry) binariesItr.next());
            binaries.put("#" + binary.getId(), binary);
        }
    }

    private void initTitleInfo() {
        genre = safeExtractValue(() -> GENRE.getFirstElement().text(), "");
        Iterator<?> authorIter = safeExtractValue(() -> BOOK_AUTHOR.getElements().iterator(), Collections.emptyIterator());
        while (authorIter.hasNext()) {
            bookAuthors.add(new Author((Jerry) authorIter.next()));
        }
        bookTitle = safeExtractValue(() -> BOOK_TITLE.getFirstElement().text(), "");
        annotation = new Annotation(ANNOTATION.getFirstElement());
        keywords = safeExtractValue(() -> KEYWORDS.getFirstElement().text(), "");
        date = safeExtractValue(() -> DATE.getFirstElement().text(), "");
        coverpage = binaries.get(safeExtractValue(() -> COVERPAGE.getFirstElement().attr("l:href")));
        lang = safeExtractValue(() -> LANG.getFirstElement().text(), "");
        srcLang = safeExtractValue(() -> SRC_LANG.getFirstElement().text(), "");
        translator = new Author(TRANSLATOR.getFirstElement());
        sequence = new Sequence(SEQUENCE.getFirstElement());
    }

    private void initDocumentInfo() {
        fb2Author = new Author(FB2_AUTHOR.getFirstElement());
        programUsed = safeExtractValue(() -> FB2_PROGRAM_USED.getFirstElement().text(), "");
        fb2Date = safeExtractValue(() -> FB2_DATE.getFirstElement().attr("value"), "");
        fb2SrcUrl = safeExtractValue(URL.class, null, () -> FB2_SRC_URL.getFirstElement().text());
        fb2SrcOcr = safeExtractValue(() -> FB2_SRC_OCR.getFirstElement().text(), "");
        fb2id = safeExtractValue(() -> FB2_ID.getFirstElement().text(), "");
        fb2Version = safeExtractValue(() -> FB2_VERSION.getFirstElement().text(), "");
        fb2History = new History(FB2_HISTORY.getElements());
    }

    private void initPublishInfo() {
        bookName = safeExtractValue(() -> BOOK_NAME.getFirstElement().text(), "");
        publisher = safeExtractValue(() -> PUBLISHER.getFirstElement().text(), "");
        city = safeExtractValue(() -> CITY.getFirstElement().text(), "");
        isbn = safeExtractValue(() -> ISBN.getFirstElement().text(), "");
    }

    private void initMyHeader() {
        myHeader = new MyHeader(MY_HEADER.getFirstElement());
    }

    private void initBookContainers() {
        rootContainer = new BookContainer(BODY.getFirstElement());
    }

    private void initMyFooter() {
        myFooter = new MyFooter(MY_FOOTER.getFirstElement());
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

    public String getDate() {
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

    public String getFb2Date() {
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
