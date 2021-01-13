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
    rcmCount INT(10) UNSIGNED NOT NULL default 0,
    hit INT(10) UNSIGNED NOT NULL default 0,
    commentsCount INT(10) UNSIGNED NOT NULL default 0
);

#멤버 테이블 생성
CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(20) NOT NULL,
    loginPw CHAR(20) NOT NULL,
    `name` CHAR(20) NOT NULL
);

#게시판 테이블 생성
CREATE TABLE board (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,   
    `name` CHAR(20) NOT NULL,
    `code` CHAR(20) NOT NULL
);

#댓글 테이블 생성
CREATE TABLE articleReply (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,   
    memberId INT(10) UNSIGNED NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    `body` CHAR(20) NOT NULL
);

#게시물 추천 테이블 생성
CREATE TABLE recommand (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    updateDate DATETIME NOT NULL,  
    memberId INT(10) UNSIGNED NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL
);

#구글 애널리틱스4 페이지 경로별 통계 정보
CREATE TABLE ga4DataPagePath (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    pagePath CHAR(100) NOT NULL UNIQUE,
    hit INT(10) UNSIGNED NOT NULL
);
SELECT * FROM ga4DataPagePath;

# 1단계, 다 불러오기
SELECT pagePath 
FROM ga4DataPagePath AS GA4_PP
WHERE GA4_PP.pagePath LIKE '/article_detail_%.html%';

# 2단계, pagePath 정제
SELECT IF (INSTR(GA4_PP.pagepath, '?') = 0, GA4_PP.pagepath, 
              SUBSTR(GA4_PP.pagepath, 1, INSTR(GA4_PP.pagepath, '?') - 1)) AS 
       pagePathWoQueryStr
FROM   ga4datapagepath AS GA4_PP
WHERE  GA4_PP.pagepath LIKE '/article_detail_%.html%';

# 3단계, pagePathQueryStr(정제된 pagePath)기준으로 sum
SELECT IF (INSTR(GA4_PP.pagepath, '?') = 0, GA4_PP.pagepath, 
              SUBSTR(GA4_PP.pagepath, 1, INSTR(GA4_PP.pagepath, '?') - 1)) AS 
       pagePathWoQueryStr 
FROM   ga4datapagepath AS GA4_PP 
WHERE  GA4_PP.pagepath LIKE '/article_detail_%.html%' 
GROUP  BY pagepathwoquerystr; 

# 4단계, subQuery를 이용
SELECT * 
FROM   (SELECT IF (INSTR(GA4_PP.pagepath, '?') = 0, GA4_PP.pagepath, 
                              SUBSTR(GA4_PP.pagepath, 1, 
                              INSTR(GA4_PP.pagepath, '?') 
                              - 1)) AS 
               pagePathWoQueryStr, 
               SUM(GA4_PP.hit) 
                      AS hit 
        FROM   ga4datapagepath AS GA4_PP 
        WHERE  GA4_PP.pagepath LIKE '/article_detail_%.html%' 
        GROUP  BY pagepathwoquerystr) AS GA4_PP; 

# 5단계, subQuery를 이용해서 나온결과를 다시 재편집
SELECT CAST(REPLACE(REPLACE(GA4_PP.pagepathwoquerystr, '/article_detail_', ''), 
            '.html' 
                   , '') 
                   AS UNSIGNED) AS articleId, 
       hit 
FROM   (SELECT IF (INSTR(GA4_PP.pagepath, '?') = 0, GA4_PP.pagepath, 
                              SUBSTR(GA4_PP.pagepath, 1, 
                              INSTR(GA4_PP.pagepath, '?') 
                              - 1)) AS 
               pagePathWoQueryStr, 
               SUM(GA4_PP.hit) 
                      AS hit 
        FROM   ga4datapagepath AS GA4_PP 
        WHERE  GA4_PP.pagepath LIKE '/article_detail_%.html%' 
        GROUP  BY pagepathwoquerystr) AS GA4_PP; 

#1단계, 조인
SELECT AR.id, 
       AR.hit, 
       GA4_PP.hit 
FROM   article AS AR 
       INNER JOIN (SELECT CAST(REPLACE(REPLACE(GA4_PP.pagepathwoquerystr, 
                                       '/article_detail_', ''), 
                                                               '.html' 
                                                                       , '') 
                                                                       AS 
                               UNSIGNED) AS 
                                               articleId, 
                          hit 
                   FROM   (SELECT IF(INSTR(GA4_PP.pagepath, '?') = 0, 
                                  GA4_PP.pagepath, 
                                                  SUBSTR(GA4_PP.pagepath, 1, 
                                                  INSTR(GA4_PP.pagepath, '?') 
                                                  - 1)) AS 
                                  pagePathWoQueryStr, 
                                  SUM(GA4_PP.hit) 
                                          AS hit 
                           FROM   ga4datapagepath AS GA4_PP 
                           WHERE  GA4_PP.pagepath LIKE '/article_detail_%.html%' 
                           GROUP  BY pagepathwoquerystr) AS GA4_PP) AS GA4_PP 
               ON AR.id = GA4_PP.articleid; 

# 2단계, 실제 쿼리
UPDATE article AS AR
INNER JOIN (
    SELECT CAST(REPLACE(REPLACE(GA4_PP.pagePathWoQueryStr, '/article_detail_', ''), '.html', '') AS UNSIGNED) AS articleId,
    hit
    FROM (
        SELECT 
        IF(
            INSTR(GA4_PP.pagePath, '?') = 0,
            GA4_PP.pagePath,
            SUBSTR(GA4_PP.pagePath, 1, INSTR(GA4_PP.pagePath, '?') - 1)
        ) AS pagePathWoQueryStr,
        SUM(GA4_PP.hit) AS hit
        FROM ga4DataPagePath AS GA4_PP
        WHERE GA4_PP.pagePath LIKE '/article_detail_%.html%'
        GROUP BY pagePathWoQueryStr
    ) AS GA4_PP
) AS GA4_PP
ON AR.id = GA4_PP.articleId
SET AR.hit = GA4_PP.hit;

# aritcle에 추천 카운트 칼럼 추가
ALTER TABLE article ADD COLUMN rcmCount INT(10) UNSIGNED NOT NULL;

# aritcle에 조회수 칼럼 추가
ALTER TABLE article ADD COLUMN hit INT(10) UNSIGNED NOT NULL;

ALTER TABLE aritcle CHANGE COLUMN rcmCount rcmCount = DATETIME NOT NULL;

# 댓글 수 칼럼 추가
ALTER TABLE article ADD COLUMN commentCount INT(10) UNSIGNED NOT NULL;

SELECT * FROM article;
#게시판 데이터 2개 생성
INSERT INTO board 
SET regDate = NOW(),
updateDate = NOW(),
`name` = '공지사항',
`code` = 'notice';

INSERT INTO board 
SET regDate = NOW(),
updateDate = NOW(),
`name` = '자유',
`code` = 'free';

UPDATE article SET boardId = 2 LIMIT 2;

#멥버 데이터 2개 생성
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'aaa',
loginPw = 'aaa',
`name` = 'aaa';

INSERT INTO `member`
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

# 게시물 랜덤 생성
INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = CONCAT('제목_',RAND()),
`body` = CONCAT('내용_',RAND()),
memberId = IF(RAND() > 0.5, 1, 2),
boardId = IF(RAND() > 0.5, 1, 2);

SELECT FLOOR(RAND()*10+1); # 1~10까지 랜덤 생성
SELECT SUM(hit) FROM article;
SELECT SUM(CONVERT(INT,hit)) FROM article;
SELECT COUNT(hit) FROM article;
SELECT hit FROM article WHERE id=1;
SELECT * FROM article;

# 1번 글 수정
UPDATE article SET `body` = '# 공지사항\r\n안녕하세요.\r\n이 사이트는 저의 글 연재 공간입니다.\r\n\r\n---\r\n - 나무\r\n  - 열매\r\n  - 줄기' WHERE id = '1'; 

INSERT INTO article(rcmCount) VALUE(SELECT COUNT(memberId) FROM article WHERE memberId = 1);
SELECT COUNT(articleId) FROM recommand WHERE articleId = 1;
UPDATE article SET rcmCount = (SELECT COUNT(memberId) FROM article WHERE memberId = 2) WHERE id = 3;
INSERT INTO article SET rcmCount  = (SELECT COUNT(memberId) FROM article WHERE memberId = 2);
UPDATE article SET rcmCount = (SELECT COUNT(memberId) FROM recommand WHERE memberId = 1) WHERE id = 2;
INSERT INTO recommand SET updateDate = NOW(), memberId = 1, articleId = 1 WHERE NOT EXISTS (SELECT * FROM recommand WHERE memberId = 1 AND articleId = 1);
SELECT * FROM article WHERE id = 1; 
UPDATE article SET hit = hit + 1 WHERE id = 1;
INSERT INTO recommand 
(updateDate, memberId, articleId) 
SELECT NOW(), 1 , 3 
FROM DUAL WHERE NOT EXISTS 
(SELECT * FROM recommand WHERE memberId = 1 AND articleId = 3);



SELECT * FROM article;
SELECT * FROM `member`;
SELECT * FROM board;
SELECT * FROM articleReply;
SELECT * FROM recommand;
SELECT * FROM article WHERE boardId = 1 ORDER BY id DESC;
SELECT * FROM article WHERE boardId = 1 ORDER BY id DESC LIMIT 0,10;
SELECT * FROM article WHERE boardId = 1 ORDER BY id DESC LIMIT 10,10;
SELECT * FROM article WHERE boardId = 1 ORDER BY id DESC LIMIT 20,10;
SELECT * , M.name AS extra_writer FROM article AS A INNER JOIN `member` AS M ON A.memberId = M.id WHERE boardId = 2;

SELECT *,
M.name AS extra_writer
FROM article AS A
INNER JOIN `member` AS M
ON A.memberId = M.id
WHERE boardId = 1
ORDER BY A.id DESC;