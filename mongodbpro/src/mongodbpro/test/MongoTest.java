package mongodbpro.test;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


public class MongoTest {
	MongoClient mongoClient = null;
	MongoDatabase database = null;
	MongoCollection<Document> collection = null;
	
	@Before
	public void Init() {
		mongoClient = new MongoClient();
		database = mongoClient.getDatabase("test");
		collection = database.getCollection("mytest");
	}
	
//	@Test
	public void find() {
		List<Document> docs = collection.find().into(new ArrayList<Document>());
		System.out.println(docs);
	}
	
//	@Test
	public void insertAndModify() {
//		collection.insertOne(new Document("name", "xiaowu"));
		// insert a document
		Document document = new Document("x", 1);
		collection.insertOne(document);
		document.append("x", 2).append("y", 3);

		// replace a document
		collection.replaceOne(Filters.eq("_id", document.get("_id")), document);
	}
	
	@Test
	public void delete() {
//		collection.deleteOne(new Document("x", 2));
		collection.deleteMany(new Document("x", 2));
	}
}
