package com.sbs.example.mysqlTextBoard.service;

import java.io.File;
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
		File HtmlFolder = new File("site");

		if ( HtmlFolder.exists() == false ) {
			HtmlFolder.mkdir();
		}
		
		for (Article article : articles) {
			Member member = memberService.getMemberByMemberId(article.memberId);
			
			String fileName = article.id + ".html";
			String html = "<meta charset=\"UTF-8\">";
			html += "<div>번호: " + article.id +"</div>";
			html += "<div>날짜: " + article.regDate +"</div>";
			html += "<div>작성자: " + member.name +"</div>";
			html += "<div>제목: " + article.title +"</div>";
			html += "<div>내용: " + article.body +"</div>";
			if (article.id >1) {
				html += "<div><a href=\"" + (article.id -1) + ".html\">이전글</a></div>";
			}
			html += "<div><a href=\"" + (article.id +1) + ".html\">다음글</a></div>";
			
			Util.writeFileContents("site/"+fileName, html);
		}
	}

}
