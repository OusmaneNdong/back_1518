package com.fonctionpublique.access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    private int id;
    private String prenom;
    private String nom;
    private String email;
    private String password;
    private String status;
    private String nin;
    private String passePort;
//    private  boolean isEnable = false;

    public  String fullName(){
        return prenom + " " + nom;
    }
}
