package model.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 * <pre>
 * 일반 서비스 메서드 구현
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.1.0
 * @since jdk1.8
 */
public class Service {
	
	String surl = "https://api.odcloud.kr/api/15077756/v1/vaccine-stat?page=1&perPage=10";
	String key = "zkRWa0Rhm9r4fYLj0DzsDezuSW%2FjBzuU3nAUHBSEtZizlGvabVXjN1ozTeDEBQDTZTJBMuXtKI%2FARKHcjw6T0Q%3D%3D";
	
	public static void main(String[] args) {
		Service sv = new Service();
		sv.inocNumToday("2021-06-03");
	}
	
    public void inocNumToday(String bDate) {
    	
    	//String bDate = "2021-05-12";

    	String result = "";
    	
    	try {

    		URL url = new URL(surl + "&cond%5BbaseDate%3A%3AEQ%5D=" + bDate + "%2000:00:00&serviceKey=" + key);
    		BufferedReader bf;

    		bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

    		result = bf.readLine();

        	JSONParser jsonParser = new JSONParser();
        	JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        	
        	JSONArray infoResult = (JSONArray)jsonObject.get("data");
        	JSONObject acc = (JSONObject)infoResult.get(0);
        	
        	System.out.println("기준 일자 : " + acc.get("baseDate"));
        	System.out.println("지역\t\t당일 1차\t당일 2차\t누적 1차\t누적 2차\t(단위 : 명)");
        	System.out.println("===========================================================");
        	for(int i = 0; i<infoResult.size(); i++) {
        		JSONObject res = (JSONObject)infoResult.get(i);
        		if(res.get("sido").equals("세종특별자치시")) {
        			System.out.print("세종특별자치시\t");
        		} else {
        			System.out.print(res.get("sido") + "\t\t");
        		}
        		System.out.printf("%d\t%d\t%d\t%d\t\n", 
        				res.get("firstCnt"), res.get("secondCnt"), 
        				res.get("totalFirstCnt"), res.get("totalSecondCnt"));
        	}

    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
	
}
