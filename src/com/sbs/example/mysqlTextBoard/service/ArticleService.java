package com.sbs.example.mysqlTextBoard.service;

import java.util.List;

import com.sbs.example.mysqlTextBoard.dao.ArticleDao;
import com.sbs.example.mysqlTextBoard.dto.Article;
import com.sbs.example.mysqlTextBoard.dto.Board;

public class ArticleService {
	private ArticleDao articleDao;

	public ArticleService() {
		articleDao = new ArticleDao();
	}

	public List<Article> getArticles(int boardId) {
		return articleDao.getArticles(boardId);
	}

	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

	public void delete(int id) {
		articleDao.delete(id);
	}

	public void modify(int memberId, int inputedId, String title, String body) {
		articleDao.modify(memberId, inputedId, title, body);
		
	}

	public int add(int boardId, int memberId, String title, String body) {
		return articleDao.add(boardId, memberId, title, body);
	}

	public int makeBoard(int loginedId, String name) {
		return articleDao.makeBoard(loginedId, name);
	}

	public Board getBoardByName(String inputName) {
		return articleDao.getBoardByName(inputName);
	}

}
