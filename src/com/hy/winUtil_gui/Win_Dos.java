package com.hy.winUtil_gui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 *	dos执行方法
 */
public class Win_Dos {
	private static String result = "";
	
    public static List<String> getFileName(){
    	//多个命令之间使用  & 做间隔
    	executeCmd("cmd.exe /C cd C:/Users/Administrator/Desktop/icon/ & dir /b");
    	String[] fileName = result.split(";");
    	ArrayList<String> list = new ArrayList<String>();
    	for (String s : fileName) {
    		System.out.println(s);
    		list.add(s);
		}
    	return list;
    }
    
     private static Map<String, String> executeCmd(String cmd) {
    	        Runtime rt = Runtime.getRuntime(); // 运行时系统获取
    	        Map<String, String> lineMap = new HashMap<String, String>();//存放返回值
    	        try {
    	            Process proc = rt.exec(cmd);// 执行命令
    	            InputStream stderr = proc.getInputStream();//执行结果 得到进程的标准输出信息流
    	            InputStreamReader isr = new InputStreamReader(stderr,"gbk");//将字节流转化成字符流
    	            BufferedReader br = new BufferedReader(isr);//将字符流以缓存的形式一行一行输出
    	            String line = null;
    	            while ((line = br.readLine()) != null) { 
    	                if (null != line && !"".equals(line)) {
    	                	
    	                	 Matcher m = Pattern.compile("[\\d][\\d][_]").matcher(line);
    	                	if(m.find()){
    	                		result = result + line +";";//打印结果
    	                	}
    	                	
    	                }
    	            }
    	            br.close();
    	            isr.close();
    	            stderr.close();
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	        return lineMap;
    	    }
     
     
     
}
