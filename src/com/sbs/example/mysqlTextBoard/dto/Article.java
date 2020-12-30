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
	public String extra_boardName;
	public String extra_boardCode;
	public int rcmCount;
	public int hit;
	public int commentsCount;
	


	public Article(Map<String, Object> articleMap) {
		this.id = (int) articleMap.get("id");
		this.regDate = (String) articleMap.get("regDate");
		this.updateDate = (String) articleMap.get("updateDate");
		this.title = (String) articleMap.get("title");
		this.body = (String) articleMap.get("body");
		this.memberId = (int) articleMap.get("memberId");
		this.boardId = (int) articleMap.get("boardId");
		this.rcmCount = (int) articleMap.get("rcmCount");
		this.hit = (int) articleMap.get("hit");
		this.commentsCount = (int) articleMap.get("commentsCount");
		if(articleMap.containsKey("extra_writer")) {
			this.extra_writer = (String)articleMap.get("extra_writer");
		}
		if(articleMap.containsKey("extra_boardName")) {
			this.extra_boardName = (String)articleMap.get("extra_boardName");
		}
		if(articleMap.containsKey("extra_boardCode")) {
			this.extra_boardCode = (String)articleMap.get("extra_boardCode");
		}
	}



	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", body=" + body + ", memberId=" + memberId + ", boardId="
				+ boardId + ", regDate=" + regDate + ", updateDate=" + updateDate + ", extra_writer=" + extra_writer
				+ ", extra_boardName=" + extra_boardName + ", extra_boardCode=" + extra_boardCode + ", rcmCount="
				+ rcmCount + ", hit=" + hit + ", commentsCount=" + commentsCount + "]";
	}

}
