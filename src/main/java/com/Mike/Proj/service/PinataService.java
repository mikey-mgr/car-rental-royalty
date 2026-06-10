package com.Mike.Proj.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.ByteArrayResource;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PinataService {

    private static final Logger log = LoggerFactory.getLogger(PinataService.class);
    private static final String PINATA_API_URL = "https://api.pinata.cloud/pinning/pinFileToIPFS";
    private static final String PINATA_GATEWAY = "https://gateway.pinata.cloud/ipfs/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Value("${PINATA_API_KEY:}")
    private String pinataApiKey;

    @Value("${PINATA_SECRET_KEY:}")
    private String pinataSecretKey;

    private static final byte[][] MAGIC_BYTES = {
        {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF},           // JPEG
        {(byte) 0x89, 0x50, 0x4E, 0x47},                    // PNG
        {(byte) 0x52, 0x49, 0x46, 0x46},                    // WEBP (RIFF...WEBP)
        {(byte) 0x47, 0x49, 0x46, 0x38}                     // GIF
    };

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String uploadImage(MultipartFile file) {
        validateFile(file);

        if (pinataApiKey == null || pinataApiKey.isBlank() || pinataSecretKey == null || pinataSecretKey.isBlank()) {
            throw new IllegalStateException("Pinata API keys not configured. Set PINATA_API_KEY and PINATA_SECRET_KEY in .env or environment variables.");
        }

        String ipfsHash = pinToIPFS(file, pinataApiKey, pinataSecretKey);
        String url = PINATA_GATEWAY + ipfsHash;
        log.info("Uploaded image to Pinata: {}", url);
        return url;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File too large. Maximum size is 5MB.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[8];
            int bytesRead = is.readNBytes(header, 0, 8);
            if (bytesRead < 3) {
                throw new IllegalArgumentException("File appears to be empty or corrupt");
            }
            if (!isValidImageMagicBytes(header)) {
                throw new IllegalArgumentException("File signature does not match any supported image format (JPEG, PNG, WebP, GIF)");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read file", e);
        }
    }

    private boolean isValidImageMagicBytes(byte[] header) {
        for (byte[] magic : MAGIC_BYTES) {
            if (startsWith(header, magic)) return true;
        }
        return false;
    }

    private boolean startsWith(byte[] data, byte[] prefix) {
        if (data.length < prefix.length) return false;
        for (int i = 0; i < prefix.length; i++) {
            if (data[i] != prefix[i]) return false;
        }
        return true;
    }

    private String pinToIPFS(MultipartFile file, String apiKey, String secretKey) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("pinata_api_key", apiKey);
            headers.set("pinata_secret_api_key", secretKey);

            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(PINATA_API_URL, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Pinata upload failed with status: {} body: {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Pinata upload failed with status: " + response.getStatusCode());
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
            String ipfsHash = (String) result.get("IpfsHash");
            if (ipfsHash == null) {
                throw new RuntimeException("Pinata response missing IpfsHash");
            }
            return ipfsHash;
        } catch (IOException e) {
            log.error("Pinata upload error", e);
            throw new RuntimeException("Failed to upload file to Pinata", e);
        }
    }
}
