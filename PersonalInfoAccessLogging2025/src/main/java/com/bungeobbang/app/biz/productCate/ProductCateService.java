package com.bungeobbang.app.biz.productCate;

import java.util.ArrayList;

public interface ProductCateService {
	ArrayList<ProductCateDTO> selectAll(ProductCateDTO productCateDTO);
	ProductCateDTO selectOne(ProductCateDTO productCateDTO);
	boolean insert(ProductCateDTO productCateDTO);
	boolean update(ProductCateDTO productCateDTO);
	boolean delete(ProductCateDTO productCateDTO);
}
