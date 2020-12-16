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
	int pages = 10;
	int pageQty = 0;
	int startPage = 0;
	int endPage = 0;

	public void makeHtml() {

		Util.rmdir("site");
		Util.mkdirs("site");

		Util.copy("site_template/part/app.css", "site/app.css");

		detail();
		articleListAll();
		articleListNotice();
		articleListFree();
		statistics();
	}

	// 통계
	private void statistics() {
		String head = getHeadHtml();
		String foot = Util.getFileContents("site_template/part/foot.html");
		StringBuffer sb_statastics = new StringBuffer();
		List<Member> members = memberService.getForPrintMembers();
		List<Article> articles = articleService.getForPrintArticles();
		int articleQtyBynoticeBoard = articleService.getArticlesCount(1);
		int articleQtyByfreeBoard = articleService.getArticlesCount(2);
		int hitByAllBoardArticles = articleService.getHitByAllArticles();
		int hitByNoticeBoardArticles = articleService.getHitByBoardArticles(1);
		int hitByFreeBoardArticles = articleService.getHitByBoardArticles(2);

		sb_statastics.append(head);
		sb_statastics.append("<div class=\"title-bar con-min-width\">");
		sb_statastics.append("<h1 class=\"con\">");
		sb_statastics.append("<i class=\"fas fa-flag\"></i>");
		sb_statastics.append("<span>통계</span>");
		sb_statastics.append("</h1>");
		sb_statastics.append("</div>");
		sb_statastics.append("<section class=\"section-1 con-min-width\">");
		sb_statastics.append("<main class=\"con-min-width\">");
		sb_statastics.append("<div class=con satastics>");
		sb_statastics.append("<div class=satastics__member>" + "회원수" + members.size() + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "전체 게시물 수" + articles.size() + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "공지게시판 게시물 수" + articleQtyBynoticeBoard + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "자유게시판 게시물 수" + articleQtyByfreeBoard + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "전체 게시물 조회 수" + hitByAllBoardArticles + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "공지게시판 게시물 조회 수" + hitByNoticeBoardArticles + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "자유게시판 게시물 조회 수" + hitByFreeBoardArticles + "</div>");
		sb_statastics.append("</div>");
		sb_statastics.append("</main>");
		sb_statastics.append(foot);
		Util.writeFileContents("site/" + "index.html", sb_statastics.toString());
		System.out.println("site/" + "index.html" + "생성");
	}

	// 자유 게시판 페이징
	private void articleListFree() {
		String head = getHeadHtml();
		String foot = Util.getFileContents("site_template/part/foot.html");
		List<Article> articles = articleService.getForPrintArticles(2);
		articleStart = 0;
		totalPages = (int) Math.ceil((double) articles.size() / pages);
		startPage = 1;
		endPage = startPage + pages - 1;
		for (int i = 1; i <= totalPages; i++) {
			List<Article> articles_Paging = articleService.getArticlesByPaging(2, articleStart, pages);
			StringBuffer sb = new StringBuffer();
			sb.append(head);
			sb.append("<div class=\"title-bar con-min-width\">");
			sb.append("<h1 class=\"con\">");
			sb.append("<i class=\"fas fa-flag\"></i>");
			sb.append("<span>자유 게시판" + i + "페이지" + "</span>");
			sb.append("</h1>");
			sb.append("</div>");
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
				sb.append("<div class=\"flex\">");
				sb.append("<div class=article-list__cell-id>" + article.id + "</div>");
				sb.append("<div class=article-list__cell-reg-date>" + article.regDate + "</div>");
				sb.append("<div class=article-list__cell-writer>" + member.name + "</div>");
				sb.append("<div class=article-list__cell-rcm>" + article.rcmCount + "</div>");
				sb.append("<div class=article-list__cell-title><a href=\"free" + article.id
						+ ".html\" class=hover-underline>" + article.title + "</a></div>");
				sb.append("</div>");
			}
			sb.append("</main>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("<main class=\"paging\">");
			sb.append("<div class=\"page-box con-min-witdh\">");
			sb.append("<div class=\"page con flex\">");
			if (i > 1) {
				if ((i - 1) % pages == 0) {
					startPage = startPage + pages;
					endPage = startPage + pages - 1;
					if (endPage >= totalPages) {
						endPage = totalPages;
					}
				}
			}
			if (i > pages) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_free_list"
						+ ((int) Math.ceil((double) (((i - 1 - pages) / pages) * pages) + 10))
						+ ".html\">&lt;&lt; </a></div>");
			}
			if (i > 1) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_free_list" + (i - 1)
						+ ".html\">&lt; 이전</a></div>");
			}
			for (int k = startPage; k <= endPage; k++) {
				if (k == i) {
					sb.append("<div class=\"page-no selected\"><a class=\"flex\" href=\"article_free_list" + k
							+ ".html\">" + k + "</a></div>");
				} else {
					sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_free_list" + k + ".html\">" + k
							+ "</a></div>");
				}
			}
			if (i < totalPages) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_free_list" + (i + 1)
						+ ".html\">다음 &gt;</a></div>");
			}
			if (i - 1 / pages < (totalPages - (totalPages % pages) + 1)) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_free_list"
						+ ((int) Math.ceil((double) (((i - 1 + pages) / pages) * pages) + 1))
						+ ".html\"> &gt;&gt;</a></div>");
			}
			sb.append("</div>");
			sb.append("</div>");
			sb.append("</main>");
			sb.append(foot);
			articleStart = articleStart + 10;
			Util.writeFileContents("site/" + "article_free_list" + i + ".html", sb.toString());
			System.out.println("site/" + "article_free_list" + i + ".html" + "생성");

		}
	}

	// 공지 게시판 페이징
	private void articleListNotice() {
		String head = getHeadHtml();
		String foot = Util.getFileContents("site_template/part/foot.html");
		List<Article> articles = articleService.getForPrintArticles(1);

		totalPages = (int) Math.ceil((double) articles.size() / pages);
		startPage = 1;
		endPage = startPage + pages - 1;
		articleStart = 0;

		for (int i = 1; i <= totalPages; i++) {

			List<Article> articles_Paging = articleService.getArticlesByPaging(1, articleStart, pages);
			StringBuffer sb = new StringBuffer();
			sb.append(head);
			sb.append("<div class=\"title-bar con-min-width\">");
			sb.append("<h1 class=\"con\">");
			sb.append("<i class=\"fas fa-flag\"></i>");
			sb.append("<span>공지 게시판" + i + "페이지" + "</span>");
			sb.append("</h1>");
			sb.append("</div>");
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
				sb.append("<div class=\"flex\">");
				sb.append("<div class=article-list__cell-id>" + article.id + "</div>");
				sb.append("<div class=article-list__cell-reg-date>" + article.regDate + "</div>");
				sb.append("<div class=article-list__cell-writer>" + member.name + "</div>");
				sb.append("<div class=article-list__cell-rcm>" + article.rcmCount + "</div>");
				sb.append("<div class=article-list__cell-title><a href=\"notice" + article.id
						+ ".html\" class=hover-underline>" + article.title + "</a></div>");
				sb.append("</div>");
			}
			sb.append("</main>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("<main class=\"paging\">");
			sb.append("<div class=\"page-box con-min-witdh\">");
			sb.append("<div class=\"page con flex flex-end\">");
			if (i > 1) {
				if ((i - 1) % pages == 0) {
					startPage = startPage + pages;
					endPage = startPage + pages - 1;
					if (endPage >= totalPages) {
						endPage = totalPages;
					}
				}
			}
			if (i > pages) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list"
						+ ((int) Math.ceil((double) (((i - 1 - pages) / pages) * pages) + 10))
						+ ".html\">&lt;&lt; </a></div>");
			}
			if (i > 1) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_notice_list" + (i - 1)
						+ ".html\">&lt; 이전</a></div>");
			}
			for (int k = startPage; k <= endPage; k++) {
				if (k == i) {
					sb.append("<div class=\"page-no selected\"><a class=\"flex\" href=\"article_notice_list" + k
							+ ".html\">" + k + "</a></div>");
				} else {
					sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_notice_list" + k + ".html\">" + k
							+ "</a></div>");
				}
			}
			if (i < totalPages) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_notice_list" + (i + 1)
						+ ".html\">다음 &gt;</a></div>");
			}
			if (i - 1 / pages < (totalPages - (totalPages % pages) + 1)) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_notice_list"
						+ ((int) Math.ceil((double) (((i - 1 + pages) / pages) * pages) + 1))
						+ ".html\"> &gt;&gt;</a></div>");
			}
			sb.append("</div>");
			sb.append("</div>");
			sb.append("</main>");
			sb.append(foot);
			articleStart = articleStart + 10;
			Util.writeFileContents("site/" + "article_notice_list" + i + ".html", sb.toString());
			System.out.println("site/" + "article_notice_list" + i + ".html" + "생성");
		}
	}

	// 전체 게시판리스트
	private void articleListAll() {
		String head = getHeadHtml();
		String foot = Util.getFileContents("site_template/part/foot.html");

		List<Article> articles = articleService.getForPrintArticles();

		totalPages = (int) Math.ceil((double) articles.size() / pages);
		startPage = 1;
		endPage = startPage + pages - 1;

		for (int i = 1; i <= totalPages; i++) {
			List<Article> articles_Paging = articleService.getArticlesByPagingAll(articleStart, pages);
			StringBuffer sb = new StringBuffer();
			sb.append(head);
			sb.append("<div class=\"title-bar con-min-width\">");
			sb.append("<h1 class=\"con\">");
			sb.append("<i class=\"fas fa-flag\"></i>");
			sb.append("<span>전체 게시물 리스트</span>");
			sb.append("</h1>");
			sb.append("</div>");
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
				if ((i - 1) % pages == 0) {
					startPage = startPage + pages;
					endPage = startPage + pages - 1;
					if (endPage >= totalPages) {
						endPage = totalPages;
					}
				}
			}
			if (i > pages) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list"
						+ ((int) Math.ceil((double) (((i - 1 - pages) / pages) * pages) + 10))
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
			if (i - 1 / pages < (totalPages - (totalPages % pages) + 1)) {
				sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list"
						+ ((int) Math.ceil((double) (((i - 1 + pages) / pages) * pages) + 1))
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
		String head = getHeadHtml();
		String foot = Util.getFileContents("site_template/part/foot.html");

		for (Board board : boards) {
			List<Article> articles = articleService.getForPrintArticles(board.id);
			int i = 0;

			for (Article article : articles) {
				Member member = memberService.getMemberByMemberId(article.memberId);
				StringBuffer sb = new StringBuffer();
				sb.append(head);
				sb.append("<div class=\"title-bar con-min-width\">");
				sb.append("<h1 class=\"con\">");
				sb.append("<i class=\"fas fa-flag\"></i>");
				sb.append("<span>게시물 상세페이지</span>");
				sb.append("</h1>");
				sb.append("</div>");
				sb.append("<detail class=\"detail-box con-min-width\">");
				sb.append("<div class=\"con flex flex-dr-c\">");
				sb.append("<subject class=\"title\">");
				sb.append("<ul class=\"flex flex-jc-c\">");
				sb.append("<li><a href=\"#\">" + article.title + "</a></li></ul></subject>");
				sb.append("<info class=\"information\">");
				sb.append("<ul class=\"flex\">");
				sb.append("<li class=\"flex-g-1\"><a href=\"#\">작성자: " + member.name + "</a></li>");
				sb.append("<li><a href=\"#\">조회수: " + article.hit + "</a></li>");
				sb.append("<li><a href=\"#\">작성일: " + article.regDate + "</a></li></ul></info>");
				sb.append("<contents class=\"contents flex-g-1\">");
				sb.append("<ul class=\"flex\">");
				sb.append("<il>" + article.body + "</il></ul></ul></contents>");
				sb.append("<buttom>");
				sb.append("<ul class=\"flex\">");
				sb.append("<li>추천수: " + article.rcmCount + "</li>");
				sb.append("<li class=\"flex-g-1\"></li>");
				if (i > 0) {
					sb.append("<li class=\"hover-underline\"><a href=\"" + board.code + (articles.get(i - 1).id)
							+ ".html\">&lt; 이전글</a></li>");
				}
				sb.append("<li class=\"hover-underline\"><a href=\"article_" + board.code + "_list"
						+ (int) Math.ceil((double) (i + 1) / pages) + ".html\">목록</a></li>");
				if (articles.size() > i + 1) {
					sb.append("<li class=\"hover-underline\"><a href=\"" + board.code + (articles.get(i + 1).id)
							+ ".html\"> 다음글 &gt;</a></li>");
				}
				sb.append("</ul></buttom></div></detail>");
				sb.append("");

				sb.append(foot);
				String fileName = board.code + article.id + ".html";
				Util.writeFileContents("site/" + fileName, sb.toString());

				System.out.println("site/" + fileName + "생성");
				i = i + 1;
			}
		}
	}

	// 메뉴바 자동생성
	private String getHeadHtml() {
		String head = Util.getFileContents("site_template/part/head.html");
		StringBuilder boardMenuContent = new StringBuilder();
		List<Board> forPrintBoard = articleService.getForPrintBoards();

		for (Board board : forPrintBoard) {
			boardMenuContent.append("<li>");

			String link = "article_" + board.code + "_list1.html";
			boardMenuContent.append("<a href=\"" + link + "\" class=\"flex flex-jc-c flex-ai-c \">");

			String icon = "<i class=\"fab fa-free-code-camp\"></i>";
			if (board.code.contains("notice")) {
				icon = "<i class=\"far fa-clipboard\"></i>";
			} else if (board.code.contains("free")) {
				icon = "<i class=\"fas fa-comment-medical\"></i>";
			}
			boardMenuContent.append(icon);
			boardMenuContent.append("<span>" + board.code + "</span>");
			boardMenuContent.append("</a></li>");
		}
		head = head.replace("[manu-bar-add]", boardMenuContent.toString());
		return head;
	}

}
