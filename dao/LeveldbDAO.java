package hfut.hu.BlockValueShare.dao;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.util.Strings;

public class LeveldbDAO {
	//日志记录
	private Logger logger =LoggerFactory.getLogger(LeveldbDAO.class);
	
	//LevelDB 的DB对象
	private DB db;
	
	@PostConstruct
	public void init() {
		try {
			DBFactory factory = new Iq80DBFactory();
			Options options = new Options();
			options.createIfMissing(true);
			//配置LecelDB存储目录
			String path = ".//leveldb";
			db = factory.open(new File(path), options);
			db.put("hello World".getBytes(), "hi".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void close() throws IOException {
		db.close();
	}
	//存入数据或修改
	public void put(String key,String value) {
		if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(value)) {
			return;
		}
		db.put(Iq80DBFactory.bytes(key),Iq80DBFactory.bytes(value));
	}
	
	//获取数据
	public String get(String key) {
		if (Strings.isNullOrEmpty(key)) {
			return null;
		}
		
		byte[] valueBytes = db.get(Iq80DBFactory.bytes(key));
		return Iq80DBFactory.asString(valueBytes);
	}
	
	//删除数据
	public void delete(String key) {
		if (Strings.isNullOrEmpty(key)) {
			return;
		}
		
		db.delete(Iq80DBFactory.bytes(key));
	}
	
	//遍历所有入库数据
	public void tranverseAllDates() {
		DBIterator iterator = db.iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<byte[], byte[]> next = iterator.next();
			String key = Iq80DBFactory.asString(next.getKey());
			String value = Iq80DBFactory.asString(next.getValue());
			logger.info(key +"; value= " + value );
		}
	}
}
