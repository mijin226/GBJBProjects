
-- fishshapedbread.bb_product_category definition
CREATE TABLE `bb_product_category` (
  `PRODUCT_CATEGORY_NUM` int NOT NULL AUTO_INCREMENT,
  `PRODUCT_CATEGORY_NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`PRODUCT_CATEGORY_NUM`),
  UNIQUE KEY `PRODUCT_CATEGORY_NAME` (`PRODUCT_CATEGORY_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fishshapedbread.bb_product definition

CREATE TABLE `bb_product` (
  `PRODUCT_NUM` int NOT NULL AUTO_INCREMENT,
  `PRODUCT_NAME` varchar(100) NOT NULL,
  `PRODUCT_PRICE` int NOT NULL,
  `PRODUCT_PROFILE_WAY` varchar(500) NOT NULL,
  `PRODUCT_CATEGORY_NUM` int DEFAULT NULL,
  PRIMARY KEY (`PRODUCT_NUM`),
  KEY `PRODUCT_CATEGORY_NUM` (`PRODUCT_CATEGORY_NUM`),
  CONSTRAINT `bb_product_ibfk_2` FOREIGN KEY (`PRODUCT_CATEGORY_NUM`) REFERENCES `bb_product_category` (`PRODUCT_CATEGORY_NUM`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fishshapedbread.bb_member definition

CREATE TABLE `bb_member` (
  `MEMBER_NUM` int NOT NULL AUTO_INCREMENT,
  `MEMBER_EMAIL` varchar(200) NOT NULL,
  `MEMBER_PASSWORD` varchar(100) NOT NULL,
  `MEMBER_NAME` varchar(30) NOT NULL,
  `MEMBER_NICKNAME` varchar(50) NOT NULL,
  `MEMBER_PHONE` varchar(20) DEFAULT NULL,
  `MEMBER_PROFILE_WAY` varchar(500) DEFAULT NULL,
  `MEMBER_ROLE` varchar(30) DEFAULT 'USER',
  `MEMBER_HIREDAY` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`MEMBER_NUM`),
#   UNIQUE KEY `MEMBER_EMAIL` (`MEMBER_EMAIL`),
#   UNIQUE KEY `MEMBER_NICKNAME` (`MEMBER_NICKNAME`),
  CONSTRAINT `bb_member_chk_1` CHECK ((`MEMBER_ROLE` in (_utf8mb4'USER',_utf8mb4'ADMIN',_utf8mb4'ONER')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*update error 해결*/
# ALTER TABLE bb_member
#     DROP INDEX MEMBER_EMAIL,
#     DROP INDEX MEMBER_NICKNAME;

-- fishshapedbread.bb_store definition

CREATE TABLE `bb_store` (
  `STORE_NUM` int NOT NULL AUTO_INCREMENT,
  `STORE_NAME` varchar(100) NOT NULL,
  `STORE_ADDRESS` varchar(500) NOT NULL,
  `STORE_ADDRESS_DETAIL` varchar(100) NOT NULL,
  `STORE_CONTACT` varchar(20) DEFAULT NULL,
  `STORE_CLOSED` char(1) DEFAULT 'N',
  `STORE_SECRET` char(1) DEFAULT 'N',
  PRIMARY KEY (`STORE_NUM`),
  CONSTRAINT `bb_store_chk_1` CHECK ((`STORE_CLOSED` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_store_chk_2` CHECK ((`STORE_SECRET` in (_utf8mb4'Y',_utf8mb4'N')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fishshapedbread.bb_declare definition

CREATE TABLE `bb_declare` (
  `DECLARE_NUM` int NOT NULL AUTO_INCREMENT,
  `STORE_NUM` int DEFAULT NULL,
  `DECLARE_CONTENT` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`DECLARE_NUM`),
  KEY `STORE_NUM` (`STORE_NUM`),
  CONSTRAINT `bb_declare_ibfk_2` FOREIGN KEY (`STORE_NUM`) REFERENCES `bb_store` (`STORE_NUM`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fishshapedbread.bb_payment definition

--  결제 테이블 생성 (BB_PAYMENT)
CREATE TABLE BB_PAYMENT (
    PAYMENT_NUM INT AUTO_INCREMENT PRIMARY KEY, -- 결제 번호 (PK, AUTO_INCREMENT)
    MEMBER_NUM INT NOT NULL, -- 회원 번호 (외래키)
    PAYMENT_AMOUNT INT NOT NULL, -- 결제 금액
    PAYMENT_AT DATETIME DEFAULT CURRENT_TIMESTAMP, -- 결제 시간
    PAYMENT_NAME VARCHAR(100) NOT NULL, -- 결제 상품명
    IMP_UUID VARCHAR(100) UNIQUE, -- 포트원에서 부여하는 상점 고유번호
    ADMIN_CHECKED CHAR(1) DEFAULT 'N' CHECK (ADMIN_CHECKED IN ('Y', 'N')), -- 관리자 확인
    FOREIGN KEY (MEMBER_NUM) REFERENCES BB_MEMBER(MEMBER_NUM) ON DELETE CASCADE -- 회원 번호 외래키
);


-- fishshapedbread.bb_point definition

CREATE TABLE `bb_point` (
  `POINT_NUM` int NOT NULL AUTO_INCREMENT,
  `MEMBER_NUM` int DEFAULT NULL,
  `POINT_PLUS` int DEFAULT NULL,
  `POINT_MINUS` int DEFAULT NULL,
  `POINT_CONTENT` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`POINT_NUM`),
  KEY `MEMBER_NUM` (`MEMBER_NUM`),
  CONSTRAINT `bb_point_ibfk_1` FOREIGN KEY (`MEMBER_NUM`) REFERENCES `bb_member` (`MEMBER_NUM`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fishshapedbread.bb_store_menu definition

CREATE TABLE `bb_store_menu` (
  `STORE_MENU_NUM` int NOT NULL AUTO_INCREMENT,
  `STORE_NUM` int NOT NULL,
  `STORE_MENU_NORMAL` varchar(1) DEFAULT 'N',
  `STORE_MENU_VEG` char(1) DEFAULT 'N',
  `STORE_MENU_MINI` char(1) DEFAULT 'N',
  `STORE_MENU_POTATO` char(1) DEFAULT 'N',
  `STORE_MENU_ICE` char(1) DEFAULT 'N',
  `STORE_MENU_CHEESE` char(1) DEFAULT 'N',
  `STORE_MENU_PASTRY` char(1) DEFAULT 'N',
  `STORE_MENU_OTHER` char(1) DEFAULT 'N',
  PRIMARY KEY (`STORE_MENU_NUM`),
  UNIQUE KEY `STORE_NUM` (`STORE_NUM`),
  CONSTRAINT `bb_store_menu_ibfk_1` FOREIGN KEY (`STORE_NUM`) REFERENCES `bb_store` (`STORE_NUM`) ON DELETE CASCADE,
  CONSTRAINT `bb_store_menu_chk_2` CHECK ((`STORE_MENU_VEG` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_store_menu_chk_3` CHECK ((`STORE_MENU_MINI` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_store_menu_chk_4` CHECK ((`STORE_MENU_POTATO` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_store_menu_chk_5` CHECK ((`STORE_MENU_ICE` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_store_menu_chk_6` CHECK ((`STORE_MENU_CHEESE` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_store_menu_chk_7` CHECK ((`STORE_MENU_PASTRY` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_store_menu_chk_8` CHECK ((`STORE_MENU_OTHER` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `check_store_menu_normal` CHECK ((`STORE_MENU_NORMAL` in (_utf8mb4'Y',_utf8mb4'N')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fishshapedbread.bb_store_payment definition

CREATE TABLE `bb_store_payment` (
  `STORE_PAYMENT_NUM` int NOT NULL AUTO_INCREMENT,
  `STORE_NUM` int NOT NULL,
  `STORE_PAYMENT_CASHMONEY` char(1) DEFAULT 'N',
  `STORE_PAYMENT_CARD` char(1) DEFAULT 'N',
  `STORE_PAYMENT_ACCOUNT` char(1) DEFAULT 'N',
  PRIMARY KEY (`STORE_PAYMENT_NUM`),
  UNIQUE KEY `STORE_NUM` (`STORE_NUM`),
  CONSTRAINT `bb_store_payment_ibfk_1` FOREIGN KEY (`STORE_NUM`) REFERENCES `bb_store` (`STORE_NUM`) ON DELETE CASCADE,
  CONSTRAINT `bb_store_payment_chk_1` CHECK ((`STORE_PAYMENT_CASHMONEY` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_store_payment_chk_2` CHECK ((`STORE_PAYMENT_CARD` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_store_payment_chk_3` CHECK ((`STORE_PAYMENT_ACCOUNT` in (_utf8mb4'Y',_utf8mb4'N')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fishshapedbread.bb_store_work definition

CREATE TABLE `bb_store_work` (
  `WORK_NUM` int NOT NULL AUTO_INCREMENT,
  `STORE_NUM` int NOT NULL,
  `STORE_WORK_WEEK` char(3) NOT NULL,
  `STORE_WORK_OPEN` datetime NOT NULL,
  `STORE_WORK_CLOSE` datetime NOT NULL,
  PRIMARY KEY (`WORK_NUM`),
  CONSTRAINT `bb_store_work_ibfk_1` FOREIGN KEY (`STORE_NUM`) REFERENCES `bb_store` (`STORE_NUM`) ON DELETE CASCADE,
  CONSTRAINT `bb_store_work_chk_1` CHECK ((`STORE_WORK_WEEK` in (_utf8mb4'MON',_utf8mb4'TUE',_utf8mb4'WED',_utf8mb4'THU',_utf8mb4'FRI',_utf8mb4'SAT',_utf8mb4'SUN')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fishshapedbread.bb_board_category definition

CREATE TABLE `bb_board_category` (
  `BOARD_CATEGORY_NUM` int NOT NULL AUTO_INCREMENT,
  `BOARD_CATEGORY_NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`BOARD_CATEGORY_NUM`),
  UNIQUE KEY `BOARD_CATEGORY_NAME` (`BOARD_CATEGORY_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- fishshapedbread.bb_board definition

CREATE TABLE `bb_board` (
  `BOARD_NUM` int NOT NULL AUTO_INCREMENT,
  `BOARD_TITLE` varchar(500) NOT NULL,
  `BOARD_CONTENT` varchar(1000) DEFAULT NULL,
  `BOARD_FOLDER` varchar(1000) DEFAULT NULL,
  `MEMBER_NUM` int DEFAULT NULL,
  `BOARD_WRITE_DAY` datetime DEFAULT CURRENT_TIMESTAMP,
  `BOARD_OPEN` char(1) DEFAULT 'N',
  `BOARD_CATEGORY_NUM` int DEFAULT NULL,
  `STORE_NUM` int DEFAULT NULL,
  `PRODUCT_NUM` int DEFAULT NULL,
  `BOARD_DELETE` char(1) DEFAULT 'N',
  PRIMARY KEY (`BOARD_NUM`),
  KEY `MEMBER_NUM` (`MEMBER_NUM`),
  KEY `BOARD_CATEGORY_NUM` (`BOARD_CATEGORY_NUM`),
  KEY `STORE_NUM` (`STORE_NUM`),
  KEY `bb_board_ibfk_4` (`PRODUCT_NUM`),
  CONSTRAINT `bb_board_ibfk_1` FOREIGN KEY (`MEMBER_NUM`) REFERENCES `bb_member` (`MEMBER_NUM`) ON DELETE SET NULL,
  CONSTRAINT `bb_board_ibfk_2` FOREIGN KEY (`BOARD_CATEGORY_NUM`) REFERENCES `bb_board_category` (`BOARD_CATEGORY_NUM`) ON DELETE CASCADE,
  CONSTRAINT `bb_board_ibfk_3` FOREIGN KEY (`STORE_NUM`) REFERENCES `bb_store` (`STORE_NUM`) ON DELETE CASCADE,
  CONSTRAINT `bb_board_ibfk_4` FOREIGN KEY (`PRODUCT_NUM`) REFERENCES `bb_product` (`PRODUCT_NUM`) ON DELETE CASCADE,
  CONSTRAINT `bb_board_chk_1` CHECK ((`BOARD_OPEN` in (_utf8mb4'Y',_utf8mb4'N'))),
  CONSTRAINT `bb_board_chk_2` CHECK ((`BOARD_DELETE` in (_utf8mb4'Y',_utf8mb4'N')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fishshapedbread.bb_like definition

CREATE TABLE `bb_like` (
  `LIKE_NUM` int NOT NULL AUTO_INCREMENT,
  `BOARD_NUM` int NOT NULL,
  `MEMBER_NUM` int DEFAULT NULL,
  PRIMARY KEY (`LIKE_NUM`),
  KEY `BOARD_NUM` (`BOARD_NUM`),
  KEY `MEMBER_NUM` (`MEMBER_NUM`),
  CONSTRAINT `bb_like_ibfk_1` FOREIGN KEY (`BOARD_NUM`) REFERENCES `bb_board` (`BOARD_NUM`) ON DELETE CASCADE,
  CONSTRAINT `bb_like_ibfk_2` FOREIGN KEY (`MEMBER_NUM`) REFERENCES `bb_member` (`MEMBER_NUM`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 주문 정보
CREATE TABLE BB_ORDER (
    ORDER_NUM INT AUTO_INCREMENT PRIMARY KEY,
    MEMBER_NUM INT NOT NULL,
    ADMIN_CHECKED CHAR(1) default 'N',
    ORDER_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 현재 날짜 및 시간 저장
    ORDER_ADDRESS VARCHAR(100),
    FOREIGN KEY (MEMBER_NUM) REFERENCES BB_MEMBER(MEMBER_NUM) ON DELETE CASCADE ,  -- 외래 키 설정 (회원 테이블 참조)
    CHECK (ADMIN_CHECKED IN ('Y', 'N', 'C'))  -- 체크 제약 조건 수정(Y, N, C(취소))
);

-- 주문 상품 정보
CREATE TABLE BB_ORDER_DETAIL (
    ORDER_DETAIL_NUM INT AUTO_INCREMENT PRIMARY KEY,
    PRODUCT_NUM INT,
    ORDER_QUANTITY INT,
    ORDER_NUM INT,
    FOREIGN KEY (ORDER_NUM) REFERENCES BB_ORDER(ORDER_NUM) ON DELETE CASCADE,  -- 외래 키 설정 (주문 테이블 참조)
    FOREIGN KEY (PRODUCT_NUM) REFERENCES BB_PRODUCT(PRODUCT_NUM) ON DELETE SET NULL ,  -- 외래 키 설정 (상품 테이블 참조)
    UNIQUE (ORDER_NUM, PRODUCT_NUM)  -- ORDER_NUM과 PRODUCT_NUM의 조합이 유니크함을 보장
);


-- fishshapedbread.bb_reply definition

CREATE TABLE `bb_reply` (
  `REPLY_NUM` int NOT NULL AUTO_INCREMENT,
  `REPLY_CONTENT` varchar(500) NOT NULL,
  `MEMBER_NUM` int DEFAULT NULL,
  `REPLY_WRITE_DAY` datetime DEFAULT CURRENT_TIMESTAMP,
  `BOARD_NUM` int DEFAULT NULL,
  PRIMARY KEY (`REPLY_NUM`),
  KEY `MEMBER_NUM` (`MEMBER_NUM`),
  KEY `BOARD_NUM` (`BOARD_NUM`),
  CONSTRAINT `bb_reply_ibfk_1` FOREIGN KEY (`MEMBER_NUM`) REFERENCES `bb_member` (`MEMBER_NUM`) ON DELETE SET NULL,
  CONSTRAINT `bb_reply_ibfk_2` FOREIGN KEY (`BOARD_NUM`) REFERENCES `bb_board` (`BOARD_NUM`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- fishshapedbread.bb_view_board_join source

create or replace
    algorithm = UNDEFINED view `fishshapedbread`.`bb_view_board_join` as
select
    `bb`.`BOARD_NUM` as `BOARD_NUM`,
    `bb`.`BOARD_TITLE` as `BOARD_TITLE`,
    `bb`.`BOARD_CONTENT` as `BOARD_CONTENT`,
    `bb`.`BOARD_WRITE_DAY` as `BOARD_WRITE_DAY`,
    `bb`.`BOARD_OPEN` as `BOARD_OPEN`,
    `bb`.`BOARD_DELETE` as `BOARD_DELETE`,
    bb.BOARD_FOLDER,
    `bm`.`MEMBER_NUM` as `MEMBER_NUM`,
    `bm`.`MEMBER_NICKNAME` as `MEMBER_NICKNAME`,
    `bm`.`MEMBER_PROFILE_WAY` as `MEMBER_PROFILE_WAY`,
    ifnull(`bl2`.`LIKE_CNT`, 0) as `LIKE_CNT`,
    `bbc`.`BOARD_CATEGORY_NUM` as `BOARD_CATEGORY_NUM`,
    `bbc`.`BOARD_CATEGORY_NAME` as `BOARD_CATEGORY_NAME`,
    (SELECT ifnull(COUNT(*), 0) FROM BB_REPLY WHERE BOARD_NUM = bb.BOARD_NUM) as REPLY_CNT
from
    (((`fishshapedbread`.`bb_board` `bb`
        left join `fishshapedbread`.`bb_member` `bm` on
        ((`bb`.`MEMBER_NUM` = `bm`.`MEMBER_NUM`)))
        left join (
            select
                `fishshapedbread`.`bb_like`.`BOARD_NUM` as `BOARD_NUM`,
                count(0) as `LIKE_CNT`
            from
                `fishshapedbread`.`bb_like`
            group by
                `fishshapedbread`.`bb_like`.`BOARD_NUM`) `bl2` on
        ((`bb`.`BOARD_NUM` = `bl2`.`BOARD_NUM`)))
        left join `fishshapedbread`.`bb_board_category` `bbc` on
        ((`bb`.`BOARD_CATEGORY_NUM` = `bbc`.`BOARD_CATEGORY_NUM`)));

-- fishshapedbread.bb_view_product_join source

create or replace
algorithm = UNDEFINED view `fishshapedbread`.`bb_view_product_join` as
select
    `bp`.`PRODUCT_NUM` as `PRODUCT_NUM`,
    `bp`.`PRODUCT_NAME` as `PRODUCT_NAME`,
    `bp`.`PRODUCT_PRICE` as `PRODUCT_PRICE`,
    `bp`.`PRODUCT_PROFILE_WAY` as `PRODUCT_PROFILE_WAY`,
    `bb`.`BOARD_NUM` as `BOARD_NUM`,
    `bp`.`PRODUCT_CATEGORY_NUM` as `PRODUCT_CATEGORY_NUM`,
    `bpc`.`PRODUCT_CATEGORY_NAME` as `PRODUCT_CATEGORY_NAME`,
    `bb`.`BOARD_TITLE` as `BOARD_TITLE`,
    `bb`.`BOARD_CONTENT` as `BOARD_CONTENT`
from
    ((`fishshapedbread`.`bb_product` `bp`
join `fishshapedbread`.`bb_product_category` `bpc` on
    ((`bp`.`PRODUCT_CATEGORY_NUM` = `bpc`.`PRODUCT_CATEGORY_NUM`)))
left join `fishshapedbread`.`bb_board` `bb` on
    ((`bp`.`PRODUCT_NUM` = `bb`.`PRODUCT_NUM`)));


-- fishshapedbread.bb_view_store_join source
-- join 조건이 inner join으로 되어있어 신고가 없으면 가게 정보가 뜨지 않는 문제 해결을 위해 수정
CREATE OR REPLACE VIEW bb_view_store_join AS
SELECT
    s.STORE_NUM,
    s.STORE_NAME,
    s.STORE_ADDRESS,
    s.STORE_ADDRESS_DETAIL,
    s.STORE_CONTACT,
    s.STORE_CLOSED,
    s.STORE_SECRET,
    sm.STORE_MENU_NORMAL,
    sm.STORE_MENU_VEG,
    sm.STORE_MENU_MINI,
    sm.STORE_MENU_POTATO,
    sm.STORE_MENU_ICE,
    sm.STORE_MENU_CHEESE,
    sm.STORE_MENU_PASTRY,
    sm.STORE_MENU_OTHER,
    sp.STORE_PAYMENT_CASHMONEY,
    sp.STORE_PAYMENT_CARD,
    sp.STORE_PAYMENT_ACCOUNT,
    -- STORE_DECLARED 필요없음
    CASE
        WHEN EXISTS (
            SELECT 1
            FROM bb_declare
            WHERE bb_declare.STORE_NUM = s.STORE_NUM
        ) THEN 'Y'
        ELSE 'N'
        END AS STORE_DECLARED
FROM bb_store s
         LEFT JOIN bb_store_menu sm ON s.STORE_NUM = sm.STORE_NUM
         LEFT JOIN bb_store_payment sp ON s.STORE_NUM = sp.STORE_NUM;


CREATE OR REPLACE VIEW BB_VIEW_SEARCHSTOREDATA_JOIN AS
SELECT S.STORE_NUM, S.STORE_NAME, S.STORE_ADDRESS, S.STORE_ADDRESS_DETAIL,
       S.STORE_CONTACT, S.STORE_CLOSED, s.STORE_SECRET,
       SM.STORE_MENU_NORMAL, SM.STORE_MENU_VEG, SM.STORE_MENU_MINI,
       SM.STORE_MENU_POTATO, SM.STORE_MENU_ICE, SM.STORE_MENU_CHEESE,
       SM.STORE_MENU_PASTRY, SM.STORE_MENU_OTHER,
       SP.STORE_PAYMENT_CASHMONEY, SP.STORE_PAYMENT_CARD, SP.STORE_PAYMENT_ACCOUNT
FROM BB_STORE S
         left JOIN BB_STORE_MENU SM ON S.STORE_NUM = SM.STORE_NUM
         left JOIN BB_STORE_PAYMENT SP ON S.STORE_NUM = SP.STORE_NUM;
         
-- trigger
DELIMITER //
-- 개별적으로 드래그하여 생성
CREATE TRIGGER after_payment_insert
AFTER INSERT ON BB_PAYMENT
FOR EACH ROW
BEGIN
    -- 포인트 테이블에 새로운 레코드 삽입
    INSERT INTO bb_point (MEMBER_NUM, POINT_PLUS, POINT_CONTENT)
    VALUES (
        NEW.MEMBER_NUM, -- 회원 번호
        NEW.PAYMENT_AMOUNT, -- 결제 금액을 포인트로 사용
        CONCAT('+', NEW.PAYMENT_AMOUNT,' point 충전') -- 포인트 내용에 결제 금액 추가
    );
END;

CREATE TRIGGER after_order_detail_insert
AFTER INSERT ON BB_ORDER_DETAIL
FOR EACH ROW
begin
	DECLARE MEMBER_NUM INT;
    DECLARE TOTAL_PRICE INT;

    -- MEMBER_NUM을 주문 테이블에서 가져오기
    SELECT O.MEMBER_NUM INTO MEMBER_NUM
    FROM BB_ORDER O
    WHERE O.ORDER_NUM = NEW.ORDER_NUM;

    -- MEMBER_NUM이 NULL이 아닌지 확인
    IF MEMBER_NUM IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'MEMBER_NUM is NULL. Check the ORDER_NUM.';
    END IF;

   -- 가격과 수량을 합산하여 총 가격 계산
    SELECT SUM(p.PRODUCT_PRICE * d.ORDER_QUANTITY) INTO TOTAL_PRICE
    FROM BB_ORDER_DETAIL d
    JOIN BB_PRODUCT p ON d.PRODUCT_NUM = p.PRODUCT_NUM
    WHERE d.ORDER_NUM = NEW.ORDER_NUM;

    -- 포인트 차감
    INSERT INTO BB_POINT (MEMBER_NUM, POINT_MINUS, POINT_CONTENT)
    VALUES(
        MEMBER_NUM,
        TOTAL_PRICE,  -- 총 가격을 사용하여 포인트 차감
        CONCAT('-', TOTAL_PRICE, ' 상품 구매')  -- 포인트 내용
    );
END;

CREATE TRIGGER after_order_update
AFTER UPDATE ON BB_ORDER
FOR EACH ROW
BEGIN 
    DECLARE TOTAL_PRICE INT;

    IF NEW.ADMIN_CHECKED = 'C' AND OLD.ADMIN_CHECKED != 'C' THEN
        -- 가격과 수량을 합산하여 총 가격 계산
        SELECT SUM(p.PRODUCT_PRICE * d.ORDER_QUANTITY) INTO TOTAL_PRICE
        FROM BB_ORDER_DETAIL d
        JOIN BB_PRODUCT p ON d.PRODUCT_NUM = p.PRODUCT_NUM
        WHERE d.ORDER_NUM = NEW.ORDER_NUM;

        -- 포인트 테이블에 포인트 추가 기록
        INSERT INTO BB_POINT (MEMBER_NUM, POINT_PLUS, POINT_CONTENT)
        VALUES (
            NEW.MEMBER_NUM,  -- 회원 번호
            TOTAL_PRICE,  -- 총 가격을 포인트로 사용
            CONCAT('+', TOTAL_PRICE, ' 상품 취소')  -- 포인트 내용
        );
    END IF;
END;


//

DELIMITER ;