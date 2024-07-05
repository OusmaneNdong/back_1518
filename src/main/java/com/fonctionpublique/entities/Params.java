package com.fonctionpublique.entities;

import java.net.URI;

public interface Params {
    public static final String SECRET_KEY = "3AgC5BMpo1qa/FZ4GQsKxd03rJMQkvrBnWNBCsDbHroEmbtld8x285bDM11Os/Ym\n";
    public static final String jwtSigningKey = "3AgC5BMpo1qa/FZ4GQsKxd03rJMQkvrBnWNBCsDbHroEmbtld8x285bDM11Os/Ym\n";
    public static final int EXP_DATE = 1000 * 60 * 24;
    //les attestations gnenerees
    public static final String DIRECTORYATTESTATION = System.getProperty("user.home")+"/Downloads/attestations";
    public static final String DIRECTORYCNI = System.getProperty("user.home")+"/Desktop/Attestation/Attestation/uploaded/documents";
    public static final String DIRECTORYQRCOD = "/Users/7maksacodpc/Downloads/The_QRCode/";


    public static final String LIENDEVERIFICATION = "https://localhost:4200/verificationattestation/";
    public static final String PREFIX = "Nº";
}
