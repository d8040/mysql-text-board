package com.sbs.example.mysqlTextBoard.controller;

import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.dto.Member;
import com.sbs.example.mysqlTextBoard.service.MemberService;

public class MemberController {

	private MemberService memberService;

	public MemberController() {
		memberService = Container.memberService;
	}

	public void doCommand(String cmd) {

		if (cmd.equals("member join")) {
			doJoin(cmd);
		} else if (cmd.equals("member login")) {
			doLogin(cmd);
		} else if (cmd.equals("member logout")) {
			logout(cmd);
		}

	}

	private void logout(String cmd) {
		System.out.println("== 로그인 ==");
		
		Container.session.logout();
	}

	private void doLogin(String cmd) {
		System.out.println("== 로그인 ==");

		System.out.printf("아이디 : ");
		String loginId = Container.scanner.nextLine();

		Member member = memberService.getMemberByLoginId(loginId);
		if (member == null) {
			System.out.println("아이디가 존재하지 않습니다.");
			return;
		}

		System.out.printf("비밀번호 : ");
		String loginPw = Container.scanner.nextLine();
		if (loginPw.equals(member.loginPw) == false) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return;
		}
		memberService.login(loginId, loginPw);

		System.out.printf("로그인 성공, %s 님 반갑습니다.\n", member.name);

		Container.session.loginedMemberId = member.id;
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
	}

}
