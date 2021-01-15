package com.sbs.example.mysqlTextBoard.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.analytics.data.v1alpha.AlphaAnalyticsDataClient;
import com.google.analytics.data.v1alpha.DateRange;
import com.google.analytics.data.v1alpha.Dimension;
import com.google.analytics.data.v1alpha.Entity;
import com.google.analytics.data.v1alpha.Metric;
import com.google.analytics.data.v1alpha.Row;
import com.google.analytics.data.v1alpha.RunReportRequest;
import com.google.analytics.data.v1alpha.RunReportResponse;
import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.apidto.DisqusApiDataListThread;
import com.sbs.example.mysqlTextBoard.dto.Article;
import com.sbs.example.mysqlTextBoard.mysqlutil.MysqlUtil;
import com.sbs.example.mysqlTextBoard.util.Util;

public class testRunner {
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TestDataType1 {
		public int age;
		public String name;
		public int height;
	}

	public void run() {
		MysqlUtil.setDBInfo(Container.config.getDbHost(), Container.config.getDbId(), Container.config.getDbPw(), Container.config.getDbName());
		testUpdateGoogleAnalyticsApi();
//		testUpdatePageHitByGa4Api();
//		testMakeArticleTagJsonFile();
	}

	private void testMakeArticleTagJsonFile() {
		Container.exportService.tagPage();
	}

	private void testUpdatePageHitByGa4Api() {
		Container.googleAnalyticsApiService.updatePageHits();
	}

	private void testUpdateGoogleAnalyticsApi() {
		String ga4PropertyId = Container.config.getGa4PropertyId();
		try (AlphaAnalyticsDataClient analyticsData = AlphaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder()
					.setEntity(Entity.newBuilder().setPropertyId(ga4PropertyId))
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addMetrics(Metric.newBuilder().setName("activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate("2020-12-22").setEndDate("today")).build();

			// Make the request
			RunReportResponse response = analyticsData.runReport(request);

			System.out.println("Report result:");
			for (Row row : response.getRowsList()) {
				System.out.printf("%s, %s%n", row.getDimensionValues(0).getValue(), row.getMetricValues(0).getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void testGoogleCredentials() {
		String keyFilePath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
		System.out.println(keyFilePath);
	}

	private void testJackson() {
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

	private void testJackson2() {
		String jsonString = "1";

		ObjectMapper ob = new ObjectMapper();
		Integer rs = null;

		try {
			rs = ob.readValue(jsonString, Integer.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}

		System.out.println(rs);
	}

	private void testJackson3() {
		String jsonString = "[1,2,3]";

		ObjectMapper ob = new ObjectMapper();
		List<Integer> rs = null;

		try {
			rs = ob.readValue(jsonString, List.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}

		System.out.println(rs.get(1));
	}

	private void testJackson4() {
		String jsonString = "[{\"age\":22, \"name\":\"홍길동\"},{\"age\":23, \"name\":\"홍길순\"},{\"age\":24, \"name\":\"임꺽정\"}]";

		ObjectMapper ob = new ObjectMapper();
		List<Map<String, Object>> rs = null;

		try {
			rs = ob.readValue(jsonString, List.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}

		System.out.println(rs.get(1).get("age"));
	}

	private void testJackson5() {
		String jsonString = "[{\"age\":22, \"name\":\"홍길동\", \"height\":178},{\"age\":23, \"name\":\"홍길순\", \"height\":168},{\"age\":24, \"name\":\"임꺽정\"}]";

		ObjectMapper ob = new ObjectMapper();
		List<TestDataType1> rs = null;

		try {
			rs = ob.readValue(jsonString, new TypeReference<List<TestDataType1>>() {
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}

		System.out.println(rs.get(0).height + rs.get(2).height);
	}

	private void testApi() {
		String url = "https://disqus.com/api/3.0/forums/listThreads.json";
		String rs = Util.callApi(url, "api_key=" + Container.config.getDisqusApiKey(),
				"forum=" + Container.config.getDisqusForumName(), "thread:ident=article_detail_2.html");
		System.out.println(rs);
	}

	private void testApi2() {
		String url = "https://disqus.com/api/3.0/forums/listThreads.json";
		Map<String, Object> rs = Util.callApiResponseToMap(url, "api_key=" + Container.config.getDisqusApiKey(),
				"forum=" + Container.config.getDisqusForumName(), "thread:ident=article_detail_2.html");
		List<Map<String, Object>> response = (List<Map<String, Object>>) rs.get("response");
		Map<String, Object> thread = response.get(0);
		System.out.println((int) thread.get("likes"));
	}

	private void testApi3() {
		String url = "https://disqus.com/api/3.0/forums/listThreads.json";
		DisqusApiDataListThread rs = (DisqusApiDataListThread) Util.callApiResponseTo(DisqusApiDataListThread.class,
				url, "api_key=" + Container.config.getDisqusApiKey(), "forum=" + Container.config.getDisqusForumName(),
				"thread:ident=article_detail_2.html");
		System.out.println(rs.response.get(0).likes + rs.response.get(0).posts);
	}

}
