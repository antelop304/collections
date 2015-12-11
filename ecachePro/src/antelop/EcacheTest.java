package antelop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.Configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class EcacheTest {
	CacheManager cacheManager = null;
//	@Before
	public void createCacheManager() {
		cacheManager = CacheManager.create();
	}
	
//	@Before
	public void createCacheManager2() {
		cacheManager = CacheManager.create("src/config/myehcache.xml");
	}
	
//	@Before
	public void createCacheManager3() {
		Configuration config = new Configuration();
		cacheManager = CacheManager.create(config);
	}
	
//	@Before
	public void createCacheManager4() throws FileNotFoundException {
		
		InputStream in = new FileInputStream(new File("src/config/myehcache.xml"));
		cacheManager = CacheManager.create(in);
	}
	
	@Before
	public void createCacheManager5() throws FileNotFoundException {
		URL url = this.getClass().getResource("/config/myehcache.xml");
		cacheManager = CacheManager.create(url);
	}
	
	@Test
	public void test() {
		System.out.println(cacheManager);
//		cacheManager.addCache("test");
		Cache cache = new Cache("test", 1, true, false, 5, 2);
		cacheManager.addCache(cache);
		
		addElement(cache);
		
		cache = cacheManager.getCache("test");
		Element e = cache.get("key1");
		System.out.println(e.getObjectValue());
	}
	
	private void addElement(Cache cache) {
		Element element = new Element("key1", "aa");
		cache.put(element);
		
		element = new Element("key1", "bb");
		cache.put(element, true);
	}
	
	@After
	public void shutdown() {
		if(cacheManager != null)
			cacheManager.shutdown();
	}
}
