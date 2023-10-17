package com.mycompany.pdffilemetadatareader;
import java.io.File;
import java.io.Serializable;

public class PDFFileInfo implements Serializable {
    private final File file;
    private String name;
    private String author;
    private final long fileSize;
    private final String pageSize;
    private final int pageCount;
    private String title;
    private String subject;
    private String keywords;
    private final String fileType;
    private final float pdfVersion;
    private final String creator;
    private final int imagesCount;
    private final int imagesFontsCount;
    private String summary;

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
    
    public String getPageSize() {
        return pageSize;
    }
    public int getPageCount() {
        return pageCount;
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

    public float getPdfVersion() {
        return pdfVersion;
    }

    public String getCreator() {
        return creator;
    }
    
    public int getImagesCount(){
        return imagesCount;
    }
    
    public int getImagesFontsCount() {
        return imagesFontsCount;
    }
    
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary){
        this.summary = summary;
    }

}

