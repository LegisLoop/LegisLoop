package com.backend.legisloop.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DatasetUtils {

    private static final Gson gson = new Gson();

    /**
     * Unzips and organizes JSON files from a Base64 encoded ZIP file.
     *
     * @param base64Zip The Base64 encoded ZIP content.
     * @return A map where keys are directory names ("bill", "people", "vote"),
     *         and values are lists of parsed JSON objects.
     * @throws IOException If an error occurs during file processing.
     */
    public static Map<String, List<JsonObject>> unzipJsonFiles(String base64Zip) throws IOException {
        byte[] zipBytes = Base64.getDecoder().decode(base64Zip);
        return processZipInputStream(new ZipInputStream(new ByteArrayInputStream(zipBytes)));
    }
    public static Map<String, List<JsonObject>> unzipJsonFilesFromFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ZipInputStream zis = new ZipInputStream(fis)) {
            return processZipInputStream(zis);
        }
    }

    /**
     * Processes a ZIP input stream, extracting and categorizing JSON files.
     *
     * @param zis The ZipInputStream to process.
     * @return A map where keys are directory names ("bill", "people", "vote"),
     *         and values are lists of parsed JSON objects.
     * @throws IOException If an error occurs during file processing.
     */
    private static Map<String, List<JsonObject>> processZipInputStream(ZipInputStream zis) throws IOException {
        Map<String, List<JsonObject>> directoryJsonMap = new HashMap<>();
        ZipEntry entry;

        while ((entry = zis.getNextEntry()) != null) {
            if (!entry.isDirectory() && entry.getName().endsWith(".json")) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }

                String jsonString = baos.toString(StandardCharsets.UTF_8);
                JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

                // Extract directory name from ZIP structure (STATE/SESSION/directory/FILENAME.json)
                String[] pathParts = entry.getName().split("/");
                if (pathParts.length >= 3) {
                    String directory = pathParts[2]; // e.g., "bill", "people", "vote"
                    directoryJsonMap
                            .computeIfAbsent(directory, k -> new ArrayList<>())
                            .add(jsonObject);
                }
            }
            zis.closeEntry();
        }
        return directoryJsonMap;
    }
}
