package com.example.android.bookcompanion.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VolumeInfo implements Parcelable {

    public final static Parcelable.Creator<VolumeInfo> CREATOR = new Creator<VolumeInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VolumeInfo createFromParcel(Parcel in) {
            return new VolumeInfo(in);
        }

        public VolumeInfo[] newArray(int size) {
            return (new VolumeInfo[size]);
        }

    };
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("authors")
    @Expose
    private List<String> authors = null;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("publishedDate")
    @Expose
    private String publishedDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("pageCount")
    @Expose
    private int pageCount;
    @SerializedName("printType")
    @Expose
    private String printType;
    @SerializedName("categories")
    @Expose
    private List<String> categories = null;
    @SerializedName("maturityRating")
    @Expose
    private String maturityRating;
    @SerializedName("allowAnonLogging")
    @Expose
    private boolean allowAnonLogging;
    @SerializedName("contentVersion")
    @Expose
    private String contentVersion;
    @SerializedName("imageLinks")
    @Expose
    private ImageLinks imageLinks;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("previewLink")
    @Expose
    private String previewLink;
    @SerializedName("infoLink")
    @Expose
    private String infoLink;
    @SerializedName("canonicalVolumeLink")
    @Expose
    private String canonicalVolumeLink;
    @SerializedName("averageRating")
    @Expose
    private double averageRating;
    @SerializedName("ratingsCount")
    @Expose
    private int ratingsCount;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;

    protected VolumeInfo(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.authors, (java.lang.String.class.getClassLoader()));
        this.publisher = ((String) in.readValue((String.class.getClassLoader())));
        this.publishedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.pageCount = ((int) in.readValue((int.class.getClassLoader())));
        this.printType = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.categories, (java.lang.String.class.getClassLoader()));
        this.maturityRating = ((String) in.readValue((String.class.getClassLoader())));
        this.allowAnonLogging = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.contentVersion = ((String) in.readValue((String.class.getClassLoader())));
        this.imageLinks = ((ImageLinks) in.readValue((ImageLinks.class.getClassLoader())));
        this.language = ((String) in.readValue((String.class.getClassLoader())));
        this.previewLink = ((String) in.readValue((String.class.getClassLoader())));
        this.infoLink = ((String) in.readValue((String.class.getClassLoader())));
        this.canonicalVolumeLink = ((String) in.readValue((String.class.getClassLoader())));
        this.averageRating = ((double) in.readValue((double.class.getClassLoader())));
        this.ratingsCount = ((int) in.readValue((int.class.getClassLoader())));
        this.subtitle = ((String) in.readValue((String.class.getClassLoader())));
    }

    public VolumeInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VolumeInfo withTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public VolumeInfo withAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public VolumeInfo withPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public VolumeInfo withPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VolumeInfo withDescription(String description) {
        this.description = description;
        return this;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public VolumeInfo withPageCount(int pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public VolumeInfo withPrintType(String printType) {
        this.printType = printType;
        return this;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public VolumeInfo withCategories(List<String> categories) {
        this.categories = categories;
        return this;
    }

    public String getMaturityRating() {
        return maturityRating;
    }

    public void setMaturityRating(String maturityRating) {
        this.maturityRating = maturityRating;
    }

    public VolumeInfo withMaturityRating(String maturityRating) {
        this.maturityRating = maturityRating;
        return this;
    }

    public boolean isAllowAnonLogging() {
        return allowAnonLogging;
    }

    public void setAllowAnonLogging(boolean allowAnonLogging) {
        this.allowAnonLogging = allowAnonLogging;
    }

    public VolumeInfo withAllowAnonLogging(boolean allowAnonLogging) {
        this.allowAnonLogging = allowAnonLogging;
        return this;
    }

    public String getContentVersion() {
        return contentVersion;
    }

    public void setContentVersion(String contentVersion) {
        this.contentVersion = contentVersion;
    }

    public VolumeInfo withContentVersion(String contentVersion) {
        this.contentVersion = contentVersion;
        return this;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    public VolumeInfo withImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public VolumeInfo withLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public VolumeInfo withPreviewLink(String previewLink) {
        this.previewLink = previewLink;
        return this;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public VolumeInfo withInfoLink(String infoLink) {
        this.infoLink = infoLink;
        return this;
    }

    public String getCanonicalVolumeLink() {
        return canonicalVolumeLink;
    }

    public void setCanonicalVolumeLink(String canonicalVolumeLink) {
        this.canonicalVolumeLink = canonicalVolumeLink;
    }

    public VolumeInfo withCanonicalVolumeLink(String canonicalVolumeLink) {
        this.canonicalVolumeLink = canonicalVolumeLink;
        return this;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public VolumeInfo withAverageRating(double averageRating) {
        this.averageRating = averageRating;
        return this;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public VolumeInfo withRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
        return this;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public VolumeInfo withSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeList(authors);
        dest.writeValue(publisher);
        dest.writeValue(publishedDate);
        dest.writeValue(description);
        dest.writeValue(pageCount);
        dest.writeValue(printType);
        dest.writeList(categories);
        dest.writeValue(maturityRating);
        dest.writeValue(allowAnonLogging);
        dest.writeValue(contentVersion);
        dest.writeValue(imageLinks);
        dest.writeValue(language);
        dest.writeValue(previewLink);
        dest.writeValue(infoLink);
        dest.writeValue(canonicalVolumeLink);
        dest.writeValue(averageRating);
        dest.writeValue(ratingsCount);
        dest.writeValue(subtitle);
    }

    public int describeContents() {
        return 0;
    }
}

