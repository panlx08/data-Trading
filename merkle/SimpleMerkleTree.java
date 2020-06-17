package hfut.hu.BlockValueShare.merkle;

import java.util.ArrayList;
import java.util.List;

import hfut.hu.BlockValueShare.common.StringUtil;

/*
 * #简化的merkle树计算根节点hash
 */
public class SimpleMerkleTree {
	public static String getTreeNodeHash(List<String> hashList) {
		if (hashList == null || hashList.size()== 0) {
			return null;
		}
		
		while (hashList.size() != 1) {
			hashList = getMerkleNodeList(hashList);
		}
		
		//最终只剩根节点
		return hashList.get(0);
	}
	//根据思想计算根节点哈希
	public static List<String> getMerkleNodeList(List<String> contentList) {
		
		List<String> merkelNodeList = new ArrayList<String>();
		
		if (contentList == null || contentList.size() == 0) {
			return merkelNodeList;
		}
		
		int index = 0,length = contentList.size();
		while(index <length ) {
			//获取左孩子节点数据
			String left = contentList.get(index++);
			//获取右孩子节点数据
			String right ="";
			if (index < length) {
				right =contentList.get(index++);		
			}
			
			//计算左右孩子节点的父节点的hash
			String sha2HexValue = StringUtil.applySha256(left + right);
			merkelNodeList.add(sha2HexValue);
			
		}
		return merkelNodeList;
	}
	
}
