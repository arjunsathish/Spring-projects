package com.example.wordsearch.dto;

public class SearchTermResponse {
	
	//private String statusCode;
	
	private String statusMessage;
	
	private SearchKeyword searchKeyword;

//	public String getStatusCode() {
//		return statusCode;
//	}
//
//	public void setStatusCode(String statusCode) {
//		this.statusCode = statusCode;
//	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public SearchKeyword getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(SearchKeyword searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

}
