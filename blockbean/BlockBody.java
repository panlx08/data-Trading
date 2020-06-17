package hfut.hu.BlockValueShare.blockbean;
import java.util.ArrayList;
import java.util.List;

/* 
 * #区块body，存放数据摘要
 * @author
 */
public class BlockBody{
	
	private List<ContentInfo> dataSet = new ArrayList<ContentInfo>();
	/* 
	 * 	区块的拥有着公钥
	 */
	private String pubKey;
	public BlockBody() {
		List<ContentInfo> dataSet= new ArrayList<ContentInfo>();
		this.pubKey = "null";
		this.dataSet = dataSet;
	}
	@Override
	public String toString() {
		return "BlockBody [dataSet=" + dataSet + ", pubKey=" + pubKey + "]";
	}
	public List<ContentInfo> getDataSet(){
		return dataSet;
	}
	public void setDataSet(List<ContentInfo> dataSet) {
		this.dataSet = dataSet;
	}
	public String getPubKey() {
		return pubKey;
	}
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}
}


