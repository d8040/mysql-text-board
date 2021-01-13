package com.sbs.example.mysqlTextBoard.dto;

import java.util.Map;

import lombok.Data;

@Data
public class Reply {

	private int id;
	private String regDate;
	private String updateDate;
	private int memberId;
	private int articleId;
	private String body;

	public Reply(Map<String, Object> replyMap) {
		this.id = (int) replyMap.get("id");
		this.regDate = (String) replyMap.get("regDate");
		this.updateDate = (String) replyMap.get("updateDate");
		this.body = (String) replyMap.get("body");
		this.memberId = (int) replyMap.get("memberId");
		this.articleId = (int) replyMap.get("articleId");
	}
}
