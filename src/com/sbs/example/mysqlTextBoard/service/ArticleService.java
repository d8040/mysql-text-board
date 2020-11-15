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

	public Article getArticles(int id) {
		return articleDao.getArticles(id);
	}

	public void delete(int id) {
		articleDao.delete(id);
	}

	public void modify(int memberId, int inputedId, String title, String body) {
		articleDao.modify(memberId, inputedId, title, body);
		
	}

	public int add(int memberId, String title, String body) {
		return articleDao.add(memberId, title, body);
	}

}
