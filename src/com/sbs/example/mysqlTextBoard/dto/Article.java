package com.sbs.example.mysqlTextBoard.dto;

public class Article {
//	public Article(int id, String title, String body, int memberId, int boardId, String regDate, String updateDate) {
//		this.id = id;
//		this.title = title;
//		this.body = body;
//		this.memberId = memberId;
//		this.boardId = boardId;
//		this.regDate = regDate;
//		this.updateDate = updateDate;
//	}

	public int id;
	public String title;
	public String body;
	public int memberId;
	public int boardId;
	public String regDate;
	public String updateDate;
	

	@Override
	public String toString() {
		return "Article [id=" + id + ", regDate=" + regDate + ", updateDate=" + updateDate + ", title=" + title
				+ ", body=" + body + ", memberId=" + memberId + ", boardId=" + boardId + "]";
	}

}
