package com.example.caravan_spring_project.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.example.caravan_spring_project.model.Caravan;
import com.example.caravan_spring_project.model.CaravanImage;
import com.example.caravan_spring_project.repository.CaravanImageRepository;
import com.example.caravan_spring_project.repository.CaravanRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;


@Service
public class ImageStorageService {

    private final CaravanRepository caravanRepository;
    private final CaravanService caravanService;

    public ImageStorageService(CaravanRepository caravanRepository, CaravanService caravanService) {
        this.caravanRepository = caravanRepository;
        this.caravanService = caravanService;
    }

    private final Path rootLocation = Paths.get("/app/uploads");

    public String saveImage(MultipartFile file, UUID caravanId, String description) {
        Optional<Caravan> optionalCaravan = caravanRepository.findById(caravanId);
        if (optionalCaravan.isEmpty()) {
            throw new RuntimeException("Caravan with ID " + caravanId + " not found.");
        }
        Caravan caravan = optionalCaravan.get();

        Path userFolder = rootLocation.resolve(String.valueOf(caravanId));
        try {
            Files.createDirectories(userFolder);

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileHash = DigestUtils.md5DigestAsHex(file.getBytes());
            String fileName = fileHash + "_" + System.currentTimeMillis() + extension;
            Path destinationFile = userFolder.resolve(Paths.get(fileName))
                    .normalize()
                    .toAbsolutePath();

            Path absoluteRoot = rootLocation.toAbsolutePath().normalize();
            if (!destinationFile.startsWith(absoluteRoot)) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            ImageDimensions dimensions = getImageDimensions(destinationFile);

            String relativePath = rootLocation.toAbsolutePath()
                    .relativize(destinationFile)
                    .toString()
                    .replace("\\", "/");

            caravanService.addCaravanImage(
                    caravan,
                    relativePath,
                    description,
                    dimensions.getWidth(),
                    dimensions.getHeight()
            );

            return relativePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }


    private ImageDimensions getImageDimensions(Path imagePath) {
        try {
            BufferedImage image = ImageIO.read(imagePath.toFile());
            if (image != null) {
                // Check if the image is rotated based on EXIF data
                if (isRotated(imagePath)) {
                    return new ImageDimensions(image.getHeight(), image.getWidth());
                }
                return new ImageDimensions(image.getWidth(), image.getHeight());
            } else {
                throw new IOException("Could not read image dimensions.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image dimensions", e);
        }
    }

    @Getter
    @AllArgsConstructor
    private static class ImageDimensions {
        private int width, height;
    }

    private boolean isRotated(Path imagePath) {
        try (InputStream metadataStream = Files.newInputStream(imagePath)) {
            Metadata metadata = ImageMetadataReader.readMetadata(metadataStream);
            ExifIFD0Directory exif = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (exif != null && exif.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                int orientation = exif.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                double rotationDegrees = 0;

                switch (orientation) {
                    case 6:
                        //rotationDegrees = 90;
                        return true;
                    case 3:
                        //rotationDegrees = 180;
                        return false;
                    case 8:
                        rotationDegrees = 270;
                        return true;

                    default:
                        rotationDegrees = 0;
                        return false;
                }
            }
            return false;

        } catch (IOException | ImageProcessingException | MetadataException e) {
            throw new RuntimeException(e);
        }
    }
}
