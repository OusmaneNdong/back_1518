package com.fonctionpublique;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@CrossOrigin
@SpringBootApplication
@ComponentScan(basePackages = "com.fonctionpublique")
public class AttestationApplication {



	public static void main(String[] args) throws FileNotFoundException, IOException {
		SpringApplication.run(AttestationApplication.class, args);


	}




}
