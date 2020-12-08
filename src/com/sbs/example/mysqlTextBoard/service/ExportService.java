package com.sbs.example.mysqlTextBoard.service;

import java.util.List;

import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.dto.Article;
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

		Util.mkdirs("site/article");

		String head = Util.getFileContents("site_template/part/head.html");
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
			sb.append("<div><a href=\"" + (article.id + 1) + ".html\">다음글</a></div>");
			sb.append(foot);

			Util.writeFileContents("site/article/" + fileName, sb.toString());

			System.out.println("site/article/" + fileName + "생성");
		}
		// 전체 게시판리스트
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
		sb.append("<main>");
		for (Article article : articles) {
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
		sb.append("</main>");
		sb.append(foot);
		Util.writeFileContents("site/article/" + "article_list.html", sb.toString());
		System.out.println("site/article/" + "article_list.html" + "생성");

		// 공지 게시판 리스트
		StringBuffer sb_noticeList = new StringBuffer();
		List<Article> articles_noticeList = articleService.getForPrintArticles(1);
		sb_noticeList.append(head);
		sb_noticeList.append("<div class=\"title-bar con-min-width\">");
		sb_noticeList.append("<h1 class=\"con\">");
		sb_noticeList.append("<i class=\"fas fa-flag\"></i>");
		sb_noticeList.append("<span>공지 게시판</span>");
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
		sb_noticeList.append("<main>");
		for (Article article : articles_noticeList) {
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
		sb_noticeList.append(foot);
		Util.writeFileContents("site/article/" + "article_notice_list.html", sb_noticeList.toString());
		System.out.println("site/article/" + "article_notice_list.html" + "생성");

		// 자유 게시판 리스트
		StringBuffer sb_freeList = new StringBuffer();
		List<Article> articles_freeList = articleService.getForPrintArticles(2);
		sb_freeList.append(head);
		sb_freeList.append("<div class=\"title-bar con-min-width\">");
		sb_freeList.append("<h1 class=\"con\">");
		sb_freeList.append("<i class=\"fas fa-flag\"></i>");
		sb_freeList.append("<span>자유 게시판</span>");
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
		sb_freeList.append("<main>");
		for (Article article : articles_freeList) {
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
		sb_freeList.append(foot);
		Util.writeFileContents("site/article/" + "article_free_list.html", sb_freeList.toString());
		System.out.println("site/article/" + "article_free_list.html" + "생성");

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
		sb_statastics.append("<div class=satastics__member>" + "전체 게시물 수" +articles.size() + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "각 게시판별 게시물 수" + articles.size() + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "전체 게시물 조회 수" + articles.size() + "</div>");
		sb_statastics.append("<div class=satastics__member>" + "각 게시판별 게시물 조회 수" + articles.size() + "</div>");
		sb_statastics.append("</div>");
		sb_statastics.append("</main>");
		sb_statastics.append(foot);
		Util.writeFileContents("site/article/" + "index.html", sb_statastics.toString());
		System.out.println("site/article/" + "index.html" + "생성");
	}

}
