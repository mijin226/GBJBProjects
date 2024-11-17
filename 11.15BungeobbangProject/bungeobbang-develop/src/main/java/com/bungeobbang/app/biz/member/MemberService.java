package com.bungeobbang.app.biz.member;

import java.util.ArrayList;

// Member 인터페이스
public interface MemberService {
	ArrayList<MemberDTO> selectAll(MemberDTO memberDTO);
	MemberDTO selectOne(MemberDTO memberDTO);
	boolean insert(MemberDTO memberDTO);
	boolean update(MemberDTO memberDTO);
	boolean delete(MemberDTO memberDTO);
	
}
