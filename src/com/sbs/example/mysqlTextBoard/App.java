package com.sbs.example.mysqlTextBoard;

import java.util.Scanner;

import com.sbs.example.mysqlTextBoard.controller.ArticleController;
import com.sbs.example.mysqlTextBoard.controller.MemberController;

public class App {
	private MemberController memberController;
	
	public App() {
		memberController = Container.memberController;
	}

	public void run() {
		Scanner sc = Container.scanner;

		ArticleController articleController = new ArticleController();

		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine();

			if (cmd.startsWith("article ")) {
				articleController.doCommand(cmd);
			} else if (cmd.startsWith("member ")) {
				memberController.doCommand(cmd);
			} 
			
			else if (cmd.equals("system exit")) {
				System.out.println("== 시스템 종료 ==");
				break;
			}
		}
	}
}
