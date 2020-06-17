package hfut.hu.BlockValueShare.common;

public class ParseDataUtil {
	 /**
	     *
	     * �����ļ���Ϣ��ʽ�� @action=Download["fileName":"fileSize":"result"]
	     * ����˵����
	     *        fileName   Ҫ���ص��ļ�������
	     *        fileSize   Ҫ���ص��ļ��Ĵ�С
	              result     �������
	     */
	    public static String getDownFileName(String data){
	        return data.substring(data.indexOf("[")+1,data.indexOf(":"));
	    }
	    public static String getDownFileSize(String data){
	        return data.substring(data.indexOf(":")+1,data.lastIndexOf(":"));
	    }
	    public static String getDownResult(String data){
	        return data.substring(data.lastIndexOf(":")+1,data.length()-1);
	    }

	    /**
	     *
	     *    �ϴ��ļ���Ϣ��ʽ�� @action=Upload["fileName":"fileSize":result]
	          ����˵����
	     *          fileName   Ҫ�ϴ����ļ�������
	     *          fileSize   Ҫ�ϴ����ļ��Ĵ�С
	                result     �ϴ����
	     */
	    public static String getUploadFileName(String data){
	        return data.substring(data.indexOf("[")+1,data.indexOf(":"));
	    }
	    public static String getUploadFileSize(String data){
	        return data.substring(data.indexOf(":")+1,data.lastIndexOf(":"));
	    }
	    public static String getUploadResult(String data){
	        return data.substring(data.lastIndexOf(":")+1,data.length()-1);
	    }

	    /**  �����ļ��б��ʽ
	     @action=GroupFileList["fileName":"fileName":"fileName"...]
	     */
	    public static String[] getFileList(String data){
	        String list = data.substring(data.indexOf("[")+1,data.length()-1);
	        return  list.split(":");
	    }
	}
