package com.example.youtubedownloader;

public class VideoRequest {
    private String url;
    private String quality = "720p"; // Default quality is 720p

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}
