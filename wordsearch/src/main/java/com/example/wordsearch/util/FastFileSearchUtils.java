package com.example.wordsearch.util;

import java.io.IOException;
import java.io.InputStream;
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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class FastFileSearchUtils {

	public static final String FILES_TO_INDEX_DIRECTORY = "E:\\filesToIndex";
	public static final String INDEX_DIR = "E:\\indexNewDirectory";

	public static final String FIELD_PATH = "path";
	public static final String FIELD_CONTENTS = "contents";

	public static void createIndex() {

		// Input folder
		String docsPath = "E:\\filesToIndex";

		// Output folder
		String indexPath = "E:\\indexNewDirectory";

		// Input Path Variable
		final Path docDir = Paths.get(docsPath);

		try {
			// org.apache.lucene.store.Directory instance
			Directory dir = FSDirectory.open(Paths.get(indexPath));

			// analyzer with the default stop words
			Analyzer analyzer = new StandardAnalyzer();

			// IndexWriter Configuration
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// IndexWriter writes new index files to the directory
			IndexWriter writer = new IndexWriter(dir, indexWriterConfig);

			// Its recursive method to iterate all files and directories
			indexDocs(writer, docDir);

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static void indexDocs(final IndexWriter writer, Path path) throws IOException {
		// Directory?
		if (Files.isDirectory(path)) {
			// Iterate directory
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						// Index this file
						indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			// Index this file
			indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
		}
	}

	static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
		try (InputStream stream = Files.newInputStream(file)) {

			// Create lucene Document
			Document doc = new Document();

			doc.add(new StringField("path", file.toString(), Field.Store.YES));
			doc.add(new LongPoint("modified", lastModified));
			doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Store.YES));

			// Updates a document by first deleting the document(s)
			// containing <code>term</code> and then adding the new
			// document. The delete and then add are atomic as seen
			// by a reader on the same index
			writer.updateDocument(new Term("path", file.toString()), doc);
		}
	}

	public static Map<String, List<String>> searchIndexWithPhraseQuery(final List<String> searchTermList,
			final String indexDirectory, final int slop) throws IOException {

		Map<String, List<String>> result = new HashMap<String, List<String>>();
		// Create lucene searcher. It search over a single IndexReader.
		IndexSearcher searcher = createSearcher();

		// Search indexed contents using search term
		TopDocs foundDocs = null;
		try {
			foundDocs = searchKeyWord(searchTermList, searcher);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Total found documents
		System.out.println("Total Results :: " + foundDocs.totalHits);
		List<String> fileList = new ArrayList<String>();
		// Let's print out the path of files which have searched term
		for (ScoreDoc sd : foundDocs.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			System.out.println("Path : " + d.get("path") + ", Score : " + sd.score);
			fileList.add(d.get("path"));
			System.out.println("searchTermList " + searchTermList.toString());
			result.put(searchTermList.toString(), fileList);
			System.out.println("Map " + result);
		}
		return result;
	}

	private static TopDocs searchKeyWord(final List<String> searchTermList, IndexSearcher searcher) throws Exception {
		// Create search query
		PhraseQuery.Builder builder = new PhraseQuery.Builder();
		builder.setSlop(0);
	    for (String searchKeyword : searchTermList) {
	    	builder.add(new Term(FIELD_CONTENTS, searchKeyword));
		}	
	    
	    PhraseQuery pq = builder.build();
	    System.out.println(pq.toString());

		// search the index
		TopDocs hits = searcher.search(pq, 5);
		return hits;
	}

	private static IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));

		// It is an interface for accessing a point-in-time view of a lucene
		// index
		IndexReader reader = DirectoryReader.open(dir);

		// Index searcher
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}

}
