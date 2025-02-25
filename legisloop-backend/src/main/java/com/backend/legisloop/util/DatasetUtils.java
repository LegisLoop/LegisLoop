package com.backend.legisloop.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public static Map<String, List<JsonObject>> unzipJsonFiles(String base64Zip) throws IOException {
        // Decode the Base64 encoded ZIP content to bytes
        byte[] zipBytes = Base64.getDecoder().decode(base64Zip);
        Map<String, List<JsonObject>> directoryJsonMap = new HashMap<>();

        try (ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
             ZipInputStream zis = new ZipInputStream(bais)) {

            ZipEntry entry;
            // Iterate through all entries in the ZIP file
            while ((entry = zis.getNextEntry()) != null) {
                // Only process files (ignore directories) and those ending with .json
                if (!entry.isDirectory() && entry.getName().endsWith(".json")) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    // Read the contents of the current entry
                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }
                    // Convert the entry's bytes into a UTF-8 string
                    String jsonString = baos.toString(StandardCharsets.UTF_8.name());
                    // Parse the string into a JsonObject
                    JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
                    
                    // Split the entry path by "/" and extract the directory name.
                    // Expected structure: STATE/SESSION/directory/FILENAME.json
                    String[] pathParts = entry.getName().split("/");
                    if (pathParts.length >= 3) {
                        String directory = pathParts[2]; // "bill", "people", or "vote"
                        // Add the JsonObject to the corresponding list in the map
                        directoryJsonMap
                            .computeIfAbsent(directory, k -> new ArrayList<>())
                            .add(jsonObject);
                    }
                }
                zis.closeEntry();
            }
        }

        return directoryJsonMap;
    }
}
