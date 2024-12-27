package com.bungeobbang.app.biz.reply;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//replyService 구현체
@Service("replyService")
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDAO replyDAO;
	
	@Override
	public ArrayList<ReplyDTO> selectAll(ReplyDTO replyDTO) {
		return (ArrayList<ReplyDTO>) this.replyDAO.selectAll(replyDTO);
	}

	@Override
	public ReplyDTO selectOne(ReplyDTO replyDTO) {
		return this.replyDAO.selectOne(replyDTO);
	}

	@Override
	public boolean insert(ReplyDTO replyDTO) {
		return this.replyDAO.insert(replyDTO);
	}

	@Override
	public boolean update(ReplyDTO replyDTO) {
		return this.replyDAO.update(replyDTO);
	}

	@Override
	public boolean delete(ReplyDTO replyDTO) {
		return this.replyDAO.delete(replyDTO);
	}

}
