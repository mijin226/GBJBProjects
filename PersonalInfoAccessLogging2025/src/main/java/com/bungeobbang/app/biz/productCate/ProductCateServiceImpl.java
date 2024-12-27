package com.bungeobbang.app.biz.productCate;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//productCateService 구현체
@Service("productCateService")
public class ProductCateServiceImpl implements ProductCateService {

	@Autowired
	private ProductCateDAO productCateDAO;
	
	@Override
	public ArrayList<ProductCateDTO> selectAll(ProductCateDTO productCateDTO) {
		return this.productCateDAO.selectAll(productCateDTO);
	}

	@Override
	public ProductCateDTO selectOne(ProductCateDTO productCateDTO) {
		return null;
	}

	@Override
	public boolean insert(ProductCateDTO productCateDTO) {
		
		return this.productCateDAO.insert(productCateDTO);
	}

	@Override
	public boolean update(ProductCateDTO productCateDTO) {
		return this.productCateDAO.update(productCateDTO);
	}

	@Override
	public boolean delete(ProductCateDTO productCateDTO) {
		return this.productCateDAO.delete(productCateDTO);
	}
	
}
