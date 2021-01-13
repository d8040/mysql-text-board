package com.sbs.example.mysqlTextBoard.service;

import java.io.IOException;

import com.google.analytics.data.v1alpha.AlphaAnalyticsDataClient;
import com.google.analytics.data.v1alpha.DateRange;
import com.google.analytics.data.v1alpha.Dimension;
import com.google.analytics.data.v1alpha.Entity;
import com.google.analytics.data.v1alpha.Metric;
import com.google.analytics.data.v1alpha.Row;
import com.google.analytics.data.v1alpha.RunReportRequest;
import com.google.analytics.data.v1alpha.RunReportResponse;
import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.dao.Ga4DataDao;

public class GoogleAnalyticsApiService {
	private Ga4DataDao ga4DataDao;
	
	public GoogleAnalyticsApiService() {
		ga4DataDao = new Ga4DataDao();
	}
	public boolean updateGa4DataPageHits() {		
		String ga4PropertyId = Container.config.getGa4PropertyId();
		
		try (AlphaAnalyticsDataClient analyticsData = AlphaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder()
					.setEntity(Entity.newBuilder().setPropertyId(ga4PropertyId))
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addMetrics(Metric.newBuilder().setName("activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate("2020-12-30").setEndDate("today")).build();

			// Make the request
			RunReportResponse response = analyticsData.runReport(request);

			for (Row row : response.getRowsList()) {
				String pagePath = row.getDimensionValues(0).getValue();
				
				int hit = Integer.parseInt(row.getMetricValues(0).getValue());
				
				update(pagePath, hit);
				
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	private void update(String pagePath, int hit) {
		ga4DataDao.deletePagePath(pagePath);
		ga4DataDao.savePagePath(pagePath, hit);
	}
	
	public void updatePageHits() {
		updateGa4DataPageHits();
		Container.articleService.updatePageHits();
	}

}
