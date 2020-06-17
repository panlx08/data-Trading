package hfut.hu.BlockValueShare.log;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import hfut.hu.BlockValueShare.merkle.SimpleMerkleTree;



/*
 *  #模拟存储Guava方式
 *  @author
 */

public class FileStoreUtil {
	//定义文件大小
	public static final int FILE_SIZE =1024;
	
	public static void  writeIntoTargetFile(String targetFileName, String content) {
		File newFile =new File(targetFileName);
		try{
			Files.write(content.getBytes(), newFile);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	//内容文件追加写入fileWriter方式
	public static void appendToTargetFile(String targetFileName,String content) {
		try {
			FileWriter writer=new FileWriter(targetFileName,true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	
	//内容文件追加写入Guava方式
	@SuppressWarnings("deprecation")
	public static void appendToTargetFile(String targetFileName,String content) {
		File file=new File(targetFileName);
		try {
			Files.append(content, file, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//模拟区块链内容写入文件
	public static void writeIntoFile(String content) {
		try {
			//查看当前目录logging是否存在
			File root=new File(".//");
			//获取当前文件下所有文件
			File[] files= root.listFiles();
			if (files==null) {
				//没有文件则创建
				String targetFileName =".//blockchain-"+ System.currentTimeMillis()+".loging";
				appendToTargetFile(targetFileName, content);  //appendToTargetFileByGuava
				return;
			}
			//有文件则查找loging
			boolean has= false;
			for(File file:files){ 
				String name	= file.getName();
				if (name.endsWith(".loging") && name.startsWith("blockchain-")) {
					//有则继续写入
					System.out.println(file.getPath());
					appendToTargetFile(file.getPath(), content);
					has= true;
					
					//写入超过size后转为log 
					if (file.length()>= FILE_SIZE) {
						String logPath = file.getPath().replace("loging", "log");
						File log = new File(logPath);
						Files.copy(file, log);
						//删除写满的logging
						file.delete();
					}
				}
			}
			//没有则创建新文件
			if (!has) {
				String targetFileName = ".//blockchain-" +
						System.currentTimeMillis() + ".loging";
				appendToTargetFile(targetFileName, content);
				return;
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	//模拟区块内容写入
	public static void writeIntoBlockFile() {
		List<String> list =new ArrayList<String>();
		list.add("AI");
		list.add("BlockChain");
		list.add("BrainScience");
		
		//List<String> alist= Arrays.asList("区块链","人工智能","脑科学","K12教育全球优质公司");
		for (int i = 0; i < 20; i++) {
			list.add(generateVCode(6));
			writeIntoFile( SimpleMerkleTree.getTreeNodeHash(list)+ list+"\n");
			//writeIntoFile( SimpleMerkleTree.getTreeNodeHash(alist)+ list+"\n");	
			//SimpleMerkleTree.getTreeNodeHash(list)
		}
	}
	
	//根据length长度生成字符串
	public static String generateVCode(int length) {
		Long vCode =new Double((Math.random() + 1) * Math.pow(10, length - 1)).longValue();
		return vCode.toString();
	}
	
	
	
	
	public static void main(String[] args) {
		writeIntoBlockFile();
	}
}