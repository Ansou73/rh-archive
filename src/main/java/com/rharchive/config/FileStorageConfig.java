// config/FileStorageConfig.java
// ════════════════════════════════════════════════════════════
package com.rharchive.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import java.io.IOException;
import java.nio.file.*;

@Configuration
public class FileStorageConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Crée le répertoire d'upload au démarrage s'il n'existe pas.
     */
    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(path);
        System.out.println("✅ Répertoire d'upload initialisé : " + path);
    }

    /**
     * Expose le dossier uploads/ comme ressource statique.
     * Permet d'accéder aux fichiers via :
     *   GET /uploads/documents/{employeId}/{nomFichier}
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath.toString() + "/")
                .setCachePeriod(3600);  // cache 1h navigateur
    }

    /**
     * Résout le chemin absolu d'un fichier uploadé.
     * Utilisé par DocumentService pour construire le chemin de stockage.
     *
     * @param sousRep sous-répertoire (ex: "1" pour l'employé id=1)
     * @param nomFichier nom du fichier généré (UUID + extension)
     * @return Path absolu du fichier
     */
    public Path resoudreCheminFichier(String sousRep, String nomFichier) throws IOException {
        Path dir = Paths.get(uploadDir, sousRep).toAbsolutePath().normalize();
        Files.createDirectories(dir);
        return dir.resolve(nomFichier).normalize();
    }

    public String getUploadDir() { return uploadDir; }
}

