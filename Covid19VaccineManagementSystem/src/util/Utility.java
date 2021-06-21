package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * <pre>
 * 공통 메서드 
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */

public class Utility {
	
	/**
     * <pre>
     * 질문에 대한 형식에 맞는 답을 받는 메서드
     * </pre>
     * @param question 질문
     * @return 긍정하는 답이면 true, 그렇지 않으면 false
     */
    public boolean getAnswer(String question) {
    	Scanner sc = new Scanner(System.in);
    	String answer = null;
    	boolean close = false;
    	
    	while(!close) {
    		System.out.print(question + " (Y/N) : "); answer = sc.next();
    		if(answer.equals("Y") || answer.equals("y")) {
    			return true;
    		} else if (answer.equals("N") || answer.equals("n")) {
    			return false;
    		} else {
    			System.out.println("입력 형식이 올바르지 않습니다.");
    		}
    	}
    	return false;
    }
    
    /**
	 * <pre>
	 * 날짜 계산 메서드
	 * </pre>
	 * @param strDate 기준일
	 * @param day 더할 일 수
	 * @return 기준일이 day만큼 더해진 날짜
	 * @throws ParseException
	 */
	public String addDate(String strDate, int day) throws ParseException { 
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd"); 
		Calendar cal = Calendar.getInstance(); 
		Date dt = dtFormat.parse(strDate); 
		cal.setTime(dt); 

		cal.add(Calendar.DATE, day); 
		return dtFormat.format(cal.getTime()); 
	}
}
