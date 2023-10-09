package com.mycompany.pdffilemetadatareader;
import java.io.File;
import java.io.Serializable;
import java.util.List;

public class PDFFileInfo implements Serializable {
    private File file;
    private String name;
    private long fileSize;
    private long pageSize;
    private int pageCount;
    private String title;
    private String subject;
    private String keywords;
    private String fileType;
    private float pdfVersion;
    private String creator;
    private List<String> images;
    private List<String> sources;

    // Constructor
    public PDFFileInfo(File file, String name, long fileSize, long pageSize, int pageCount, String title, String subject,
                       String keywords, String fileType, float pdfVersion, String creator, List<String> images, List<String> sources) {
        this.file = file;
        this.name = name;
        this.fileSize = fileSize;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.title = title;
        this.subject = subject;
        this.keywords = keywords;
        this.fileType = fileType;
        this.pdfVersion = pdfVersion;
        this.creator = creator;
        this.images = images;
        this.sources = sources;
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

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    public long getPageSize() {
        return pageSize;
    }
    
    public void setPagesSize(long pageSize) {
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
    
    public List<String> getImages(){
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
    
    public List<String> getSources() {
        return sources;
    }
    
    public void setSources(List<String> sources) {
        this.sources = sources;
    }

}
