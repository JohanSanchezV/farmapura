package com.proyecto.serviceimpl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.proyecto.service.FirebaseStorageService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    @Override
    public String cargaImagen(MultipartFile archivo, String carpeta, Long id) {
        try {
            String original = (archivo.getOriginalFilename() != null) ? archivo.getOriginalFilename() : "file";
            String fileName = "img" + sacaNumero(id) + "_" + original;

            // 1) Guardar a archivo temporal
            File tmp = File.createTempFile("img", null);
            try (FileOutputStream fos = new FileOutputStream(tmp)) {
                fos.write(archivo.getBytes());
            }

            // 2) Subir a Firebase con token de descarga y obtener URL pública
            String url = uploadFile(tmp, carpeta, fileName);

            // 3) Borrar temporal
            tmp.delete();

            return url;
        } catch (IOException e) {
            throw new RuntimeException("Error subiendo imagen a Firebase", e);
        }
    }

    private String uploadFile(File file, String carpeta, String fileName) throws IOException {
        // Cargar credenciales del JSON en resources/firebase/...
        ClassPathResource credsRes = new ClassPathResource(
                rutaJsonFile + File.separator + archivoJsonFile
        );
        Credentials credentials = GoogleCredentials.fromStream(credsRes.getInputStream());

        // Cliente de Storage
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();

        // Ruta dentro del bucket
        String objectPath = rutaSuperiorStorage + "/" + carpeta + "/" + fileName;

        // Token de descarga (Firebase)
        String downloadToken = UUID.randomUUID().toString();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("firebaseStorageDownloadTokens", downloadToken);

        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) contentType = "application/octet-stream";

        BlobId blobId = BlobId.of(BucketName, objectPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .setMetadata(metadata)
                .build();

        // Subir bytes
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        // URL pública permanente (mientras el token sea válido)
        String encodedPath = URLEncoder.encode(objectPath, StandardCharsets.UTF_8);
        return "https://firebasestorage.googleapis.com/v0/b/"
                + BucketName + "/o/" + encodedPath
                + "?alt=media&token=" + downloadToken;
    }

    private String sacaNumero(long id) {
        return String.format("%019d", id);
    }
}
