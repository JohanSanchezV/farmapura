/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.proyecto.service;
import org.springframework.stereotype.Service;   
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FirebaseStorageService {

    public String cargaImagen(MultipartFile archivoLocalCliente, String carpeta, Long id);

    final String BucketName = "farmapura-cdf27.firebasestorage.app";

    //Esta es la ruta básica de este proyecto
    final String rutaSuperiorStorage = "farmaproyecto";

    //Ubicación donde se encuentra el archivo de configuración Json
    final String rutaJsonFile = "firebase";
    
    //El nombre del archivo Json
    final String archivoJsonFile = "farmapura-cdf27-firebase-adminsdk-fbsvc-954185b2cd.json";
}
