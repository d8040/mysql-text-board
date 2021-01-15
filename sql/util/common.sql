#게시글에 붙어 있는 태그들 확인
SELECT A.id, A.title, GROUP_CONCAT(T.body)
FROM article AS A
LEFT JOIN tag AS T
ON T.relTypeCode = 'article'
AND A.id = T.relId
GROUP BY A.id;