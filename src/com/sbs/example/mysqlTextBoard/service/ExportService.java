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
		
		Util.mkdirs("site/article");
		
		
		for (Article article : articles) {
			Member member = memberService.getMemberByMemberId(article.memberId);
			StringBuffer sb = new StringBuffer();
			
			String fileName = article.id + ".html";
			sb.append("<!DOCTYPE html>");
			sb.append("<html lang=\"ko\">");
			
			sb.append("<head>");
			sb.append("<meta charset=\"UTF-8\">");
			sb.append("<meta name=\"viewport\" content=\"width=device-width, inital-scale=1\">");
			sb.append("<title>게시물 상세페이지 - "+article.title + "</title>");
			sb.append("</head>");
			
			sb.append("<body>");
			sb.append("<h1>게시물 상세페이지</h1>");
			sb.append("<div>번호: " + article.id +"</div>");
			sb.append("<div>날짜: " + article.regDate +"</div>");
			sb.append("<div>작성자: " + member.name +"</div>");
			sb.append("<div>제목: " + article.title +"</div>");
			sb.append("<div>내용: " + article.body +"</div>");
			
			if (article.id >1) {
				sb.append("<div><a href=\"" + (article.id -1) + ".html\">이전글</a></div>");
			}
			sb.append("<div><a href=\"" + (article.id +1) + ".html\">다음글</a></div>");
			
			sb.append("</body>");
			
			sb.append("</html>");			
			
			Util.writeFileContents("site/article/"+fileName, sb.toString());
			
			System.out.println("site/article/"+fileName+"생성");
		}
	}

}
