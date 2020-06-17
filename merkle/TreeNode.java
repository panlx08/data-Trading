package hfut.hu.BlockValueShare.merkle;

import hfut.hu.BlockValueShare.common.StringUtil;

/*
 * #树节点定义bean
 * @author
 */
public class TreeNode {
	//左孩子
	private TreeNode left;
	//右孩子
	private TreeNode right;
	//二叉树孩子节点的数据
	private String data;
	//二叉树中孩子节点的数据对应的哈希值
	private String hash;
	//节点名称
	private String name;
	
	public TreeNode() {
	}
	
	public TreeNode(String data) {
		this.data=data;
		this.hash=StringUtil.applySha256(data);
		this.name="[节点：" + data + "]";
	}
	
	public TreeNode getLeft() {
		return left;
	}
	public void setLeft(TreeNode left) {
		this.left = left;
	}
	public TreeNode getRight() {
		return right;
	}
	public void setRight(TreeNode right) {
		this.right = right;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
