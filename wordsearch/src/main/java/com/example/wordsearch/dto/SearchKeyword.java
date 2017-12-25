/**
 * 
 */
package com.example.wordsearch.dto;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 *
 */
public class SearchKeyword {

	private Map<String,List<String>> result;

	public Map<String,List<String>> getResult() {
		return result;
	}

	public void setResult(Map<String,List<String>> result) {
		this.result = result;
	}
}
