package com.sbs.example.mysqlTextBoard.controller;

import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.service.MemberService;

public class MemberController {

	private MemberService memberService;
	
	public MemberController() {
		memberService = Container.memberService;
	}

	public void doCommand(String cmd) {

		if (cmd.equals("member join")) {
			doJoin(cmd);
		}

	}

	private void doJoin(String cmd) {
		System.out.println("== 회원 가입 ==");

		System.out.printf("아이디 : ");
		String loginId = Container.scanner.nextLine();
		System.out.printf("비밀번호 : ");
		String loginPw = Container.scanner.nextLine();
		System.out.printf("사용자이름 : ");
		String name = Container.scanner.nextLine();

		int id = memberService.join(loginId, loginPw, name);

		System.out.println(id + "번 회원가입이 완료되었습니다.");
		Container.session.loginedMemberId = id;
	}

}
