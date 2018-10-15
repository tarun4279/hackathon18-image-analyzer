package com.chegg.hackathon.imageanalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageAnalyzerApplication {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		SpringApplication.run(ImageAnalyzerApplication.class, args);
	}
}
