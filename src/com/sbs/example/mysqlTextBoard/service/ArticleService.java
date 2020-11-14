package com.sbs.example.mysqlTextBoard.service;

import java.util.List;

import com.sbs.example.mysqlTextBoard.dao.ArticleDao;
import com.sbs.example.mysqlTextBoard.dto.Article;

public class ArticleService {
	private ArticleDao articleDao;

	public ArticleService() {
		articleDao = new ArticleDao();
	}

	public List<Article> getArticles() {
		return articleDao.getArticles();
	}

	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

	public int delete(int id) {
		return articleDao.delete(id);
	}

	public void modify(int inputedId, String title, String body) {
		articleDao.modify(inputedId, title, body);
		
	}

	public int write(int memberId, String title, String body) {
		return articleDao.write(memberId, title, body);
	}

}
