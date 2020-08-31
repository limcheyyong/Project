package com.sc.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.api_backend.model.request.OpenData;
import com.sc.db.news.model.News;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
public class OpenDataUtil {
	
	@Autowired
	public static final List<News> queryNewsList(OpenData openData   ){
		String strURL = "https://www.tainan.gov.tw/API/v3/Rest/News/22C3B697A101DF19?take="+ openData.getRows()+"&skip=0&sdate="+openData.getStartDate()+"&edate=" + openData.getEndDate();		
		List<News> newsList = new ArrayList<News>();
		try {
			String strRead  = basicHttpsGetIgnoreCertificateValidation(strURL);
			JsonNode productNode = new ObjectMapper().readTree(strRead);
			Iterator<JsonNode> iterator = productNode.elements();
	        while (iterator.hasNext()) {
	        	JsonNode marks = iterator.next();
	        	try {
					String linkUrl = marks.path("連結網址").textValue();
					String postTimeText = marks.path("刊登日期").textValue();    
					String newsTitle = marks.path("標題").textValue();
					String summary = marks.path("內容").textValue();
					String image_url = marks.path("相關圖片").toString();
					String strImgUrl = "";
					if(image_url.length()>0 && image_url.indexOf("(")>0 &&  image_url.indexOf(")")>image_url.indexOf("(")  )
					{
						strImgUrl = image_url.substring(image_url.indexOf("(")+1 ,image_url.indexOf(")"));
					}
					
					News news = new News();
					news.setNewsTitle(newsTitle);
					news.setPostTimeText(postTimeText);
					news.setLinkUrl(linkUrl);	            
					if(summary.length()>500)
						summary = summary.substring(0,499);	            	
					news.setSummary(summary);
					news.setNewsImage(strImgUrl);
					if(postTimeText.length()>8)
					{
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
						String strd = postTimeText.substring(0,postTimeText.indexOf(" "));
						Date post_date = sdf.parse(postTimeText.substring(0,postTimeText.indexOf(" ")));
						news.setPostTime(post_date);
					}
					newsList.add(news);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
	         }
        } catch (Exception e) {
			e.printStackTrace();
		}
		return newsList;
	}
	
	
	public static String basicHttpsGetIgnoreCertificateValidation(String url ) throws Exception {
        // String url = "https://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/Tainan/88?$top=500&$format=json";     
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // don't check
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // don't check
                }
            }
        };
         
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, trustAllCerts, null);
         
        LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);
         
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .build();
         
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) ...");
         
        CloseableHttpResponse response = httpclient.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return result;

    }
	
	public static final int checkSpecialDay(){
		int res = 1;  //1: 非假日, 2:假日


		return res;
	}
	
}
