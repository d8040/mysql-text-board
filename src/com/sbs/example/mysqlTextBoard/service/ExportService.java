package com.sbs.example.mysqlTextBoard.service;

import java.util.List;

import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.dto.Article;
import com.sbs.example.mysqlTextBoard.dto.Board;
import com.sbs.example.mysqlTextBoard.dto.Member;
import com.sbs.example.mysqlTextBoard.util.Util;

public class ExportService {

	private ArticleService articleService;
	private MemberService memberService;

	public ExportService() {
		articleService = Container.articleService;
		memberService = Container.memberService;
	}

	int articleStart = 0;
	int totalPages = 0;
	int list = 10;
	int paging = 10;
	int pageQty = 0;
	int startPage = 0;
	int endPage = 0;

	public void makeHtml() {

//		Util.rmdir("site"); // 삭제하면 깃 초기화됨
		Util.mkdirs("site");

		Util.copy("site_template/app.css", "site/app.css");
		Util.copy("site_template/app.js", "site/app.js");

		detail();
//		articleListAll();
		mainPage();
		articleListPages();
	}

	private void articleListPages() {
		List<Board> boards = articleService.getForPrintBoards();

		for (Board board : boards) {
			List<Article> articles = articleService.getForPrintArticles(board.id);

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
		List<Article> articles_Paging = articleService.getArticlesByPaging(board.id, articleStart, list);
		StringBuffer mainContent = new StringBuffer();
		StringBuffer sb = new StringBuffer();

		sb.append(getHeadHtml("article_list_" + board.code));

		String bodyTemplate = Util.getFileContents("site_template/article_list.html");
		String foot = Util.getFileContents("site_template/foot.html");
		for (Article article : articles_Paging) {
			Member member = memberService.getMemberByMemberId(article.memberId);
			String link = board.code + article.id + ".html";

			mainContent.append("<div class=\"flex\">");
			mainContent.append("<div class=article-list__cell-id>" + article.id + "</div>");
			mainContent.append("<div class=article-list__cell-reg-date>" + article.regDate + "</div>");
			mainContent.append("<div class=article-list__cell-writer>" + member.name + "</div>");
			mainContent.append("<div class=article-list__cell-rcm>" + article.rcmCount + "</div>");
			mainContent.append("<div class=article-list__cell-title><a href=\"" + link + "\" class=hover-underline>"
					+ article.title + "</a></div>");
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
			pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.code + "_"
					+ ((int) Math.ceil((double) (((i - 1 - paging) / paging) * paging) + paging))
					+ ".html\">&lt;&lt; </a></div>");
		}
		if (i > 1) {
			pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.code + "_"
					+ (i - 1) + ".html\">&lt; 이전</a></div>");
		}
		for (int k = startPage; k <= endPage; k++) {
			if (k == i) {
				pageBoxContent.append("<div class=\"page-no selected\"><a class=\"flex\" href=\"article_list_"
						+ board.code + "_" + k + ".html\">" + k + "</a></div>");
			} else {
				pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.code + "_"
						+ k + ".html\">" + k + "</a></div>");
			}
		}
		if (i < totalPages) {
			pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.code + "_"
					+ (i + 1) + ".html\">다음 &gt;</a></div>");
		}
		if (i - 1 / paging < (totalPages - (totalPages % paging) + 1)) {
			pageBoxContent.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list_" + board.code + "_"
					+ ((int) Math.ceil((double) (((i - 1 + paging) / paging) * paging) + 1))
					+ ".html\"> &gt;&gt;</a></div>");
		}
		String body = bodyTemplate.replace("${article-list__main-content}", mainContent.toString());
		body = body.replace("${page-box__paging-content}", pageBoxContent.toString());
		body = body.replace("${title-bar__content}", "<i class=\"far fa-clipboard\"></i>  " +board.name);
		sb.append(body);
		sb.append(foot);

		String fileName = "article_list_" + board.code + "_" + i + ".html";
		String filePath = "site/" + fileName;

		Util.writeFileContents(filePath, sb.toString());
		System.out.println(filePath + "생성");

	}

	// 메인페이지
	private void mainPage() {
		String head = getHeadHtml("index");
		String bodyTemplate = Util.getFileContents("site_template/index.html");
		StringBuffer sb = new StringBuffer();
		String body = bodyTemplate;
		int i = 0;
		sb.append(head);
		List<Article> articles = articleService.getForMainPageArticles();
		for (Article article : articles) {
			body = body.replace("${index__summary-writer-" + i + "}", article.extra_writer);
			body = body.replace("${index__summary-board-" + i + "}", article.extra_boardName);
			body = body.replace("${index__summary-body-title-" + i + "}", article.title);
			body = body.replace("${index__summary-body-" + i + "}", article.body.subSequence(0, 200) + "......");
			body = body.replace("${index__summary-link-"+i+"}", article.extra_boardCode + (article.id) + ".html");
			i = i + 1;
		}
		sb.append(body);

		Util.writeFileContents("site/" + "index.html", sb.toString());
		System.out.println("site/" + "index.html" + "생성");
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
			sb.append(getHeadHtml("article_list"));
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
				Member member = memberService.getMemberByMemberId(article.memberId);
				Board board = articleService.getBoardByid(article.boardId);
				sb.append("<div class=\"flex\">");
				sb.append("<div class=article-list__cell-id>" + article.id + "</div>");
				sb.append("<div class=article-list__cell-reg-date>" + article.regDate + "</div>");
				sb.append("<div class=article-list__cell-writer>" + member.name + "</div>");
				sb.append("<div class=article-list__cell-rcm>" + article.rcmCount + "</div>");
				sb.append("<div class=article-list__cell-title><a href=\"" + board.code + article.id
						+ ".html\" class=hover-underline>" + article.title + "</a></div>");
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
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list"
						+ ((int) Math.ceil((double) (((i - 1 - paging) / paging) * paging) + paging))
						+ ".html\">&lt;&lt; </a></div>");
			}
			if (i > 1) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list" + (i - 1)
						+ ".html\">&lt; 이전</a></div>");
			}

			for (int k = startPage; k <= endPage; k++) {
				if (k == i) {
					sb.append("<div class=\"page-no selected\"><a class=\"flex\" href=\"article_list" + k + ".html\">"
							+ k + "</a></div>");
				} else {
					sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list" + k + ".html\">" + k
							+ "</a></div>");
				}
			}
			if (i < totalPages) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list" + (i + 1)
						+ ".html\">다음 &gt;</a></div>");
			}
			if (i - 1 / paging < (totalPages - (totalPages % paging) + 1)) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list"
						+ ((int) Math.ceil((double) (((i - 1 + paging) / paging) * paging) + 1))
						+ ".html\"> &gt;&gt;</a></div>");
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

		String head = getHeadHtml("detail");
		String bodyTemplate = Util.getFileContents("site_template/article_detail.html");
		String foot = Util.getFileContents("site_template/foot.html");

		for (Board board : boards) {
			List<Article> articles = articleService.getForPrintArticles(board.id);

			int id = 0;

			for (Article article : articles) {
				StringBuffer sb = new StringBuffer();
				sb.append(head);
				String body = bodyTemplate.replace("${article-detail__title}", article.title);
				body = body.replace("${title-bar__content}", "board > " + article.extra_boardName);
				body = body.replace("${article-detail__writer}", "작성자: " + article.extra_writer);
				body = body.replace("${article-detail__hit}", "조회수: " + article.hit + "");
				body = body.replace("${article-detail__regDate}", "작성일: " + article.regDate);
				body = body.replace("${article-detail__body}", article.body);
				body = body.replace("${article-detail__rcm}", "추천수: " + article.rcmCount + "");

				if (id > 0) {
					body = body.replace("${article-detail__link-prev-article}",
							board.code + (articles.get(id - 1).id) + ".html");
				} else {
					body = body.replace("${article-detail__link-prev-article-class}", "none");
				}
				body = body.replace("${article-detail__link-list}",
						"article_list_" + board.code + "_" + (int) Math.ceil((double) (id + 1) / paging) + ".html");
				body = body.replace("${title-bar__file}",
						"article_list_" + board.code + "_" + (int) Math.ceil((double) (id + 1) / paging) + ".html");
				if (articles.size() > id + 1) {
					body = body.replace("${article-detail__link-next-article}",
							board.code + (articles.get(id + 1).id) + ".html");
				} else {
					body = body.replace("${article-detail__link-next-article-class}", "none");
				}
				sb.append(body);
				sb.append(foot);
				String fileName = board.code + article.id + ".html";
				Util.writeFile("site/" + fileName, sb.toString());

				System.out.println("site/" + fileName + "생성");
				id = id + 1;
			}
		}
	}

	// 메뉴바 자동생성
	private String getHeadHtml(String pageName) {
		String head = Util.getFileContents("site_template/head.html");
		StringBuilder boardMenuContent = new StringBuilder();
		List<Board> forPrintBoard = articleService.getForPrintBoards();

		for (Board board : forPrintBoard) {
			boardMenuContent.append("<li>");

			String link = "article_list_" + board.code + "_1.html";
			boardMenuContent.append("<a href=\"" + link + "\" class=\"flex flex-jc-c flex-ai-c \">");

			boardMenuContent.append("<span>"+board.name+"</span>");
			boardMenuContent.append("</a></li>");
		}
		head = head.replace("[manu-bar-add]", boardMenuContent.toString());
//		String titleBarContentHtml = getTitleBarContentByPageName(pageName);
//		head = head.replace("${title-bar__content}", titleBarContentHtml);
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
