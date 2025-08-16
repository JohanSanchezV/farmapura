/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.proyecto.serviceimpl;

import com.google.auth.Credentials;
import com.google.auth.ServiceAccountSigner;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.proyecto.service.FirebaseStorageService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    @Override
    public String cargaImagen(MultipartFile archivo, String carpeta, Long id) {
        try {
            String original = archivo.getOriginalFilename();
            String fileName = "img" + sacaNumero(id) + "_" + original;
            File tmp = File.createTempFile("img", null);
            try (FileOutputStream fos = new FileOutputStream(tmp)) {
                fos.write(archivo.getBytes());
            }

            // 3) Subir a Firebase y obtener URL
            String url = uploadFile(tmp, carpeta, fileName);

            // 4) Borrar el archivo temporal
            tmp.delete();

            return url;
        } catch (IOException e) {
            throw new RuntimeException("Error subiendo imagen a Firebase", e);
        }
    }

    private String uploadFile(File file, String carpeta, String fileName) throws IOException {
        ClassPathResource creds = new ClassPathResource(
            rutaJsonFile + File.separator + archivoJsonFile
        );
        Credentials credentials = GoogleCredentials.fromStream(creds.getInputStream());

        // 2) Inicializar cliente de Storage
        Storage storage = StorageOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .getService();

        // 3) Definir destino en bucket
        BlobId blobId = BlobId.of(
            BucketName,
            rutaSuperiorStorage + "/" + carpeta + "/" + fileName
        );
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(Files.probeContentType(file.toPath()))
            .build();

        // 4) Crear el blob
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        // 5) Generar URL firmada
        return storage.signUrl(
            blobInfo,
            3650, TimeUnit.DAYS,
            Storage.SignUrlOption.signWith((ServiceAccountSigner) credentials)
        ).toString();
    }

    private String sacaNumero(long id) {
        return String.format("%019d", id);
    }
}
