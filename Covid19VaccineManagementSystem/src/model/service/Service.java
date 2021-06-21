package model.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import util.Utility;
/**
 * <pre>
 * 일반 서비스 메서드 구현
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */
public class Service {
	
	/**
	 * <pre>
	 * REST API 서버로부터 특정 일자의 백신 접종 현황을 읽어오고 출력하는 메서드
	 * </pre>
	 * @param bDate 기준 일자
	 */
    public void inocNumToday(String bDate) {
    	
    	String surl = "https://api.odcloud.kr/api/15077756/v1/vaccine-stat?page=1&perPage=10";
    	String key = "zkRWa0Rhm9r4fYLj0DzsDezuSW%2FjBzuU3nAUHBSEtZizlGvabVXjN1ozTeDEBQDTZTJBMuXtKI%2FARKHcjw6T0Q%3D%3D";
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
    
    /**
     * <pre>
     * 우선접종 대상자 식별 메서드
     * </pre>
     * @return 우선접종 대상자이면 true, 그렇지 않으면 false
     */
    public boolean isPrior() {
    	Utility util = new Utility();
     	System.out.println("다음 질문에 Y 또는 N으로 답해주세요.");
    	System.out.println("***************************");
    	
    	if(util.getAnswer("의료기관 종사자입니까? ")) {
    		return true;
    	}
    	if(util.getAnswer("집단생활 생활자 및 종사자입니까? ")) {
    		return true;
    	}
    	if(util.getAnswer("65세 이상 고령자입니까? ")) {
    		return true;
    	}
    	if(util.getAnswer("중증도 이상 위험의 성인 만성 질환자입니까? ")) {
    		return true;
    	}
    	if(util.getAnswer("소아청소년 교육/보육시설 종사자입니까? ")) {
    		return true;
    	}
    	if(util.getAnswer("코로나19 1차 대응요원입니까? ")) {
    		return true;
    	}
    	if(util.getAnswer("50~64세 성인입니까? ")) {
    		return true;
    	}
    	if(util.getAnswer("경찰/소방 공무원/군인 등 사회필수인력입니까? ")) {
    		return true;
    	}
    	if(util.getAnswer("교정시설 등 수감자 혹은 직원입니까? ")) {
    		return true;
    	}
    	return false;
    }
}
