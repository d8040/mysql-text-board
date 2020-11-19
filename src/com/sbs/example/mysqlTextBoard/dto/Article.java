package com.sbs.example.mysqlTextBoard.dto;

import java.util.Map;

public class Article {

	public int id;
	public String title;
	public String body;
	public int memberId;
	public int boardId;
	public String regDate;
	public String updateDate;
	public String extra_writer;
	public int rcmCount;
	


	public Article(Map<String, Object> articleMap) {
		this.id = (int) articleMap.get("id");
		this.regDate = (String) articleMap.get("regDate");
		this.updateDate = (String) articleMap.get("updateDate");
		this.title = (String) articleMap.get("title");
		this.body = (String) articleMap.get("body");
		this.memberId = (int) articleMap.get("memberId");
		this.boardId = (int) articleMap.get("boardId");
		this.rcmCount = (int) articleMap.get("rcmCount");
		if(articleMap.containsKey("extra_writer")) {
			this.extra_writer = (String)articleMap.get("extra_writer");
		}
	}



	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", body=" + body + ", memberId=" + memberId + ", boardId="
				+ boardId + ", regDate=" + regDate + ", updateDate=" + updateDate + ", extra_writer=" + extra_writer
				+ "]";
	}
}
