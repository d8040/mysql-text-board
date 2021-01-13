package com.sbs.example.mysqlTextBoard.dto;

import java.util.Map;

import lombok.Data;

@Data
public class Article {

	private int id;
	private String title;
	private String body;
	private int memberId;
	private int boardId;
	private String regDate;
	private String updateDate;
	private String extra_writer;
	private String extra_boardName;
	private String extra_boardCode;
	private int rcmCount;
	private int hit;
	private int commentsCount;
	


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
