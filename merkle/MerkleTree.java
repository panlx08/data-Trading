package hfut.hu.BlockValueShare.merkle;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hfut.hu.BlockValueShare.common.StringUtil;


/*Melkle树构建、生成根节点哈希值的工具类
 * 
 */
public class MerkleTree {
	//TreeNode List
	private List<TreeNode> list;
	//根节点
	private TreeNode root;
	
	//构造函数
	public MerkleTree(List<String> contents) {
		createMerkleTree(contents);
	}
	
	//构造merkle树
	private void createMerkleTree(List<String> contents) {
		//输入为空则不进行任何处理
		if(contents == null || contents.size() == 0) {
			return;
		}
		
		//初始化
		list= new ArrayList<TreeNode>();
		
		//根据数据创建叶子节点
		List<TreeNode> leafList =createLeafList(contents);
		list.addAll(leafList);
		
		//创建父节点
		List<TreeNode> parents = createParentList(leafList);
		list.addAll(parents);
		
		//循环创建各级父节点直至根节点
		while (parents.size() > 1) {
			List<TreeNode> temp = createParentList(parents);
			list.addAll(temp);
			parents = temp;
		}
		
		root = parents.get(0);
	}
	
	//创建父节点列表
	private List<TreeNode> createParentList(List<TreeNode> leafList) {
		List<TreeNode> parents = new ArrayList<TreeNode>();
		
		//空检验
		if(leafList == null || leafList.size() == 0) {
			return parents;
		}
		
		int length = leafList.size();
		for (int i = 0 ; i < length - 1; i+=2) {
			TreeNode parent = createParentNode(leafList.get(i),leafList.get(i+1));
			parents.add(parent);
		}
		
		//奇数个几点时，单独处理最后一个
		if (length % 2 != 0 ) {
			TreeNode parent = createParentNode(leafList.get(length - 1),null);
			parents.add(parent);
		}
		
		return parents;
	}
	
	//创建父节点
	private TreeNode createParentNode(TreeNode left,TreeNode right) {
		TreeNode parent = new TreeNode();
		
		parent.setLeft(left);
		parent.setRight(right);
		
		//如果right为空，则父节点的hash为left的hash
		String hash = left.getHash();
		if (right != null) {
			hash = StringUtil.applySha256(left.getHash() + right.getHash());
		}
		
		//hash字段和data字段
		parent.setData(hash);
		parent.setHash(hash);
		
		if (right != null) {
			parent.setName("(" + left.getName() + "和" + right.getName() + "的父节点)");
		}else {
			parent.setName("(继承节点{" + left.getName() + "}成为父节点)");
		}
		
		return parent;
	}

	//构建叶子节点列表
	private List<TreeNode> createLeafList(List<String> contents) {
		List<TreeNode> leafList = new ArrayList<TreeNode>();
		
		//空检验
		if (contents == null || contents.size() == 0) {
			return leafList;
		}
		
		for(String content: contents) {
			TreeNode node = new TreeNode(content);
			leafList.add(node);
		}
		
		return leafList;
	}
	
	//遍历树
	public void traverseTreeNodes() {
		Collections.reverse(list);
		TreeNode root = list.get(0);
		traverseTreeNodes(root);
	}
	
	public void traverseTreeNodes(TreeNode node) {
		System.out.println(node.getName());
		if(node.getLeft() != null) {
			traverseTreeNodes(node.getLeft());
		}
		
		if(node.getRight() != null) {
			traverseTreeNodes(node.getRight());
		}
	}

	public List<TreeNode> getList() {
		if (list == null) {
			return list;
		}
		Collections.reverse(list);
		return list;
	}
	
	public void setList(List<TreeNode> list) {
		this.list =list;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	
	
	public static void main(String[] args) {
		List<String> contents ;
		contents =Arrays.asList("区块链","人工智能","脑科学","K12教育全球优质公司");
		
		new MerkleTree(contents).traverseTreeNodes();
		
		System.out.println(new MerkleTree(contents).getRoot().getHash());
	}
}
