package com.example.youtubedownloader.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class VideoService {

    public String downloadVideo(String url, String quality) {
        try {
            // Get video title using yt-dlp and sanitize it for file name use
            String[] titleCommand = {"yt-dlp", "--get-title", url};
            Process titleProcess = new ProcessBuilder(titleCommand).start();
            BufferedReader titleReader = new BufferedReader(new InputStreamReader(titleProcess.getInputStream()));
            String title = titleReader.readLine();
            titleProcess.waitFor();
            titleReader.close();

            if (title == null || title.isEmpty()) {
                return null;
            }

            // Sanitize the title to make it a valid filename
            String sanitizedTitle = title.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "_");
            String uniqueId = UUID.randomUUID().toString();
            String filename = sanitizedTitle + "_" + uniqueId + ".mp4";

            // Download the video using yt-dlp with specified quality
            String userHome = System.getProperty("user.home");
            String outputPath = Paths.get(userHome, "Downloads", "Bookmark", filename).toString();
            String[] command = {"yt-dlp", "-f", "bestvideo[height<=" + quality + "]+bestaudio/best[height<=" + quality + "]",
                    "-o", outputPath, url};
            Process process = new ProcessBuilder(command).start();
            process.waitFor();

            // Optional: Read the output (useful for debugging)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

            return filename;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
