package com.sbs.example.mysqlTextBoard.controller;

import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.service.ExportService;

public class ExportController {

	private ExportService exportService;

	public ExportController() {
		exportService = Container.exportService;
	}

	public void doCommand(String cmd) {
		if (cmd.equals("build site")) {
			doBuild(cmd);
		}

	}

	private void doBuild(String cmd) {
		System.out.println("== html 생성을 시작합니다.==");
		exportService.makeHtml();
	}
}