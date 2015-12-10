package mongodbpro.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class CodecRegistryTest {
	public static void main(String[] args) throws EmailException {
		HtmlEmail email = new HtmlEmail();
	    email.setHostName("smtp.163.com");
//	    email.setAuthentication("13235911336@163.com", "kwtdzlepttmmpuot");
	    email.setAuthentication("xiamufenggezi@163.com", "anhwvegggnogxyqh");
	    email.setCharset("UTF-8");
		email.addTo("13235911336@163.com", "rr");
	    email.setFrom("xiamufenggezi@163.com", "antelop");
	    email.setSubject("subject中文");
	    email.setHtmlMsg("<b>msg中文</b>");
	    String st = email.send();
	    System.out.println(Thread.currentThread().getId() + ":" + st);
		
	}
	MongoClient mongoClient = null;
	MongoDatabase database = null;
	MongoCollection<BasicDBObject> collection = null;
	
	@Before
	public void Init() {
		mongoClient = new MongoClient();
		database = mongoClient.getDatabase("test");
		collection = database.getCollection("mytest", BasicDBObject.class);
	}
	
	@Test
	public void insertAndModify() {
		// insert a document
		BasicDBObject document = new BasicDBObject("x", 1);
		collection.insertOne(document);
		document.append("x", 2).append("y", 3);

		// replace a document
		collection.replaceOne(Filters.eq("_id", document.get("_id")), document);
	}
	
	@After
	public void find() {
		// find documents
		List<BasicDBObject> foundDocument = collection.find().into(new ArrayList<BasicDBObject>());
		System.out.println(foundDocument);
	}
}
