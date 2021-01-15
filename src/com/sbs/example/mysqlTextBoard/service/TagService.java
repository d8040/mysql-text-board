package com.sbs.example.mysqlTextBoard.service;

import java.util.List;

import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.dao.TagDao;
import com.sbs.example.mysqlTextBoard.dto.Tag;

public class TagService {
	private TagDao tagDao;
	
	public TagService() {
		tagDao = Container.tagDao;
	}

	public List<Tag> getDedupTagsByRelTypeCode(String relTypeCode) {
		return tagDao.getDedupTagsByRelTypeCode(relTypeCode);
	}

	public List<String> getDeudpTagBodiesByRelTypecode(String relTypeCode) {		
		return tagDao.getDeudpTagBodiesByRelTypecode(relTypeCode);
	}

}
