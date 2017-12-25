package com.example.wordsearch.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.wordsearch.dto.SearchKeyword;
import com.example.wordsearch.dto.SearchTerm;
import com.example.wordsearch.dto.SearchTermResponse;
//import com.example.wordsearch.error.ErrorResponse;
import com.example.wordsearch.service.WordSearchService;
import com.example.wordsearch.util.FastFileSearchUtils;
//import com.example.wordsearch.util.FileSearchUtils;

@RestController
@RequestMapping("/api")
public class SearchTermController {
	
	public static final Logger logger = LoggerFactory.getLogger(SearchTermController.class);

	@Autowired
	private Environment env;

	@Autowired
	WordSearchService wordSearchService;
	
	@PostConstruct
	  public void init(){
		System.out.println("Controller - creating index");
		//FastFileSearchUtils.createIndex();
	  }

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<? extends Object> doSearch(@RequestBody SearchTerm searchTerm) {
		SearchTermResponse response = new SearchTermResponse();
		if (wordSearchService.isValid(searchTerm)) {
			List<String> searchWordList = searchTerm.getSearchKeyword();
			logger.info("keyword List >> "+searchWordList.toString());
			logger.info("folder to Scan >> "+env.getProperty("dirPath"));
			try {
				Map<String, List<String>> resultMap = wordSearchService.searchFiles(searchWordList,
						env.getProperty("dirPath"));
				logger.info(" RESULT "+resultMap);
				if (resultMap != null && resultMap.size() > 0) {
					
					SearchKeyword searchKeyword = new SearchKeyword();
					searchKeyword.setResult(resultMap);
					response.setSearchKeyword(searchKeyword);
					return new ResponseEntity<SearchTermResponse>(response, HttpStatus.CREATED);
				} else {
					response.setStatusMessage("No Matches Found");
					return new ResponseEntity<SearchTermResponse>(response, HttpStatus.CREATED);
				}   

			} catch (IOException e) {

			}
		} else {
			throw new ConstraintViolationException("error", Collections.emptySet());
		}
		return null;

		// System.out.println(env.getProperty("dirPath"));
		// return "Greetings from Spring Boot!"+env.getProperty("dirPath");
	}
	
	 

}
