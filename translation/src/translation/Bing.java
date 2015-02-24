package translation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Bing {

	public static void main(String[] args) throws Throwable {
		Bing bing = new Bing();
		bing.redir("translate.txt", "e:/");
		 try {
             File file=new File("e:/input.txt");
             if(file.isFile() && file.exists()){
                 InputStreamReader read = new InputStreamReader(
                 new FileInputStream(file));
                 BufferedReader bufferedReader = new BufferedReader(read);
                 String lineTxt = null;
                 while((lineTxt = bufferedReader.readLine()) != null){
            	    Pattern p = Pattern.compile("(\\d+)\\s(\\S+)\\s+(\\d+)\\s(.*)\\s{2,}(.*)");
            	    Matcher m = p.matcher(lineTxt);
            	    while (m.find()) {
            	    	//System.out.println(m.group(4));
            	    	String idc = m.group(4);
            	    	
            	    	Matcher m1 = bing.translate(idc);
            	    	while (m1.find()) {
                        //System.out.println(m1.group(1));
                        System.out.println(lineTxt.replace(m.group(4), m1.group(1)));
                       }                      
                    }
                 }
                 read.close();
     }else{
         System.out.println("找不到指定的文件");
     }
     } catch (Exception e) {
         System.out.println("读取文件内容出错");
         e.printStackTrace();
     }

	    
	}
	public Matcher translate(String q) throws Throwable {
		//String q= "Superficial foreign body of left forearm";
		String word="["+"\""+ q +"\""+ "]";
		URI uri2 = new URIBuilder()
		 .setScheme("http")
	        .setHost("api.microsofttranslator.com")
	        .setPath("/v2/ajax.svc/TranslateArray2")
	        .setParameter("appId", "\"T-teNuudQGc4TDxHlTaW0kq3jG7YanRhteunzxm-9rBY*\"")
	        .setParameter("texts", word)
	        .setParameter("from", "\"\"")
	        .setParameter("to", "\"zh-CHS\"")
	        .setParameter("options", "{}")
	        .setParameter("oncomplete","onComplete_4")
	        .setParameter("onerror", "onError_4")
	        .setParameter("_", "1407390273271")
	        .build();
		//System.out.println(uri2);
		Document doc = Jsoup.connect(uri2.toString()).ignoreContentType(true).timeout(10000).get();
	    String res = doc.body().text();
	    //System.out.println(res);
	    Pattern p = Pattern.compile("TranslatedText\":\"(.*)\",\"TranslatedTextSentenceLengths");
	    Matcher m = p.matcher(res);
		return m ;
		
	}
	
	public void redir(String drug_no,String path_name) {
		  String path = path_name;
	        PrintStream ps = null;
		    try {
		      FileOutputStream fos = 
		              new FileOutputStream(path+File.separator+drug_no);
		      ps = new PrintStream(fos);
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    if(ps != null){
		      System.setOut(ps);
		    }
	}

}
