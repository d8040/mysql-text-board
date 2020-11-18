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
		} else if (cmd.startsWith("article selectBoard ")) {
			doSelectBoard(cmd);
		} else if (cmd.startsWith("article addReply ")) {
			doAddReply(cmd);
		} else if (cmd.startsWith("article modifyReply ")) {
			doModifyReply(cmd);
		}
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

		if (reply.memberId != memberId) {
			System.out.println("수정권한이 없습니다.");
			return;
		}

		System.out.printf("번호 : %d\n", reply.id);
		System.out.printf("작성날짜 : %s\n", reply.regDate);
		System.out.printf("작성자 : %s\n", reply.memberId);

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
		System.out.printf("번호 : %d\n", article.id);
		System.out.printf("작성날짜 : %s\n", article.regDate);
		System.out.printf("작성자 : %s\n", article.memberId);
		System.out.printf("제목 : %s\n", article.title);
		System.out.printf("내용 : %s\n", article.body);

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
		String inputedName = cmd.split(" ")[2];

		Board board = articleService.getBoardByName(inputedName);

		Container.session.selectBoardId = board.id;
		System.out.println(board.name + "로 이동되었습니다.");
	}

	private void doMakeBoard(String cmd) {
		System.out.println("== 게시판 등록 ==");

		if (Container.session.isLogined() == false) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		System.out.printf("게시판 이름 : ");
		String name = Container.scanner.nextLine().trim();

		int loginedId = Container.session.loginedMemberId;
		int boardId = articleService.makeBoard(loginedId, name);

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
		int boardId = Container.session.selectBoardId;

		int id = articleService.add(boardId, memberId, title, body);

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

		if (article.memberId != memberId) {
			System.out.println("수정권한이 없습니다.");
			return;
		}

		System.out.printf("번호 : %d\n", article.id);
		System.out.printf("작성날짜 : %s\n", article.regDate);
		System.out.printf("작성자 : %s\n", article.memberId);

		System.out.printf("제목 : ");
		String title = Container.scanner.nextLine();
		System.out.printf("내용 : ");
		String body = Container.scanner.nextLine();

		articleService.modify(memberId, inputedId, title, body);

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
		if (article.memberId != Container.session.loginedMemberId) {
			System.out.println("권한이 없습니다.");
			return;
		}

		articleService.delete(inputedId);
		System.out.printf("%d번 게시물을 삭제하였습니다.\n", inputedId);
	}

	private void showList(String cmd) {
		System.out.println("== 게시물 리스트 ==");
		int boardId = Container.session.selectBoardId;
		List<Article> articles = articleService.getArticles(boardId);

		System.out.println("번호 / 작성 / 수정 / 작성자 / 제목");

		for (Article article : articles) {
			Member member = memberService.getMemberByMemberId(article.memberId);
			System.out.printf("%d / %s / %s / %s / %s\n", article.id, article.regDate, article.updateDate, member.name,
					article.title);
		}
	}

	private void showDetail(String cmd) {
		System.out.println("== 게시물 상세페이지 ==");
//		if (Container.session.isLogined() == false) {
//			System.out.println("로그인 후 이용해주세요.");
//			return;
//		}
		int inputedId = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(inputedId);

		if (article == null) {
			System.out.println("존재하지 않는 게시물 입니다.");
			return;
		}

		Member member = memberService.getMemberByMemberId(article.memberId);
		List<Reply> replies = articleService.getRepliesByArticleId(article.id);

		System.out.printf("번호 : %d\n", article.id);
		System.out.printf("작성날짜 : %s\n", article.regDate);
		System.out.printf("수정날짜 : %s\n", article.updateDate);
		System.out.printf("작성자 : %s\n", member.name);
		System.out.printf("제목 : %s\n", article.title);
		System.out.printf("내용 : %s\n", article.body);

		if (replies != null) {
			System.out.println("-----------------------------------------------------------");
			System.out.println("댓글목록");
			for (Reply reply : replies) {
				System.out.printf("%d | 작성자(%s) | 댓글내용: %s \n", reply.id , member.name, reply.body);
			}
		}
	}

}
