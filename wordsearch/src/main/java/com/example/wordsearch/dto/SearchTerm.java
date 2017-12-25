package com.example.wordsearch.dto;

import java.util.List;

public class SearchTerm {
	
	private List<String> searchKeyword;

	@Override
	public String toString() {
		return "SearchTerm [searchKeyword=" + searchKeyword + ", getSearchKeyword()=" + getSearchKeyword()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public List<String> getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(List<String> searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

}
