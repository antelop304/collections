package mongodbpro.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mongodbpro.util.MongoDBUtil;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class MongoDBUtilTest {
	MongoDatabase db = null;
	MongoCollection<Document> collection = null;
	
	@Before
	public void getBd() {
		db = MongoDBUtil.instance.getDB("test");
		System.out.println("-->" + db.getName());
		
	}
	
	@Test
	public void getAllDBNames() {
		MongoIterable<String> mongoIterable = MongoDBUtil.instance.getAllDBNames();
		List<String> tableNames = mongoIterable.into(new ArrayList<String>());
		System.out.println(tableNames);
	}
//	@Test
	public void getAllCollection() {
		List<String> collections = MongoDBUtil.instance.getAllCollections("test");
		for (Iterator<String> iterator = collections.iterator(); iterator.hasNext();) {
			String collName = iterator.next();
			MongoCollection<Document> coll = db.getCollection(collName);
			List<Document> docs = coll.find().into(new ArrayList<Document>());
			System.out.println(docs.size());
		}
	}
	
//	@Test
	public void getCollection() {
		collection = MongoDBUtil.instance.getCollection("test", "mytest");
		System.out.println(collection);
	}
	
	
}
