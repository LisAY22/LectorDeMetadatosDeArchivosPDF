package com.mycompany.pdffilemetadatareader;
import java.io.File;
import java.io.Serializable;

public class PDFFileInfo implements Serializable {
    private File file;
    private String name;
    private String author;
    private long fileSize;
    private String pageSize;
    private int pageCount;
    private String title;
    private String subject;
    private String keywords;
    private String fileType;
    private float pdfVersion;
    private String creator;
    private int imagesCount;
    private int imagesFontsCount;
    private final String summary;

    // Constructor
    public PDFFileInfo(File file, String name, String author, long fileSize, String pageSize, int pageCount, String title, String subject,
                       String keywords, String fileType, float pdfVersion, String creator, int imagesCount, int imagesFontsCount, String summary) {
        this.file = file;
        this.name = name;
        this.author = author;
        this.fileSize = fileSize;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.title = title;
        this.subject = subject;
        this.keywords = keywords;
        this.fileType = fileType;
        this.pdfVersion = pdfVersion;
        this.creator = creator;
        this.imagesCount = imagesCount;
        this.imagesFontsCount = imagesFontsCount;
        this.summary = summary;
    }

    // Métodos getter y setter para acceder a los atributos

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getPageSize() {
        return pageSize;
    }
    
    public void setPagesSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public float getPdfVersion() {
        return pdfVersion;
    }

    public void setPdfVersion(float pdfVersion) {
        this.pdfVersion = pdfVersion;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public int getImagesCount(){
        return imagesCount;
    }
    
    public void setImagesCount(int imagesCount) {
        this.imagesCount = imagesCount;
    }
    
    public int getImagesFontsCount() {
        return imagesFontsCount;
    }
    
    public void setImagesFontsCount(int imagesFontsCount) {
        this.imagesFontsCount = imagesFontsCount;
    }
    public String getSummary() {
        return summary;
    }

}

