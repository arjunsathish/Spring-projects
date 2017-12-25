package com.example.wordsearch.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.wordsearch.dto.SearchTerm;
//import com.example.wordsearch.util.FileSearchUtils;

@Service("wordSearchService")
public class WordSearchServiceImpl implements WordSearchService {

	public static final Logger logger = LoggerFactory.getLogger(WordSearchServiceImpl.class);

	@Override
	public boolean isValid(SearchTerm searchterm) {

		// TODO Auto-generated method stub
		if (null != searchterm && null != searchterm.getSearchKeyword()) {
			return searchterm.getSearchKeyword().size() > 0;
		}
		return false;
	}

	@Override
	public Map<String, List<String>> searchFiles(List<String> searchWordList, String dirPath) throws IOException {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		List<String> matchFileList = new ArrayList<String>();
		logger.info(" RESULT "+dirPath);
		final Path path = Paths.get(dirPath);
		walkDirectory(searchWordList, path, matchFileList);
		result.put(searchWordList.toString(), matchFileList);
		return result;
	}

	private static void countWords(List<String> searchWordList, Path filePath, List<String> matchList)
			throws IOException {
		List<String> list = new ArrayList<>();
		int matchCount = 0;
		boolean matchFound = false;
		//logger.info("file To Process "+filePath.toUri());
		try (Stream<String> stream = Files.lines(Paths.get(filePath.toUri()))) {
			list = new ArrayList<>();
			list = stream.collect(Collectors.toList());
			//logger.info("file conatins"+list);
			for (String word : searchWordList) {
				logger.info(word);
				matchFound = list.stream().anyMatch(str -> str.trim().startsWith(word));
				if (!matchFound) {
					//logger.info("checking endwith");
					matchFound = list.stream().anyMatch(str -> str.trim().endsWith(word));
					//logger.info("Match Found"+matchFound);
					if (!matchFound) {
					matchFound = list.stream().anyMatch(str -> str.trim().equals(word));
					}
					//logger.info("Match Found"+matchFound);
				}
				
				if (matchFound) {
					//logger.info("Match Found");
					matchCount++;
				}
				//logger.info("MatchCount "+matchCount);
				if (matchCount == searchWordList.size()) {
					//logger.info("Exact Matches");
					continue;
				}  

			}
			
		} catch (IOException e) {
			throw new IOException("Exception in Searching keywords" + e);
		}
		list = null;
		if (matchFound) {
			matchList.add(filePath.toUri().toString());
			logger.info("matched File List "+matchList);

		}
	}

	private static void walkDirectory(List<String> searchWordList, Path dirPath, List<String> matchFileList)
			throws IOException {
        logger.info("coming here");
		if (Files.isDirectory(dirPath)) {
			// Iterate directory
			Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					countWords(searchWordList, file, matchFileList);
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			walkDirectory(searchWordList, dirPath, matchFileList);
		}

	}

}
