package com.bungeobbang.app.biz.product;

import java.util.ArrayList;
import java.util.Optional;

public interface ProductService {
	ArrayList<ProductDTO> selectAll(ProductDTO productDTO);
	Optional<ProductDTO> selectOne(ProductDTO productDTO);
	boolean insert(ProductDTO productDTO);
	boolean update(ProductDTO productDTO);
	boolean delete(ProductDTO productDTO);
}