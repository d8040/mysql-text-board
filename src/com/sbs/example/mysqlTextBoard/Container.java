package com.sbs.example.mysqlTextBoard;

import java.util.Scanner;

import com.sbs.example.mysqlTextBoard.controller.MemberController;
import com.sbs.example.mysqlTextBoard.dao.MemberDao;
import com.sbs.example.mysqlTextBoard.service.MemberService;
import com.sbs.example.mysqlTextBoard.session.Session;

public class Container {

	public static Scanner scanner;
	
	public static MemberController memberController;
	public static Session session;
	public static MemberService memberService;
	public static MemberDao memberDao;
	
	static {
		scanner = new Scanner(System.in);
		
		session = new Session();
		memberDao = new MemberDao();
		
		memberService = new MemberService();
		memberController = new MemberController();
	}

}
