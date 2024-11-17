package com.bungeobbang.app.biz.reply;

import java.util.ArrayList;

public interface ReplyService {
	ArrayList<ReplyDTO> selectAll(ReplyDTO replyDTO);
	ReplyDTO selectOne(ReplyDTO replyDTO);
	boolean insert(ReplyDTO replyDTO);
	boolean update(ReplyDTO replyDTO);
	boolean delete(ReplyDTO replyDTO);
}
