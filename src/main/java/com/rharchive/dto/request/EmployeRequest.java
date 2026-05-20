// dto/request/EmployeRequest.java
package com.rharchive.dto.request;

import com.rharchive.enums.TypeContrat;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeRequest {
    @NotBlank private String nom;
    @NotBlank private String poste;
    @NotBlank private String departement;
    @Email @NotBlank private String email;
    private String telephone;
    @NotNull private TypeContrat contrat;
    @NotNull private LocalDate dateEmbauche;
    private String couleur;
}