package com.bungeobbang.app.biz.like;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class LikeDTO {
	private int likeNum;	//좋아요 고유번호(PK)
	private int memberNum;	//회원 고유번호(FK)
	private int boardNum;	//게시물 고유번호(FK)
	private String condition;
	private int cnt;
}
