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
			dologout(cmd);
		} else if (cmd.equals("member whoami")) {
			showWhoami(cmd);
		}

	}

	private void showWhoami(String cmd) {
		System.out.println("== 로그인 정보 ==");
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		int loginedId = Container.session.loginedMemberId;
		Member member = memberService.getMemberByMemberId(loginedId);

		System.out.println("번호: " + member.id);
		System.out.println("가입 일자: " + member.regDate);
		System.out.println("수정 일자: " + member.updateDate);
		System.out.println("로그인 아이디: " + member.loginId);
		System.out.println("사용자 이름: " + member.name);
		System.out.println("회원 종류: " + member.getType());

	}

	private void dologout(String cmd) {
		System.out.println("== 로그아웃 ==");
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		Container.session.logout();
	}

	private void doLogin(String cmd) {
		System.out.println("== 로그인 ==");

		if (Container.session.isLogined()) {
			System.out.println("로그아웃 이후 이용해주세요!");
			return;
		}

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
		System.out.printf("로그인 성공, %s 님 반갑습니다.\n", member.name);

		Container.session.loginedMemberId = member.id;
	}

	private void doJoin(String cmd) {
		System.out.println("== 회원 가입 ==");

		if (Container.session.isLogined()) {
			System.out.println("로그아웃 이후 이용해주세요!");
			return;
		}

		System.out.printf("아이디 : ");
		String loginId = Container.scanner.nextLine().trim();

		Member member = memberService.getMemberByLoginId(loginId);
		if(member != null) {
			if (member.loginId.equals(loginId)) {
				System.out.println("이미 존재하는 아이디입니다.");
				return;
			}	
		}
		
		System.out.printf("비밀번호 : ");
		String loginPw = Container.scanner.nextLine().trim();
		System.out.printf("사용자이름 : ");
		String name = Container.scanner.nextLine().trim();

		int id = memberService.join(loginId, loginPw, name);

		System.out.println(id + "번 회원가입이 완료되었습니다.");

	}
}
