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

	public void makeHtml() {
		List<Article> articles = articleService.getForPrintArticles();
		System.out.println(articles.size());
		Util.mkdirs("site/article");

		String head = getHeadHtml();
		String foot = Util.getFileContents("site_template/part/foot.html");

		for (Article article : articles) {
			Member member = memberService.getMemberByMemberId(article.memberId);
			StringBuffer sb = new StringBuffer();
			String fileName = article.id + ".html";
			sb.append(head);
			sb.append("<div class=\"title-bar con-min-width\">");
			sb.append("<h1 class=\"con\">");
			sb.append("<i class=\"fas fa-flag\"></i>");
			sb.append("<span>게시물 상세페이지</span>");
			sb.append("</h1>");
			sb.append("</div>");
			sb.append("<section class=\"section-1 con-min-width\">");
			sb.append("<div class=\"con\">");
			sb.append("<div class=\"article-detail\">");
			sb.append("<div>번호: " + article.id + "</div>");
			sb.append("<div>날짜: " + article.regDate + "</div>");
			sb.append("<div>작성자: " + member.name + "</div>");
			sb.append("<div>제목: " + article.title + "</div>");
			sb.append("<div>내용: " + article.body + "</div>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("</section>");

			if (article.id > 1) {
				sb.append("<div><a href=\"" + (article.id - 1) + ".html\">이전글</a></div>");
			}
			if (articles.size() >= article.id) {
				sb.append("<div><a href=\"" + (article.id + 1) + ".html\">다음글</a></div>");
			}
			sb.append(foot);

			Util.writeFileContents("site/article/" + fileName, sb.toString());

			System.out.println("site/article/" + fileName + "생성");
		}
		// 전체 게시판리스트
		
		List<Article> articles_listArticle = articleService.getForPrintArticles();
		int start = 0;
		int page = 10;
		for (int i = 1; i <= (articles_listArticle.size() / 10) + 1; i++) {
			List<Article> articles_Paging = articleService.getArticlesByPagingAll(start, page);
		StringBuffer sb = new StringBuffer();
		sb.append(head);
		sb.append("<div class=\"title-bar con-min-width\">");
		sb.append("<h1 class=\"con\">");
		sb.append("<i class=\"fas fa-flag\"></i>");
		sb.append("<span>전체 게시물 리스트</span>");
		sb.append("</h1>");
		sb.append("</div>");
		sb.append("<section class=\"section-1 con-min-width\">");
		sb.append("<div class=\"con\">");
		sb.append("<div class=\"article-list\">");
		sb.append("<header><div>");
		sb.append("<div class=\"article-list__cell-id\">번호</div>");
		sb.append("<div class=\"article-list__cell-reg-date\">날짜</div>");
		sb.append("<div class=\"article-list__cell-writer\">작성자</div>");
		sb.append("<div class=\"article-list__cell-rcm\">추천수</div>");
		sb.append("<div class=\"article-list__cell-title\">제목</div>");
		sb.append("</div></header>");
		sb.append("<main class=\"article-box\">");
		for (Article article : articles_Paging) {
			Member member = memberService.getMemberByMemberId(article.memberId);
			sb.append("<div>");
			sb.append("<div class=article-list__cell-id>" + article.id + "</div>");
			sb.append("<div class=article-list__cell-reg-date>" + article.regDate + "</div>");
			sb.append("<div class=article-list__cell-writer>" + member.name + "</div>");
			sb.append("<div class=article-list__cell-rcm>" + article.rcmCount + "</div>");
			sb.append("<div class=article-list__cell-title><a href=" + article.id + ".html class=hover-underline>"
					+ article.title + "</a></div>");
			sb.append("</div>");
		}
		sb.append("</main></div></div>");
		sb.append("<main class=\"paging\">");
		sb.append("<div class=\"page-box con-min-witdh\">");
		sb.append("<div class=\"page con flex flex-end\">");
		for (int k = 1; k <= (articles_listArticle.size() / 10) + 1; k++) {
			sb.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_list" + k
					+ ".html\">" + k + "</a></div>");
		}
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</main>");
		sb.append(foot);
		start = start + 10;
		Util.writeFileContents("site/article/" + "article_list" + i + ".html", sb.toString());
		System.out.println("site/article/" + "article_list" + i + ".html" + "생성");
		}
		start = 0;
		page = 10;
		// 공지 게시판 페이징
		List<Article> articles_noticeListarticle = articleService.getForPrintArticles(1);
		for (int i = 1; i <= (articles_noticeListarticle.size() / 10) + 1; i++) {

			List<Article> articles_Paging = articleService.getArticlesByPaging(1, start, page);
			StringBuffer sb_noticeList = new StringBuffer();
			sb_noticeList.append(head);
			sb_noticeList.append("<div class=\"title-bar con-min-width\">");
			sb_noticeList.append("<h1 class=\"con\">");
			sb_noticeList.append("<i class=\"fas fa-flag\"></i>");
			sb_noticeList.append("<span>공지 게시판"+i+"페이지" +"</span>");
			sb_noticeList.append("</h1>");
			sb_noticeList.append("</div>");
			sb_noticeList.append("<section class=\"section-1 con-min-width\">");
			sb_noticeList.append("<div class=\"con\">");
			sb_noticeList.append("<div class=\"article-list\">");
			sb_noticeList.append("<header><div>");
			sb_noticeList.append("<div class=\"article-list__cell-id\">번호</div>");
			sb_noticeList.append("<div class=\"article-list__cell-reg-date\">날짜</div>");
			sb_noticeList.append("<div class=\"article-list__cell-writer\">작성자</div>");
			sb_noticeList.append("<div class=\"article-list__cell-rcm\">추천수</div>");
			sb_noticeList.append("<div class=\"article-list__cell-title\">제목</div>");
			sb_noticeList.append("</div></header>");
			sb_noticeList.append("<main class=\"article-box\">");
			for (Article article : articles_Paging) {
				Member member = memberService.getMemberByMemberId(article.memberId);
				sb_noticeList.append("<div>");
				sb_noticeList.append("<div class=article-list__cell-id>" + article.id + "</div>");
				sb_noticeList.append("<div class=article-list__cell-reg-date>" + article.regDate + "</div>");
				sb_noticeList.append("<div class=article-list__cell-writer>" + member.name + "</div>");
				sb_noticeList.append("<div class=article-list__cell-rcm>" + article.rcmCount + "</div>");
				sb_noticeList.append("<div class=article-list__cell-title><a href=" + article.id
						+ ".html class=hover-underline>" + article.title + "</a></div>");
				sb_noticeList.append("</div>");
			}
			sb_noticeList.append("</main>");
			sb_noticeList.append("</div>");
			sb_noticeList.append("</div>");
			sb_noticeList.append("<main class=\"paging\">");
			sb_noticeList.append("<div class=\"page-box con-min-witdh\">");
			sb_noticeList.append("<div class=\"page con flex flex-end\">");
			for (int k = 1; k <= (articles_noticeListarticle.size() / 10) + 1; k++) {
				sb_noticeList.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_notice_list" + k
						+ ".html\">" + k + "</a></div>");
			}
			sb_noticeList.append("</div>");
			sb_noticeList.append("</div>");
			sb_noticeList.append("</main>");
			sb_noticeList.append(foot);
			start = start + 10;
			Util.writeFileContents("site/article/" + "article_notice_list" + i + ".html", sb_noticeList.toString());
			System.out.println("site/article/" + "article_notice_list" + i + ".html" + "생성");
		}

		// 자유 게시판 페이징
		List<Article> articles_freeList = articleService.getForPrintArticles(2);
		start = 0;
		page = 10;
		for (int i = 1; i <= (articles_freeList.size() / 10) + 1; i++) {
			List<Article> articles_Paging = articleService.getArticlesByPaging(2, start, page);
			StringBuffer sb_freeList = new StringBuffer();
			sb_freeList.append(head);
			sb_freeList.append("<div class=\"title-bar con-min-width\">");
			sb_freeList.append("<h1 class=\"con\">");
			sb_freeList.append("<i class=\"fas fa-flag\"></i>");
			sb_freeList.append("<span>자유 게시판"+i+"페이지" +"</span>");
			sb_freeList.append("</h1>");
			sb_freeList.append("</div>");
			sb_freeList.append("<section class=\"section-1 con-min-width\">");
			sb_freeList.append("<div class=\"con\">");
			sb_freeList.append("<div class=\"article-list\">");
			sb_freeList.append("<header><div>");
			sb_freeList.append("<div class=\"article-list__cell-id\">번호</div>");
			sb_freeList.append("<div class=\"article-list__cell-reg-date\">날짜</div>");
			sb_freeList.append("<div class=\"article-list__cell-writer\">작성자</div>");
			sb_freeList.append("<div class=\"article-list__cell-rcm\">추천수</div>");
			sb_freeList.append("<div class=\"article-list__cell-title\">제목</div>");
			sb_freeList.append("</div></header>");
			sb_freeList.append("<main class=\"article-box\">");
			for (Article article : articles_Paging) {
				Member member = memberService.getMemberByMemberId(article.memberId);
				sb_freeList.append("<div>");
				sb_freeList.append("<div class=article-list__cell-id>" + article.id + "</div>");
				sb_freeList.append("<div class=article-list__cell-reg-date>" + article.regDate + "</div>");
				sb_freeList.append("<div class=article-list__cell-writer>" + member.name + "</div>");
				sb_freeList.append("<div class=article-list__cell-rcm>" + article.rcmCount + "</div>");
				sb_freeList.append("<div class=article-list__cell-title><a href=" + article.id
						+ ".html class=hover-underline>" + article.title + "</a></div>");
				sb_freeList.append("</div>");
			}
			sb_freeList.append("</main>");
			sb_freeList.append("</div>");
			sb_freeList.append("</div>");
			sb_freeList.append("<main class=\"paging\">");
			sb_freeList.append("<div class=\"page-box con-min-witdh\">");
			sb_freeList.append("<div class=\"page con flex\">");
			for (int k = 1; k <= (articles_freeList.size() / 10) + 1; k++) {
				sb_freeList.append("<div class=\"page-no\"><a class=\"flex\" href=\"article_free_list" + k
						+ ".html\">" + k + "</a></div>");
			}
			sb_freeList.append("</div>");
			sb_freeList.append("</div>");
			sb_freeList.append("</main>");
			sb_freeList.append(foot);
			Util.writeFileContents("site/article/" + "article_free_list" + i + ".html", sb_freeList.toString());
			System.out.println("site/article/" + "article_free_list" + i + ".html" + "생성");
			start = start + 10;
		}
		// 통계
		StringBuffer sb_statastics = new StringBuffer();
//		List<Article> articles_statastics = articleService.getForPrintArticles();
		List<Member> members = memberService.getForPrintMembers();
		sb_statastics.append(head);
		sb_statastics.append("<div class=\"title-bar con-min-width\">");
		sb_statastics.append("<h1 class=\"con\">");
		sb_statastics.append("<i class=\"fas fa-flag\"></i>");
		sb_statastics.append("<span>통계</span>");
		sb_statastics.append("</h1>");
		sb_statastics.append("</div>");
		sb_statastics.append("<section class=\"section-1 con-min-width\">");
		sb_statastics.append("<main>");
		sb_statastics.append("<div class=satastics>");
		sb_statastics.append("<div class=satastics__member>" + "회원수" + members.size() + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "전체 게시물 수" + articles.size() + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "각 게시판별 게시물 수" + articles.size() + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "전체 게시물 조회 수" + articles.size() + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "각 게시판별 게시물 조회 수" + articles.size() + "</div>");
		sb_statastics.append("</div>");
		sb_statastics.append("</main>");
		sb_statastics.append(foot);
		Util.writeFileContents("site/article/" + "index.html", sb_statastics.toString());
		System.out.println("site/article/" + "index.html" + "생성");
		
	}
	//메뉴바 자동생성
	private String getHeadHtml() {
		String head = Util.getFileContents("site_template/part/head.html");
		StringBuilder boardMenuContent = new StringBuilder();
		List<Board> forPrintBoard = articleService.getForPrintBoards();
		
		for (Board board : forPrintBoard) {
			boardMenuContent.append("<li>");
			
			String link = "article_"+board.code + "_list1.html";
			boardMenuContent.append("<a href=\""+link+"\" class=\"flex flex-jc-c flex-ai-c \">");
			
			String icon = "<i class=\"fab fa-free-code-camp\"></i>";
			if (board.code.contains("notice")) {
				icon = "<i class=\"far fa-clipboard\"></i>";
			}else if (board.code.contains("free")) {
				icon = "<i class=\"fas fa-comment-medical\"></i>";
			}
			boardMenuContent.append(icon);
			boardMenuContent.append("<span>"+board.code+"</span>");
			boardMenuContent.append("</a></li>");
		}
		head = head.replace("[manu-bar-add]", boardMenuContent.toString());
		return head;
	}
}
