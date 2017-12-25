package com.example.wordsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages={"com.example.wordsearch"})
public class WordsearchApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(WordsearchApplication.class, args);
	}
	
}

