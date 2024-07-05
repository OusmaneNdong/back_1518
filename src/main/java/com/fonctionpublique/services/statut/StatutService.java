package com.fonctionpublique.services.statut;

import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Structure;
import com.fonctionpublique.entities.Utilisateur;

import java.io.FileNotFoundException;

public interface StatutService {

    Integer rejetedStatut(Integer id);
}
