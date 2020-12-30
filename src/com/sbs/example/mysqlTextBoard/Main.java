package com.sbs.example.mysqlTextBoard;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbs.example.mysqlTextBoard.test.testRunner;
import com.sbs.example.mysqlTextBoard.util.Util;

public class Main {
	public static void main(String[] args) {
		new testRunner().run();
//		new App().run();
	}

	private static void testJackson() {
		String jsonString = "{\"age\":22, \"name\":\"홍길동\"}";
		
		ObjectMapper ob = new ObjectMapper();
		Map rs = null;
		try {
			rs = ob.readValue(jsonString, Map.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(rs.get("age"));
	}

	private static void testApi() {
		String url = "https://disqus.com/api/3.0/forums/listThreads.json";			
		
		String rs = Util.callApi(url, "api_key=tbCnSFNxE6RvUHSPdUu1spaX0MRXStP1TGg1YDS4ssDZBHZV7nI7oByduvqcPdBJ", "forum=dkey", "thread:ident=java7.html");
		System.out.println(rs);
	}
}
