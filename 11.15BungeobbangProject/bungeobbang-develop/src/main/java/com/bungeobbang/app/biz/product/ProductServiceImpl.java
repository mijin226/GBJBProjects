package com.bungeobbang.app.biz.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//productService 구현체
@Service("productService")
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDAO productDAO;
	
    private void applyDefaultValues(ProductDTO productDTO) {
        if (productDTO.getCondition() == null || productDTO.getCondition().isEmpty()) {
            productDTO.setCondition("ALL");  // 기본 조회 조건 설정
        }
        if (productDTO.getFilterList() == null) {
            productDTO.setFilterList(new HashMap<>());  // 빈 필터 설정
        }
        if (productDTO.getStartNum() == 0) {
            productDTO.setStartNum(1);  // 기본 시작 번호 설정
        }
        if (productDTO.getEndNum() == 0) {
            productDTO.setEndNum(6);  // 기본 페이지 끝 번호 설정
        }
    }

	@Override
	public ArrayList<ProductDTO> selectAll(ProductDTO productDTO) {
		applyDefaultValues(productDTO);
		return (ArrayList<ProductDTO>) this.productDAO.selectAll(productDTO);
	}

	@Override
	//명시적 Null처리 위한 Optional 사용
	public Optional<ProductDTO> selectOne(ProductDTO productDTO) {
		applyDefaultValues(productDTO);
	    return Optional.ofNullable(productDAO.selectOne(productDTO));
	}
	

	@Override
	public boolean insert(ProductDTO productDTO) {
		return this.productDAO.insert(productDTO);
	}

	@Override
	public boolean update(ProductDTO productDTO) {
		return false;
	}

	@Override
	public boolean delete(ProductDTO productDTO) {
		return false;
	}
	
}
