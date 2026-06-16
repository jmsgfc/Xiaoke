package com.xiaoke.daily.storage;

import com.xiaoke.daily.config.AppProperties;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class LocalFileStorageService {
    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "webp");
    private static final DateTimeFormatter YEAR = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MM");
    private final AppProperties properties;

    public LocalFileStorageService(AppProperties properties) {
        this.properties = properties;
    }

    public UploadResponse save(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("请选择图片文件");
        }
        String extension = extension(file.getOriginalFilename());
        if (!ALLOWED.contains(extension)) {
            throw new IllegalArgumentException("只支持 jpg、jpeg、png、webp 图片");
        }

        LocalDate today = LocalDate.now();
        String relativeDir = "photos/" + today.format(YEAR) + "/" + today.format(MONTH);
        Path dir = Path.of(properties.getUploadDir()).resolve(relativeDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        String id = UUID.randomUUID().toString().replace("-", "");
        String originalName = id + "-original." + extension;
        String thumbName = id + "-thumb.jpg";
        Path original = dir.resolve(originalName);
        Path thumbnail = dir.resolve(thumbName);
        file.transferTo(original);
        Thumbnails.of(original.toFile())
                .width(600)
                .outputFormat("jpg")
                .toFile(thumbnail.toFile());

        String storagePath = relativeDir + "/" + originalName;
        String thumbPath = relativeDir + "/" + thumbName;
        return new UploadResponse(
                url(storagePath),
                url(thumbPath),
                storagePath,
                thumbPath
        );
    }

    public void deleteIfExists(String storagePath) throws IOException {
        if (storagePath == null || storagePath.isBlank()) {
            return;
        }
        Path root = Path.of(properties.getUploadDir()).toAbsolutePath().normalize();
        Path target = root.resolve(storagePath).toAbsolutePath().normalize();
        if (!target.startsWith(root)) {
            throw new IllegalArgumentException("文件路径不在上传目录内");
        }
        Files.deleteIfExists(target);
    }

    private String url(String storagePath) {
        String base = properties.getPublicUploadBaseUrl();
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/" + storagePath.replace("\\", "/");
    }

    private String extension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("图片缺少扩展名");
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }
}
