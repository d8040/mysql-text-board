package com.sbs.example.mysqlTextBoard;

import java.util.Scanner;

import com.sbs.example.mysqlTextBoard.controller.ExportController;
import com.sbs.example.mysqlTextBoard.controller.MemberController;
import com.sbs.example.mysqlTextBoard.dao.Ga4DataDao;
import com.sbs.example.mysqlTextBoard.dao.MemberDao;
import com.sbs.example.mysqlTextBoard.dao.TagDao;
import com.sbs.example.mysqlTextBoard.service.ArticleService;
import com.sbs.example.mysqlTextBoard.service.DisqusApiService;
import com.sbs.example.mysqlTextBoard.service.ExportService;
import com.sbs.example.mysqlTextBoard.service.GoogleAnalyticsApiService;
import com.sbs.example.mysqlTextBoard.service.MemberService;
import com.sbs.example.mysqlTextBoard.service.TagService;
import com.sbs.example.mysqlTextBoard.session.Session;

public class Container {
	public static GoogleAnalyticsApiService googleAnalyticsApiService;

	public static Scanner scanner;
	
	public static Session session;
	public static MemberDao memberDao;
	public static Ga4DataDao ga4DataDao;
	public static TagDao tagDao;
	public static TagService tagService;
	public static ArticleService articleService;
	public static MemberService memberService;
	public static ExportService exportService;
	public static MemberController memberController;
	public static ExportController exportController;
	public static DisqusApiService disqusApiService;
	public static AppConfig config;



	
	static {
		googleAnalyticsApiService = new GoogleAnalyticsApiService();
		
		config = new AppConfig();
		scanner = new Scanner(System.in);
		
		session = new Session();
		disqusApiService = new DisqusApiService();
		memberDao = new MemberDao();
		ga4DataDao = new Ga4DataDao();
		tagDao = new TagDao();
		tagService = new TagService();
		articleService = new ArticleService();
		memberService = new MemberService();
		exportService = new ExportService();
		memberController = new MemberController();
		exportController = new ExportController();
		
	}

}
