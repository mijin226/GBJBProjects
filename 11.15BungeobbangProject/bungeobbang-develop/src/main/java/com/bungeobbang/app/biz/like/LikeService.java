package com.bungeobbang.app.biz.like;

import java.util.ArrayList;

public interface LikeService {
	ArrayList<LikeDTO> selectAll(LikeDTO likeDTO);
	LikeDTO selectOne(LikeDTO likeDTO);
	boolean insert(LikeDTO likeDTO);
	boolean update(LikeDTO likeDTO);
	boolean delete(LikeDTO likeDTO);
}
