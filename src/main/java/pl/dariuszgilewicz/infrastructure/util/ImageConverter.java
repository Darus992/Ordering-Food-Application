package pl.dariuszgilewicz.infrastructure.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class ImageConverter {

    public static byte[] convertFileToBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while converting the file[%s] to a byte array. ".formatted(file.getOriginalFilename()) + e);
        }
    }

    public static String convertFromBytes(byte[] bytes) {
        try {
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while converting the byte array[%s] to a string. ".formatted(bytes) + e);
        }
    }

    public static byte[] convertToBytes(String imageAsString) {
        try {
            return Base64.getDecoder().decode(imageAsString);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while converting string:[ %s ] to a byte array. ".formatted(imageAsString) + e);
        }
    }
}
