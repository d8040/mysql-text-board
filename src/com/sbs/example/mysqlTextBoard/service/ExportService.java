package com.sbs.example.mysqlTextBoard.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.dto.Article;
import com.sbs.example.mysqlTextBoard.dto.Board;
import com.sbs.example.mysqlTextBoard.dto.Member;
import com.sbs.example.mysqlTextBoard.util.Util;

public class ExportService {

    private ArticleService articleService;
    private MemberService memberService;
    private DisqusApiService disqusApiService;

    public ExportService() {
	articleService = Container.articleService;
	memberService = Container.memberService;
	disqusApiService = Container.disqusApiService;
    }

    int articleStart = 0;
    int totalPages = 0;
    int list = 10;
    int paging = 10;
    int pageQty = 0;
    int startPage = 0;
    int endPage = 0;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date time = new Date();
    String time1 = format1.format(time);

    public void makeHtml() {

	//		Util.rmdir("site"); // 삭제하면 깃 초기화됨
	Util.mkdirs("site");

	Util.copy("site_template/app.css", "site/app.css");
	Util.copy("site_template/app.js", "site/app.js");
	Util.copy("site_template/toastUi.js", "site/toastUi.js");
	Util.copy("site_template/current.sql", "site/current.sql");
	Util.forderCopy("site_template/img", "site/img");

	loadDataFromDisqus();
	loadDataFromGa4Data();

	detail();
	searchPage();
	tagPage();
	//		articleListAll();
	mainPage();
	articleListPages();
    }

    public void tagPage() {
	Map<String, List<Article>> articlesByTagMap = articleService.getArticlesByTagMap();

	String jsonText = Util.getJsonText(articlesByTagMap);
	Util.writeFile("site/article_tag.json", jsonText);
    }

    private void searchPage() {
	List<Article> articles = articleService.getForPrintArticlesForSearch();
	String jsonText = Util.getJsonText(articles);
	Util.writeFile("site/article_list.json", jsonText);

	Util.copy("site_template/article_search.js", "site/article_search.js");

	String head = getHeadHtml("article_search", 231321321);
	String foot = Util.getFileContents("site_template/foot.html");
	String html = Util.getFileContents("site_template/article_search.html");
	StringBuffer sb = new StringBuffer();
	String body = html;

	sb.append(head);
	sb.append(body);
	sb.append(foot);
	Util.writeFileContents("site/article_search.html", sb.toString());
	System.out.println("site/article_search.html" + "생성");
    }

    private void loadDataFromGa4Data() {
	Container.googleAnalyticsApiService.updatePageHits();
    }

    public String getArticleDetailFileName(int id) {
	return "article_detail_" + id + ".html";
    }

    private void loadDataFromDisqus() {
	Container.disqusApiService.updateArticleCount();
    }

    private void articleListPages() {
	List<Board> boards = articleService.getForPrintBoards();

	for (Board board : boards) {
	    List<Article> articles = articleService.getForPrintArticles(board.getId());

	    // 페이지 계산
	    articleStart = 0;
	    totalPages = (int) Math.ceil((double) articles.size() / list);

	    for (int i = 1; i <= totalPages; i++) {
		articleListPage(board, articleStart, i, totalPages);
		articleStart = articleStart + 10;
	    }
	}

    }

    private void articleListPage(Board board, int articleStart, int i, int totalPages) {
	List<Article> articles_Paging = articleService.getArticlesByPaging(board.getId(), articleStart, list);
	StringBuffer mainContent = new StringBuffer();
	StringBuffer sb = new StringBuffer();

	sb.append(getHeadHtml(board.getName(), 213654654));
	String bodyTemplate = Util.getFileContents("site_template/article_list.html");
	String foot = Util.getFileContents("site_template/foot.html");
	for (Article article : articles_Paging) {
	    Member member = memberService.getMemberByMemberId(article.getMemberId());
	    String link = "article_detail_" + article.getId() + ".html";
	    String comments = "   <i class=\"far fa-comment-dots\"></i> " + article.getCommentsCount();

	    mainContent.append("<div class=\"flex\">");
	    mainContent.append("<div class=article-list__cell-id>" + article.getId() + "</div>");
	    mainContent.append("<div class=article-list__cell-writer>" + member.getName() + "</div>");
	    if (article.getTitle().length() > 31) {
		mainContent.append("<div class=article-list__cell-title><a href=\"" + link + "\" class=hover-underline>" + article.getTitle().substring(0, 30) + "...." + comments);
	    } else {
		mainContent.append("<div class=article-list__cell-title><a href=\"" + link + "\" class=hover-underline>" + article.getTitle() + comments);
	    }
	    //			mainContent.append("<a href=\"{{fileName}}#disqus_thread\">0 Comments</a>");
	    mainContent.append("</a><nav>");
	    mainContent.append("<div class=article-list__cell-writer1>" + member.getName() + "</div>");
	    mainContent.append("<div class=article-list__cell-reg-date1>" + article.getRegDate().subSequence(2, 4) + "/" + article.getRegDate().subSequence(5, 7) + "/" + article.getRegDate().subSequence(8, 10) + "</div>");
	    mainContent.append("</nav>");
	    mainContent.append("</div>");
	    mainContent.append("<div class=article-list__cell-reg-date>" + article.getRegDate().subSequence(2, 4) + "/" + article.getRegDate().subSequence(5, 7) + "/" + article.getRegDate().subSequence(8, 10) + "</div>");
	    mainContent.append("<div class=article-list__cell-rcm>" + article.getRcmCount() + "</div>");
	    mainContent.append("<div class=article-list__cell-hit>" + article.getHit() + "</div>");
	    mainContent.append("</div>");
	}
	StringBuffer pageBoxContent = new StringBuffer();
	if (i >= 1) {
	    startPage = (((i - 1) / paging) * 10) + 1;
	    if (startPage == 0) {
		startPage = 1;
	    }
	    endPage = startPage + paging - 1;
	    if (endPage >= totalPages) {
		endPage = totalPages;
	    }
	}

	if (i > paging)

	{
	    pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.getCode().trim() + "_" + ((int) Math.ceil((double) (((i - 1 - paging) / paging) * paging) + paging)) + ".html\">&lt;&lt; </a></div>");
	}
	if (i > 1) {
	    pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.getCode().trim() + "_" + (i - 1) + ".html\">&lt; 이전</a></div>");
	}
	for (int k = startPage; k <= endPage; k++) {
	    if (k == i) {
		pageBoxContent.append("<div class=\"page-no selected\"><a class=\"flex\" href=\"article_list_" + board.getCode().trim() + "_" + k + ".html\">" + k + "</a></div>");
	    } else {
		pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.getCode().trim() + "_" + k + ".html\">" + k + "</a></div>");
	    }
	}
	if (i < totalPages) {
	    pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.getCode().trim() + "_" + (i + 1) + ".html\">다음 &gt;</a></div>");
	}
	if (i - 1 / paging < (totalPages - (totalPages % paging) + 1)) {
	    pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.getCode().trim() + "_" + ((int) Math.ceil((double) (((i - 1 + paging) / paging) * paging) + 1)) + ".html\"> &gt;&gt;</a></div>");
	}
	String body = bodyTemplate.replace("${article-list__main-content}", mainContent.toString());
	body = body.replace("${page-box__paging-content}", pageBoxContent.toString());
	body = body.replace("${title-bar__content}", "<i class=\"far fa-clipboard\"></i>  " + board.getName());
	sb.append(body);
	sb.append(foot);

	String fileName = "article_list_" + board.getName() + "_" + i + ".html";
	String filePath = "site/" + fileName;

	Util.writeFileContents(filePath, sb.toString());
	System.out.println(filePath + "생성");

    }

    // 메인페이지
    private void mainPage() {
	String head = getHeadHtml("index", 496465411);
	String foot = Util.getFileContents("site_template/foot.html");
	String bodyTemplate = Util.getFileContents("site_template/index.html");
	StringBuffer sb = new StringBuffer();
	String body = bodyTemplate;
	
	sb.append(head);
	List<Board> forPrintBoard = articleService.getForPrintBoards();
	for (Board board : forPrintBoard) {
	    System.out.println(board.getCode());
	    List<Article> articles = articleService.getForPrintArticles(board.getId());
	    int i = 0;
	    for (Article article : articles) {
//		String articleBodyForPrint = article.getBody().substring(0, 20);
//		articleBodyForPrint = articleBodyForPrint.replaceAll("script", "<!--REPLACE:script-->");
		body = body.replace("${index__summary-writer-"+board.getCode()+ i + "}", article.getExtra_writer());
		body = body.replace("${index__summary-board-"+board.getCode() + i + "}", article.getExtra_boardName());
		body = body.replace("${index__summary-body-title-"+board.getCode() + i + "}", article.getTitle());
		body = body.replace("${index__summary-regDate-"+board.getCode() + i + "}", article.getRegDate().subSequence(2, 4) + "-" + article.getRegDate().subSequence(5, 7) + "-" + article.getRegDate().subSequence(8, 10));
		body = body.replace("${index__summary-body-"+board.getCode() + i + "}", article.getBody());
		body = body.replace("${index__summary-link-"+board.getCode() + i + "}", "article_detail_" + article.getId() + ".html");
		i = i + 1;
		if(i == 4) {
		    break;
		}
	    }
	}
	sb.append(body);
	sb.append(foot);
	Util.writeFileContents("site/" + "index.html", sb.toString());
	System.out.println("site/index.html" + "생성");
    }

    // 전체 게시판리스트
    private void articleListAll() {
	String foot = Util.getFileContents("site_template/foot.html");

	List<Article> articles = articleService.getForPrintArticles();

	totalPages = (int) Math.ceil((double) articles.size() / list);
	startPage = 1;
	endPage = startPage + paging - 1;

	for (int i = 1; i <= totalPages; i++) {
	    List<Article> articles_Paging = articleService.getArticlesByPagingAll(articleStart, list);

	    StringBuffer sb = new StringBuffer();
	    sb.append(getHeadHtml("article_list", 213654654));
	    sb.append("<section class=\"section-1 con-min-width\">");
	    sb.append("<div class=\"con flex flex-jc-c\">");
	    sb.append("<div class=\"article-list\">");
	    sb.append("<header><div class=\"flex\">");
	    sb.append("<div class=\"article-list__cell-id\">번호</div>");
	    sb.append("<div class=\"article-list__cell-reg-date\">날짜</div>");
	    sb.append("<div class=\"article-list__cell-writer\">작성자</div>");
	    sb.append("<div class=\"article-list__cell-rcm\">추천수</div>");
	    sb.append("<div class=\"article-list__cell-title\">제목</div>");
	    sb.append("</div></header>");
	    sb.append("<main class=\"article-box\">");
	    for (Article article : articles_Paging) {
		Member member = memberService.getMemberByMemberId(article.getMemberId());
		Board board = articleService.getBoardByid(article.getBoardId());
		sb.append("<div class=\"flex\">");
		sb.append("<div class=article-list__cell-id>" + article.getId() + "</div>");
		sb.append("<div class=article-list__cell-reg-date>" + article.getRegDate() + "</div>");
		sb.append("<div class=article-list__cell-writer>" + member.getName() + "</div>");
		sb.append("<div class=article-list__cell-rcm>" + article.getRcmCount() + "</div>");
		sb.append("<div class=article-list__cell-title><a href=\"" + board.getCode().trim() + article.getId() + ".html\" class=hover-underline>" + article.getTitle() + "</a></div>");
		sb.append("</div>");
	    }
	    sb.append("</main></div></div>");
	    sb.append("<main class=\"paging\">");
	    sb.append("<div class=\"page-box con-min-witdh\">");
	    sb.append("<div class=\"page flex flex-jc-c con\">");
	    if (i > 1) {
		if ((i - 1) % paging == 0) {
		    startPage = startPage + paging;
		    endPage = startPage + paging - 1;
		    if (endPage >= totalPages) {
			endPage = totalPages;
		    }
		}
	    }
	    if (i > paging) {
		sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list" + ((int) Math.ceil((double) (((i - 1 - paging) / paging) * paging) + paging)) + ".html\">&lt;&lt; </a></div>");
	    }
	    if (i > 1) {
		sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list" + (i - 1) + ".html\">&lt; 이전</a></div>");
	    }

	    for (int k = startPage; k <= endPage; k++) {
		if (k == i) {
		    sb.append("<div class=\"page-no selected\"><a class=\"flex\" href=\"article_list" + k + ".html\">" + k + "</a></div>");
		} else {
		    sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list" + k + ".html\">" + k + "</a></div>");
		}
	    }
	    if (i < totalPages) {
		sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list" + (i + 1) + ".html\">다음 &gt;</a></div>");
	    }
	    if (i - 1 / paging < (totalPages - (totalPages % paging) + 1)) {
		sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list" + ((int) Math.ceil((double) (((i - 1 + paging) / paging) * paging) + 1)) + ".html\"> &gt;&gt;</a></div>");
	    }
	    sb.append("</div>");
	    sb.append("</div>");
	    sb.append("</main>");
	    sb.append(foot);

	    articleStart = articleStart + 10;
	    Util.writeFileContents("site/" + "article_list" + i + ".html", sb.toString());
	    System.out.println("site/" + "article_list" + i + ".html" + "생성");
	}
    }

    //상세페이지
    private void detail() {
	List<Board> boards = articleService.getForPrintBoards();

	String bodyTemplate = Util.getFileContents("site_template/article_detail.html");
	String foot = Util.getFileContents("site_template/foot.html");

	for (Board board : boards) {
	    List<Article> articles = articleService.getForPrintArticles(board.getId());

	    int id = 0;

	    for (Article article : articles) {
		StringBuffer sb = new StringBuffer();
		String head = getHeadHtml("detail", article.getId());
		sb.append(head);

		String articleBodyForPrint = article.getBody();
		articleBodyForPrint = articleBodyForPrint.replaceAll("script", "<!--REPLACE:script-->");

		String body = bodyTemplate.replace("${article-detail__title}", article.getTitle());
		body = body.replace("${title-bar__content}", "board > " + article.getExtra_boardName());
		body = body.replace("${article-detail__writer}", "작성자: " + article.getExtra_writer());
		body = body.replace("${article-detail__hit}", "조회수: " + article.getHit() + "");
		body = body.replace("${article-detail__regDate}", "작성일: " + article.getRegDate());
		body = body.replace("${article-detail__body}", articleBodyForPrint);
		body = body.replace("${article-detail__rcm}", "추천수: " + article.getRcmCount() + "");

		if (id > 0) {
		    body = body.replace("${article-detail__link-prev-article}", "article_detail_" + (articles.get(id - 1).getId()) + ".html");
		} else {
		    body = body.replace("${article-detail__link-prev-article-class}", "none");
		}
		body = body.replace("${article-detail__link-list}", "article_list_" + board.getName() + "_" + (int) Math.ceil((double) (id + 1) / paging) + ".html");
		body = body.replace("${title-bar__file}", "article_list_" + board.getName() + "_" + (int) Math.ceil((double) (id + 1) / paging) + ".html");
		if (articles.size() > id + 1) {
		    body = body.replace("${article-detail__link-next-article}", "article_detail_" + (articles.get(id + 1).getId()) + ".html");
		} else {
		    body = body.replace("${article-detail__link-next-article-class}", "none");
		}
		String fileName = "article_detail_" + article.getId() + ".html";
		body = body.replace("${site-domain}", "blog.phoneus.net");
		body = body.replace("${fileName}", fileName);
		sb.append(body);
		sb.append(foot);
		Util.writeFile("site/" + fileName, sb.toString());

		System.out.println("site/" + fileName + "생성");
		id = id + 1;
	    }
	}
    }

    // 메뉴바 자동생성
    private String getHeadHtml(String pageName, int articleId) {
	String head = Util.getFileContents("site_template/head.html");
	StringBuilder boardMenuContent = new StringBuilder();
	List<Board> forPrintBoard = articleService.getForPrintBoards();
	Article article = articleService.getArticle(articleId);
	for (Board board : forPrintBoard) {
	    if (board != null) {
		boardMenuContent.append("<li>");

		String link = "article_list_" + board.getName() + "_1.html";
		boardMenuContent.append("<a href=\"" + link + "\" class=\"flex flex-jc-c flex-ai-c \">");

		boardMenuContent.append("<span>" + board.getName() + "</span>");
		boardMenuContent.append("</a></li>");
	    }
	}
	head = head.replace("[manu-bar-add]", boardMenuContent.toString());
	if (articleId == 213654654) {
	    head = head.replace("${meta-title}", pageName + " List");
	    head = head.replace("${page-title}", pageName + " List");
	    head = head.replace("${meta-description}", "Article list for " + pageName);
	    head = head.replace("${meta-url)", "blog.phoneus.net/" + "article_list_" + pageName + "_1.html");
	} else if (articleId == 496465411) {
	    head = head.replace("${meta-title}", "Main Page");
	    head = head.replace("${page-title}", "fun and coding");
	    head = head.replace("${meta-description}", "Technology for java, html, photograph, technical, css,js, java script, scss, cording, c");
	    head = head.replace("${meta-url)", "blog.phoneus.net/site/index.html");
	} else if (articleId == 231321321) {
	    head = head.replace("${meta-title}", "Search Page");
	    head = head.replace("${page-title}", "fun and coding");
	    head = head.replace("${meta-description}", "Technology for java, html, photograph, technical, css,js, java script, scss, cording, c");
	    head = head.replace("${meta-url}", "blog.phoneus.net/site/article_search.html");
	} else {
	    head = head.replace("${meta-title}", article.getTitle());
	    head = head.replace("${meta-description}", article.getBody().substring(0, 40));
	    head = head.replace("${page-title}", article.getTitle());
	    head = head.replace("${meta-url}", "blog.phoneus.net/article_detail_" + article.getId() + ".html");
	}
	//		String titleBarContentHtml = getTitleBarContentByPageName(pageName);
	//		head = head.replace("${title-bar__content}", titleBarContentHtml);
	head = head.replace("${current-date}", time1);
	return head;
    }

    //	private String getTitleBarContentByPageName(String pageName) {
    //		if (pageName.equals("index")) {
    //			return "<i class=\"fas fa-home\"></i> <span>HOME</span>";
    //		} else if (pageName.contains("notice")) {
    //			return "<i class=\"far fa-clipboard\"></i> <span>WEB CORDING</span>";
    //		} else if (pageName.contains("free")) {
    //			return "<i class=\"fas fa-comment-medical\"></i> <span>FREE LIST</span>";
    //		} else if (pageName.contains("detail")) {
    //			return "<i class=\"fas fa-flag\"></i> <span>ARTICLE DETAIL</span>";
    //		} else if (pageName.startsWith("article_list")) {
    //			return "<i class=\"fas fa-flag\"></i> <span>ALL LIST</span>";
    //		}
    //		return "";
    //	}

}
