/**
 * 
 */
package model.service;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import model.dto.Center;

/**
 * <pre>
 * 센터 관리 위한 메서드 구현
 * </pre>
 * @author Hyunsoo Kim
 * @ version ver.1.0
 * @ since jdk1.8
 */
public class CenterService {
	
	/** 센터들을 저장/관리하기 위한 자료 저장구조 */
	private ArrayList<Center> cenList = new ArrayList<Center>();
	private ArrayList<String> tempArrayList = new ArrayList<String>();
	private ArrayList<String> districts = new ArrayList<String>();
	

	public void readFile(){
		BufferedReader br = null;
		String line;
		String path = "./docs/centerInfo.csv";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			while((line = br.readLine()) != null) {
				String[] temp = line.split(","); 
				////////////////////////////////////////////////////////////////////////////////
				Center dto = new Center();
				dto.setCenterName(temp[0]);
				dto.setFacName(temp[1]);
				dto.setPostCode(temp[2]);
				dto.setDistrict(temp[3]);
				tempArrayList.add(temp[3]);
				dto.setAddress(temp[4]);
				if(temp.length>5) {
					dto.setContact(temp[5]);
				} else {
					dto.setContact(null);
				}
				cenList.add(dto);				
//				//////////////////////////////////////////////////////////////////////////////////
//				for(int i=0; i<temp.length; i++) {
//					System.out.print((i+1)+"열: "+temp[i]);
//					if(i!=temp.length-1) System.out.print(", ");
//					else System.out.println();
//				}
//				//////////////////////////////////////////////////////////////////////////////////
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		districts = (ArrayList)tempArrayList.parallelStream().distinct().collect(Collectors.toList());
//		for(int i=0;i<districts.size();i++) {
//			System.out.println(districts.get(i));
//		}
	}
}





