package com.example.youtubedownloader.controller;

import com.example.youtubedownloader.VideoRequest;
import com.example.youtubedownloader.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/download")
    public ResponseEntity<?> downloadVideo(@RequestBody VideoRequest videoRequest) {
        String filename = videoService.downloadVideo(videoRequest.getUrl(), videoRequest.getQuality());
        if (filename != null) {
            return ResponseEntity.ok().body("{\"message\":\"Video downloaded successfully.\", \"filename\":\"" + filename + "\"}");
        } else {
            return ResponseEntity.status(500).body("{\"message\":\"Failed to download video.\"}");
        }
    }

    @GetMapping("/downloads/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("/home/solipuram-kranthikumar/Downloads/Bookmark").resolve(filename).normalize();
            Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
