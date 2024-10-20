CREATE TABLE BD_MEMBER (
   MEMBER_NUM         NUMBER                        --PK
   ,MEMBER_EMAIL      VARCHAR2(100) UNIQUE NOT NULL      --이메일 (아이디)
   ,MEMBER_PW         VARCHAR2(100) NOT NULL            --비밀번호
   ,MEMBER_NAME       VARCHAR2(50) NOT NULL            --이름
   ,MEMBER_PHONE       VARCHAR2(50)                  --전화번호
   ,MEMBER_NICKNAME   VARCHAR2(100) UNIQUE NOT NULL      --닉네임
   ,MEMBER_PROFILE_WAY VARCHAR2(500) NOT NULL            --프로필 이미지 서버 경로
   ,MEMBER_ROLE      VARCHAR2(20) DEFAULT 'user'         --권한 (일반, 점주, 관리자)
   ,MEMBER_HIREDAY      DATE DEFAULT SYSDATE            --가입일
   ,CONSTRAINT PK_BD_MEMBER PRIMARY KEY(MEMBER_NUM)
   ,CONSTRAINT CHECK_MEMBER_ROLE CHECK(MEMBER_ROLE IN('user', 'seller', 'admin')) 
);
--테이블 컬럼 모두 조회
SELECT * FROM BD_MEMBER;
--테이블 제거
DROP TABLE BD_MEMBER;
--특정 회원 제거
DELETE FROM BD_MEMBER WHERE MEMBER_NUM=1;
--관련테이블과 함께 테이블 제거
DROP TABLE BD_MEMBER CASCADE CONSTRAINTS;
DROP TABLE BD_CATEGORY CASCADE CONSTRAINTS;
DROP TABLE BD_BOARD CASCADE CONSTRAINTS;
DROP TABLE BD_LIKE CASCADE CONSTRAINTS;
DROP TABLE BD_IMAGE CASCADE CONSTRAINTS;
DROP TABLE BD_REPLY CASCADE CONSTRAINTS;



--INSERT 고유번호, 이메일, 비밀번호, 이름, 전화번호, 닉네임, 프로필 경로, 권한, 가입일 추가(회원가입)
INSERT INTO BD_MEMBER(MEMBER_NUM,MEMBER_EMAIL,MEMBER_PW,MEMBER_NAME,MEMBER_PHONE,MEMBER_NICKNAME,MEMBER_PROFILE_WAY,MEMBER_ROLE)
VALUES((SELECT NVL(MAX(MEMBER_NUM),0)+1 FROM BD_MEMBER),'snow@naver.com','1234','스노우','010-1234-5678','snow','PICTURE','user');
INSERT INTO BD_MEMBER(MEMBER_NUM,MEMBER_EMAIL,MEMBER_PW,MEMBER_NAME,MEMBER_PHONE,MEMBER_NICKNAME,MEMBER_PROFILE_WAY,MEMBER_ROLE)
VALUES((SELECT NVL(MAX(MEMBER_NUM),0)+1 FROM BD_MEMBER),'ari@naver.com','5678','아리','010-1234-5111','ari','PICTURE','admin');

--SELECTALL 전체 회원 조회.
SELECT MEMBER_NUM, MEMBER_NAME, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_PHONE, MEMBER_PROFILE_WAY,MEMBER_ROLE,MEMBER_HIREDAY FROM BD_MEMBER

--SELECTALL 최근 7일간 신규 회원 모두 조회.(현재날짜-7일보다 가입일자가 클 때)
--TRUNC : 날짜 또는 숫자의 값을 자르는 함수.현재 날짜에서 시간 제거. INTERVAL:날짜 및 시간 차이를 나타내는 값을 표현하는 단어. 
--회원 고유번호, 이메일, 닉네임, 전화번호, 프로필경로, 권한, 가입일자 데이터 조회
SELECT MEMBER_NUM, MEMBER_NAME, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_PHONE, MEMBER_PROFILE_WAY,MEMBER_ROLE,MEMBER_HIREDAY 
FROM BD_MEMBER WHERE TRUNC(MEMBER_HIREDAY) >= SYSDATE - INTERVAL '7' DAY

--SELECTONE 인풋값 이메일과 동일한 데이터 있는지 회원 조회.(이메일 중복검사) 
SELECT MEMBER_EMAIL FROM BD_MEMBER WHERE MEMBER_EMAIL='teemo@naver.com';

--SELECTONE 인풋값 닉네임과 동일한 데이터 있는지 회원 조회.(닉네임 중복검사)
SELECT MEMBER_NICKNAME FROM BD_MEMBER WHERE MEMBER_NICKNAME= 'snow';

--LOGIN_SELECTONE 로그인 진행 회원 조회.인풋값 이메일, 비밀번호 일치할 때 고유번호, 닉네임, 역할 모두 반환
SELECT MEMBER_NUM, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_ROLE 
FROM BD_MEMBER WHERE MEMBER_EMAIL = 'teemo@naver.com' AND MEMBER_PW = '1234'

--MEMBER_DETAIL_SELECTONE 회원 1명 상세조회.인풋값 PK통해서 고유번호, 이름, 이메일, 닉네임, 전화번호, 프로필 사진경로 가입일자 역할 모두 반환
SELECT MEMBER_NUM, MEMBER_NAME, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_PHONE, MEMBER_PROFILE_WAY, MEMBER_ROLE,MEMBER_HIREDAY
FROM BD_MEMBER WHERE MEMBER_NUM = 1

--PASSWORD_CHEK_SELECTONE 패스워드 일치 여부 확인 회원 1명 조회. 인풋값 PK,패스워드 통해서 고유번호, 이메일, 이름, 폰번호, 닉네임, 프로필사진 경로, 역할 모두 반환
SELECT MEMBER_NUM FROM BD_MEMBER WHERE MEMBER_NUM = 1 AND MEMBER_PW ='1234'

--PROFILEWAY_SELECTONE특정 회원 프로필 사진 경로 조회 : PK값 C에서 주면, M은 특정 회원 조회 + 사진 경로 반환
SELECT MEMBER_PROFILE_WAY FROM BD_MEMBER WHERE MEMBER_NUM = 1

--DELETE 회원 데이터 삭제: PK 같을 때, 해당 회원 데이터 모두 삭제
DELETE FROM BD_MEMBER WHERE MEMBER_NUM = ?

--UPDATE 회원 1명 수정. pk값을 통해 이메일 값 변경
UPDATE BD_MEMBER SET MEMBER_EMAIL = 'rhalwls56@naver.com' WHERE MEMBER_NUM = 2
SELECT * FROM BD_MEMBER
--비밀번호값 수정 : 인풋값 PK 받아서 비밀번호 수정
UPDATE BD_MEMBER SET MEMBER_PW = ? WHERE MEMBER_NUM = ?

--UPDATE 회원 1명 조회. pk값 통해 닉네임 값 변경
UPDATE BD_MEMBER SET MEMBER_NICKNAME = '타모' WHERE MEMBER_NUM = 2

--UPDATE 회원 1명 조회. pk값 통해 휴대폰 번호 값 변경
UPDATE BD_MEMBER SET MEMBER_PHONE = '010-1234-9101' WHERE MEMBER_NUM = 2

--UPDATE 회원 1명 조회. pk값 통해 프로필사진경로 값 변경
UPDATE BD_MEMBER SET MEMBER_PROFILE_WAY = 'WAY' WHERE MEMBER_NUM = 2