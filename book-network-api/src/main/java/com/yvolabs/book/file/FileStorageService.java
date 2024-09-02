package com.yvolabs.book.file;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 01/09/2024
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadPath;

    /**
     * @return String -> The file path of uploaded image
     * @apiNote
     * <p>
     *     1) File size limit  defined in application.yml --> spring.servlet.multipart.max-file-size <br>
     *     2) File.separator - System dependant separator <br>
     *     3) File will be uploaded to the root Upload folder/ EG. uploads/users/1/1725271509157.jpg
     * </p>
     */
    public String saveFile(@Nonnull MultipartFile sourceFile, @Nonnull Integer bookId, @Nonnull Integer userId) {

        final String fileUploadSubPath = "users" + separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath, bookId);
    }

    private String uploadFile(MultipartFile sourceFile, String fileUploadSubPath, Integer bookId) {
        log.info("File upload start: bookId={}, fileUploadSubPath={} ", bookId, fileUploadSubPath);

        // FOLDER PATH
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Failed to create the target folder: {}", targetFolder);
                return null;
            }
        }

        // TARGET FULL FILENAME/PATH
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + separator + currentTimeMillis() + "." + fileExtension; //EG: uploads/users/1/1725271509157.jpg
        Path targetPath = Path.of(targetFilePath);

        // SAVE FILE
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved to {}", targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("File was not saved!", e);
        }

        return null;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        int lasDotIndex = fileName.lastIndexOf('.');
        if (lasDotIndex == -1) {
            return "";
        }

        return fileName.substring(lasDotIndex + 1).toLowerCase();
    }
}
