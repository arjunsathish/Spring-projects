package com.example.wordsearch.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import com.example.wordsearch.dto.SearchTerm;

public interface WordSearchService {
	
	boolean isValid(SearchTerm searchterm);
	
	Map<String,List<String>> searchFiles(List<String> searchWordList,String dirPath) throws IOException;
	

}
