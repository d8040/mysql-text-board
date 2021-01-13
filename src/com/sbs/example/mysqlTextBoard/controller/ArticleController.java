package com.sbs.example.mysqlTextBoard.controller;

import java.util.List;

import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.dto.Article;
import com.sbs.example.mysqlTextBoard.dto.Board;
import com.sbs.example.mysqlTextBoard.dto.Member;
import com.sbs.example.mysqlTextBoard.dto.Reply;
import com.sbs.example.mysqlTextBoard.service.ArticleService;
import com.sbs.example.mysqlTextBoard.service.MemberService;

public class ArticleController {
	private ArticleService articleService;
	private MemberService memberService;

	public ArticleController() {
		articleService = Container.articleService;
		memberService = Container.memberService;
	}

	public void doCommand(String cmd) {
		if (cmd.equals("article list")) {
			showList(cmd);
		} else if (cmd.startsWith("article detail ")) {
			showDetail(cmd);
		} else if (cmd.startsWith("article delete ")) {
			doDelete(cmd);
		} else if (cmd.startsWith("article modify ")) {
			doModify(cmd);
		} else if (cmd.equals("article add")) {
			doWrite(cmd);
		} else if (cmd.equals("article makeBoard")) {
			doMakeBoard(cmd);
		} else if (cmd.equals("article selectBoard")) {
			doSelectBoard(cmd);
		} else if (cmd.startsWith("article addReply ")) {
			doAddReply(cmd);
		} else if (cmd.startsWith("article modifyReply ")) {
			doModifyReply(cmd);
		} else if (cmd.startsWith("article delReply ")) {
			doDelReply(cmd);
		} else if (cmd.startsWith("article recommend ")) {
			doRecommend(cmd);
		} else if (cmd.startsWith("article canlclerecommend ")) {
			doCancleRcmd(cmd);
		} else if (cmd.startsWith("article list ")) {
			showListPaging(cmd);
		}
		else if (cmd.startsWith("article 20")) {
			do20(cmd);
		}
	}

	private void showListPaging(String cmd) {
		int inputId = Integer.parseInt(cmd.split(" ")[2]);
		if (inputId <= 1) {
			inputId = 1;
		}

		String boardCode = Container.session.getCurrentBoardCode();
		Board board = articleService.getBoardByCode(boardCode);
		System.out.printf("== %s 게시판 리스트 ==\n", board.getName());
		List<Article> articles = articleService.getForPrintArticles(board.getId());
		int page = 10;
		int start = articles.size() - 1;
		start -= (inputId - 1) * page;
		int end = start - (page - 1);

		if (end < 0) {
			end = 0;
		}
		if (start < 0 ) {
			System.out.println(inputId+"페이지는 존재하지 않습니다.");
			return;
		}

		System.out.println("번호 / 작성 / 수정 / 작성자 / 제목 / 추천수");

		for (int i = start; i >= end; i--) {			
			Article article = articleService.getForPrintArticle(i, board.getId());
			if (article == null ) {
				return;
			}
			Member member = memberService.getMemberByMemberId(article.getMemberId());
			System.out.printf("%d / %s / %s / %s / %s / %d\n", article.getId(), article.getRegDate(), article.getUpdateDate(),
					member.getName(), article.getTitle(), article.getRcmCount());
		}
	}
// 게시물 20개 자동 생성
	private void do20(String cmd) {
		System.out.println("== 게시물 등록 ==");

		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		int memberId = Container.session.loginedMemberId;
		Board board = articleService.getBoardByCode(Container.session.getCurrentBoardCode());
		
		for(int i = 1; i<=20;i++) {
			
			int id = articleService.add(board.getId(), memberId, "제목"+i, "내용"+i);
			
			System.out.println(id + "번 게시물이 생성되었습니다.");
		}
	}

	private void doCancleRcmd(String cmd) {
		System.out.println("== 게시물 추천취소 ==");
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		int inputId = Integer.parseInt(cmd.split(" ")[2]);

		int memberId = Container.session.loginedMemberId;
		Article article = articleService.getArticle(inputId);

		if (article.getMemberId() != memberId) {
			System.out.println("수정권한이 없습니다.");
			return;
		}

		int id = articleService.cancleRcmd(memberId, article.getId());

		System.out.println(id + "추천이 취소 되었습니다.");
	}

	private void doRecommend(String cmd) {
		System.out.println("== 게시물 추천 ==");
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		int inputId = Integer.parseInt(cmd.split(" ")[2]);

		int memberId = Container.session.loginedMemberId;
		Article article = articleService.getArticle(inputId);

		int id = articleService.recommand(memberId, article.getId());

		System.out.println(id + "추천 되었습니다.");
	}

	private void doDelReply(String cmd) {
		System.out.println("== 댓글 삭제 ==");
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		int inputedId = Integer.parseInt(cmd.split(" ")[2]);

		Reply reply = articleService.getReplyByArticleId(inputedId);
		if (reply == null) {
			System.out.println("존재하지 않는 게시물 입니다.");
			return;
		}

		if (reply.getMemberId() != Container.session.loginedMemberId) {
			System.out.println("권한이 없습니다.");
			return;
		}

		articleService.deleteReply(inputedId);
		System.out.printf("%d번 게시물을 삭제하였습니다.\n", inputedId);
	}

	private void doModifyReply(String cmd) {
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		int inputedId = Integer.parseInt(cmd.split(" ")[2]);

		Reply reply = articleService.getReplyByArticleId(inputedId);

		System.out.printf("== %d번 댓글 수정 ==\n", inputedId);

		if (reply == null) {
			System.out.println("존재하지 않는 댓글 입니다.");
			return;
		}

		int memberId = Container.session.loginedMemberId;

		if (reply.getMemberId() != memberId) {
			System.out.println("수정권한이 없습니다.");
			return;
		}

		System.out.printf("번호 : %d\n", reply.getId());
		System.out.printf("작성날짜 : %s\n", reply.getRegDate());
		System.out.printf("작성자 : %s\n", reply.getMemberId());

		System.out.printf("내용 : ");
		String body = Container.scanner.nextLine();

		articleService.replyModify(memberId, inputedId, body);

		System.out.println(inputedId + "번 게시물 수정이 완료되었습니다.");

	}

	private void doAddReply(String cmd) {
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		int inputedId = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(inputedId);
		System.out.printf("== %d번 게시물 댓글 등록 ==\n", inputedId);
		if (article == null) {
			System.out.println("존재하지 않는 게시물 입니다.");
			return;
		}

		Member member = memberService.getMemberByMemberId(article.getMemberId());

		System.out.printf("번호 : %d\n", article.getId());
		System.out.printf("작성날짜 : %s\n", article.getRegDate());
		System.out.printf("작성자 : %s\n", member.getName());
		System.out.printf("제목 : %s\n", article.getTitle());
		System.out.printf("내용 : %s\n", article.getBody());

		System.out.printf("댓글 : ");
		String reply = Container.scanner.nextLine();

		int memberId = Container.session.loginedMemberId;
		int id = articleService.reply(memberId, inputedId, reply);

		System.out.printf("%d번 게시물에 %d번 댓글이 입력되었습니다.\n", inputedId, id);
	}

	private void doSelectBoard(String cmd) {
		System.out.println("== 게시판 이동 ==");
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		System.out.println("= 게시판 목록 =");
		System.out.println("번호 / 생성날짜 / 코드 / 이름 / 게시물 수량");

		List<Board> boards = articleService.getForPrintBoards();

		for (Board board : boards) {
			int articlesCount = articleService.getArticlesCount(board.getId());
			System.out.printf("%d / %s / %s / %s / %d \n", board.getId(), board.getRegDate(), board.getCode(), board.getName(),
					articlesCount);
		}

		System.out.printf("게시판 코드: ");
		String inputCode = Container.scanner.nextLine().trim();

		Board board = articleService.getBoardByCode(inputCode);

		if (board == null) {
			System.out.println("코드를 잘 못 입력하였습니다.");
			return;
		}

		Container.session.setCurrentBoardCode(board.getCode());
		System.out.println(board.getName() + "로 이동되었습니다.");
	}

	private void doMakeBoard(String cmd) {
		System.out.println("== 게시판 등록 ==");

		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		Member member = memberService.getMemberByMemberId(Container.session.loginedMemberId);

		if (member.isAdmin() == false) {
			System.out.println("관리자만 생성할 수 있습니다.");
			return;
		}

		System.out.printf("게시판 이름 : ");
		String name = Container.scanner.nextLine().trim();

		if (articleService.isMakeBoardAvailableName(name) == false) {
			System.out.println("중복 게시판이 있습니다.");
			return;
		}

		System.out.printf("게시판 코드 : ");
		String code = Container.scanner.nextLine().trim();

		if (articleService.isMakeBoardAvailableCode(code) == false) {
			System.out.println("중복 게시판이 있습니다.");
			return;
		}

		int boardId = articleService.makeBoard(name, code);

		System.out.println(boardId + "번 게시판이 생성되었습니다.");
	}

	private void doWrite(String cmd) {
		System.out.println("== 게시물 등록 ==");

		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		System.out.printf("제목 : ");
		String title = Container.scanner.nextLine();
		System.out.printf("내용 : ");
		String body = Container.scanner.nextLine();

		int memberId = Container.session.loginedMemberId;
		Board board = articleService.getBoardByCode(Container.session.getCurrentBoardCode());

		int id = articleService.add(board.getId(), memberId, title, body);

		System.out.println(id + "번 게시물이 생성되었습니다.");

	}

	private void doModify(String cmd) {
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		int inputedId = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(inputedId);
		System.out.printf("== %d번 게시물 수정 ==\n", inputedId);
		if (article == null) {
			System.out.println("존재하지 않는 게시물 입니다.");
			return;
		}

		int memberId = Container.session.loginedMemberId;

		if (article.getMemberId() != memberId) {
			System.out.println("수정권한이 없습니다.");
			return;
		}

		System.out.printf("번호 : %d\n", article.getId());
		System.out.printf("작성날짜 : %s\n", article.getRegDate());
		System.out.printf("작성자 : %s\n", article.getMemberId());

		System.out.printf("제목 : ");
		String title = Container.scanner.nextLine();
		System.out.printf("내용 : ");
		String body = Container.scanner.nextLine();

		articleService.modify(inputedId, title, body);

		System.out.println(inputedId + "번 게시물 수정이 완료되었습니다.");

	}

	private void doDelete(String cmd) {
		System.out.println("== 게시물 삭제 ==");
		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		int inputedId = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(inputedId);
		if (article == null) {
			System.out.println("존재하지 않는 게시물 입니다.");
			return;
		}
		if (article.getMemberId() != Container.session.loginedMemberId) {
			System.out.println("권한이 없습니다.");
			return;
		}

		articleService.delete(inputedId);
		System.out.printf("%d번 게시물을 삭제하였습니다.\n", inputedId);
	}

	private void showList(String cmd) {

		String boardCode = Container.session.getCurrentBoardCode();
		Board board = articleService.getBoardByCode(boardCode);
		System.out.printf("== %s 게시판 리스트 ==\n", board.getName());
		List<Article> articles = articleService.getForPrintArticles(board.getId());

		System.out.println("번호 / 작성 / 수정 / 작성자 / 제목 / 추천수");

		for (Article article : articles) {
			Member member = memberService.getMemberByMemberId(article.getMemberId());
			System.out.printf("%d / %s / %s / %s / %s / %d\n", article.getId(), article.getRegDate(), article.getUpdateDate(),
					member.getName(), article.getTitle(), article.getRcmCount());
		}
	}

	private void showDetail(String cmd) {
		System.out.println("== 게시물 상세페이지 ==");
//		if (Container.session.isLogined() == false) {
//			System.out.println("로그인 후 이용해주세요.");
//			return;
//		}
		int inputedId = Integer.parseInt(cmd.split(" ")[2]);
		articleService.addHitCount(inputedId);
		Article article = articleService.getArticle(inputedId);

		if (article == null) {
			System.out.println("존재하지 않는 게시물 입니다.");
			return;
		}

		Member member = memberService.getMemberByMemberId(article.getMemberId());

		System.out.printf("번호 : %d\n", article.getId());
		System.out.printf("작성날짜 : %s\n", article.getRegDate());
		System.out.printf("수정날짜 : %s\n", article.getUpdateDate());
		System.out.printf("작성자 : %s\n", member.getName());
		System.out.printf("제목 : %s\n", article.getTitle());
		System.out.printf("내용 : %s\n", article.getBody());
		System.out.printf("조회수 : %s\n", article.getHit());
		System.out.printf("추천수: %d\n", article.getRcmCount());

		List<Reply> replies = articleService.getRepliesByArticleId(article.getId());
		if (replies.size() != 0) {
			System.out.println("-----------------------------------------------------------");
			System.out.println("댓글목록");
		}
		for (Reply reply : replies) {
			System.out.printf("%d | 작성자(%s) | 댓글내용: %s \n", reply.getId(), member.getName(), reply.getBody());
		}

	}

}
