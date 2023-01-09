package mse.advDB;

public class Article {
    private String id;
    private String title;
    private List<Author> authors;
    private Venue venue;
    private int year;
    private List<String> keywords;
    private List<String> fos;
    private int nCitation;
    private String pageStart;
    private String pageEnd;
    private String lang;
    private String volume;
    private String issue;
    private String issn;
    private String isbn;
    private String doi;
    private String pdf;
    private List<String> url;
    private String abstract_;// underscore because abstract is a word of Java

    public Article() {
    }

    // getters and setters for each field

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Venue getVenue() {
        return venue;
    }
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public List<String> getKeywords() {
        return keywords;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getFos() {
        return fos;
    }
    public void setFos(List<String> fos) {
        this.fos = fos;
    }

    public int getnCitation() {
        return nCitation;
    }
    public void setnCitation(int nCitation) {
        this.nCitation = nCitation;
    }

    public String getPageStart() {
        return pageStart;
    }
    public void setPageStart(String pageStart) {
        this.pageStart = pageStart;
    }

    public String getPageEnd() {
        return pageEnd;
    }
    public void setPageEnd(String pageEnd) {
        this.pageEnd = pageEnd;
    }

    public String getLang() {
        return lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getVolume() {
        return volume;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }
    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getIssn() {
        return issn;
    }
    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDoi() {
        return doi;
    }
    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getPdf() {
        return pdf;
    }
    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public List<String> getUrl() {
        return url;
    }
    public void setUrl(List<String> url) {
        this.url = url;
    }

    public String getAbstract() {
        return abstract_;
    }
    public void setAbstract(String abstract_) {
        this.abstract_ = abstract_;
    }

    @Override
    public String toString() {
        return "Article [id=" + id + ", title=" + title + ", authors=" + authors + ", venue=" + venue + ", year=" + year
                + ", keywords=" + keywords + ", fos=" + fos + ", nCitation=" + nCitation + ", pageStart=" + pageStart
                + ", pageEnd=" + pageEnd + ", lang=" + lang + ", volume=" + volume + ", issue=" + issue + ", issn=" + issn
                + ", isbn=" + isbn + ", doi=" + doi + ", pdf=" + pdf + ", url=" + url + ", abstract_=" + abstract_ + "]";
    }

}
