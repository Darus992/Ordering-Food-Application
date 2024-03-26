package pl.dariuszgilewicz.infrastructure.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

public class FileUploadUtil {

    public static String uploadFile(MultipartFile file, String uploadDir, String imageFileName) {
        String storageFileName = null;

        try {
            Date createdAt = new Date();

            if(imageFileName != null){
                storageFileName = createdAt.getTime() + "_" + file.getOriginalFilename();
                Path currentPath = Paths.get(uploadDir, imageFileName);
                Files.delete(currentPath);
            }else {
                storageFileName = createdAt.getTime() + "_" + file.getOriginalFilename();
            }
            Path newPath = Paths.get(uploadDir, storageFileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return storageFileName;
    }

    public static byte[] convertFileToBytes(MultipartFile file){
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while converting the file[%s] to a byte array. ".formatted(file.getOriginalFilename()) + e);
        }
    }
}
