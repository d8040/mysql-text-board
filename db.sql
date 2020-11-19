# DB 생성
DROP DATABASE IF EXISTS textBoard;
CREATE DATABASE textBoard;
USE textBoard;

# 게시물 테이블 생성
CREATE TABLE article (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title CHAR(200) NOT NULL,
    `body` TEXT NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    boardId INT(10) UNSIGNED NOT NULL,
    rcmCount INT(10) UNSIGNED NOT NULL
);

#멤버 테이블 생성
CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(200) NOT NULL,
    loginPw CHAR(200) NOT NULL,
    `name` CHAR(200) NOT NULL
);

#게시판 테이블 생성
CREATE TABLE board (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,   
    `name` CHAR(200) NOT NULL
);

#댓글 테이블 생성
CREATE TABLE articleReply (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,   
    memberId INT(10) UNSIGNED NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    `body` CHAR(200) NOT NULL
);

#게시물 추천 테이블 생성
CREATE TABLE recommand (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    updateDate DATETIME NOT NULL,  
    memberId INT(10) UNSIGNED NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL
);
#게시판 데이터 2개 생성
INSERT INTO BOARD 
SET regDate = NOW(),
updateDate = NOW(),
`name` = 'notice';

INSERT INTO BOARD 
SET regDate = NOW(),
updateDate = NOW(),
`name` = 'free';

#멥버 데이터 2개 생성
INSERT INTO MEMBER
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'aaa',
loginPw = 'aaa',
`name` = 'aaa';

INSERT INTO MEMBER
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'bbb',
loginPw = 'bbb',
`name` = 'bbb';

# 게시물 데이터 3개 생성
INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목1',
`body` = '내용1',
memberId = 1,
boardId = 1;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목2',
`body` = '내용2',
memberId = 1,
boardId = 1;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목3',
`body` = '내용3',
memberId = 1,
boardId = 1;


INSERT INTO article(rcmCount) VALUE(SELECT COUNT(memberId) FROM article WHERE memberId = 1);
SELECT COUNT(articleId) FROM recommand WHERE articleId = 1;
UPDATE article SET rcmCount = (SELECT COUNT(memberId) FROM article WHERE memberId = 2) WHERE id = 3;
INSERT INTO article SET rcmCount  = (SELECT COUNT(memberId) FROM article WHERE memberId = 2);
UPDATE article SET rcmCount = (SELECT COUNT(memberId) FROM recommand WHERE memberId = 1) WHERE id = 2;
INSERT INTO recommand SET updateDate = NOW(), memberId = 1, articleId = 1 WHERE NOT EXISTS (SELECT * FROM recommand WHERE memberId = 1 AND articleId = 1);

INSERT INTO recommand 
(updateDate, memberId, articleId) 
SELECT NOW(), 1 , 3 
FROM DUAL 
WHERE NOT EXISTS 
(SELECT * FROM recommand WHERE memberId = 1 AND articleId = 3);

# aritcle에 추천 카운트 칼럼 추가
ALTER TABLE article ADD COLUMN rcmCount INT(10) UNSIGNED NOT NULL;

# aritcle에 조회수 칼럼 추가
ALTER TABLE article ADD COLUMN hit INT(10) UNSIGNED NOT NULL;

ALTER TABLE aritcle CHANGE COLUMN rcmCount rcmCount = DATETIME NOT NULL;

SELECT * FROM article;
SELECT * FROM `member`;
SELECT * FROM board;
SELECT * FROM articleReply;
SELECT * FROM recommand;

SELECT *,
M.name AS extra_writer
FROM article AS A
INNER JOIN `member` AS M
ON A.memberId = M.id
WHERE boardId = 1
ORDER BY A.id DESC;