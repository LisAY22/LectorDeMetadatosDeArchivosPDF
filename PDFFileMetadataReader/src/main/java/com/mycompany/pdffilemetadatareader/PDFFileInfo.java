package com.mycompany.pdffilemetadatareader;
import java.io.File;
import java.io.Serializable;

public class PDFFileInfo implements Serializable {
    private File file;
    private long fileSize;
    private int pageCount;
    private String title;
    private String subject;
    private String keywords;
    private String fileType;
    private String pdfVersion;
    private String creator;

    // Constructor
    public PDFFileInfo(File file, long fileSize, int pageCount, String title, String subject,
                       String keywords, String fileType, float pdfVersion, String creator) {
        this.file = file;
        this.fileSize = fileSize;
        this.pageCount = pageCount;
        this.title = title;
        this.subject = subject;
        this.keywords = keywords;
        this.fileType = fileType;
        this.pdfVersion = String.valueOf(pdfVersion);
        this.creator = creator;
    }

    // Métodos getter y setter para acceder a los atributos

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
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

    public String getPdfVersion() {
        return pdfVersion;
    }

    public void setPdfVersion(String pdfVersion) {
        this.pdfVersion = pdfVersion;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
