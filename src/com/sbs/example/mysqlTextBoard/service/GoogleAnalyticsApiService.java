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

public class GoogleAnalyticsApiService {
	public void updatePageHitsDate() {
		
		String ga4PropertyId = Container.config.getGa4PropertyId();
		try (AlphaAnalyticsDataClient analyticsData = AlphaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder()
					.setEntity(Entity.newBuilder().setPropertyId(ga4PropertyId))
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addMetrics(Metric.newBuilder().setName("activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate("2020-12-01").setEndDate("today")).build();

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

}
