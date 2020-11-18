package com.sbs.example.mysqlTextBoard.dto;

import java.util.Map;

public class Reply {
	
	public Reply(Map<String, Object> replyMap) {
		this.id = (int) replyMap.get("id");
		this.regDate = (String) replyMap.get("regDate");
		this.updateDate = (String) replyMap.get("updateDate");
		this.body = (String) replyMap.get("body");
		this.memberId = (int) replyMap.get("memberId");
		this.articleId = (int) replyMap.get("articleId");
	}
	public int id;
	public String regDate;
	public String updateDate;
	public int memberId;
	public int articleId;
	public String body;

}
