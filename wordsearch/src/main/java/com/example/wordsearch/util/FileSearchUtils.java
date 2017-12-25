//package com.example.wordsearch.util;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.Reader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.index.CorruptIndexException;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.Term;
//import org.apache.lucene.queryParser.ParseException;
//import org.apache.lucene.search.Hit;
//import org.apache.lucene.search.Hits;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.PhraseQuery;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.store.LockObtainFailedException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class FileSearchUtils {
//	
//	public static final Logger logger = LoggerFactory.getLogger(FileSearchUtils.class);
//
//	public static final String FILES_TO_INDEX_DIRECTORY = "E:\\filesToIndex";
//	public static final String INDEX_DIRECTORY = "E:\\indexDirectory";
//
//	public static final String FIELD_PATH = "path";
//	public static final String FIELD_CONTENTS = "contents";
//	
//
//	public static Map<String, List<String>> searchIndexWithPhraseQuery(final List<String> searchTermList, final String indexDirectory, final int slop)
//			throws IOException, ParseException {
//		
//			
//		Map<String, List<String>> result = new HashMap<String, List<String>>();
//
//		Directory directory = FSDirectory.getDirectory(INDEX_DIRECTORY);
//
//		IndexSearcher indexSearcher = new IndexSearcher(directory);
//
//		PhraseQuery phraseQuery = new PhraseQuery();
//
//		for (String searchKeyword : searchTermList) {
//			phraseQuery.add(new Term(FIELD_CONTENTS, searchKeyword));
//
//		}
//		phraseQuery.setSlop(slop);
//		Hits hits = indexSearcher.search(phraseQuery);
//		System.out.println("Number of hits: " + hits.length());
//
//		Iterator<Hit> it = hits.iterator();
//		List<String> fileList = new ArrayList<String>();
//		while (it.hasNext()) {
//			Hit hit = it.next();
//			Document document = hit.getDocument();
//			String path = document.get(FIELD_PATH);
//			System.out.println("Hit: " + path);
//			fileList.add(path);
//			System.out.println("searchTermList "+searchTermList.toString());
//			result.put(searchTermList.toString(),fileList);
//			System.out.println("Map "+result);
//		}
//		
//		return result;
//	
//	}
//
//	public static void createIndex() throws CorruptIndexException, LockObtainFailedException, IOException {
//		Analyzer analyzer = new StandardAnalyzer();
//		boolean recreateIndexIfExists = true;
//		IndexWriter indexWriter = new IndexWriter(INDEX_DIRECTORY, analyzer, recreateIndexIfExists);
//		File dir = new File(FILES_TO_INDEX_DIRECTORY);		
//		File[] files = dir.listFiles();
//		for (File file : files) {
//			if (file.isDirectory()) {
//				File[] childFiles = file.listFiles();
//				for (File childFile : childFiles) {
//					addDocument(indexWriter, childFile);
//				}
//			} else {
//				addDocument(indexWriter, file);
//			}
//		}
//		indexWriter.optimize();
//		indexWriter.close();
//	}
//
//	private static void addDocument(IndexWriter indexWriter, File file) throws CorruptIndexException, IOException {
//		Document document = new Document();
//		String path;
//		try {
//			path = file.getCanonicalPath();
//			document.add(new Field(FIELD_PATH, path, Field.Store.YES, Field.Index.UN_TOKENIZED));
//			Reader reader = new FileReader(file);
//			document.add(new Field(FIELD_CONTENTS, reader));
//			indexWriter.addDocument(document);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//}
