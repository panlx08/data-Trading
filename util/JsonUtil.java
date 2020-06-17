package hfut.hu.BlockValueShare.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	public static String toJson(Object object) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
	}
	//Short hand helper to turn Object into a json string
    public static String getJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }

    //Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
    public static String getDificultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }
//	public static void main(String []args) {
//		
//		List<String> a = new ArrayList<String>();
//		a.add("HelloGirl");
//		a.add("GoAway");
//		Block block = new Block(a);
//		block.mineBlock();
//		block.mineBlock();
//		block.mineBlock();
//		block.mineBlock();
//		System.out.println(block.getBlockHeader().getMerkleRoot());
//		System.out.println(block.getBlockHash());
//		System.out.println(JsonUtil.toJson(block));
//	}
}
