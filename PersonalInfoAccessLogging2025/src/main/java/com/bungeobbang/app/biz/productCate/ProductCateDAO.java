package com.bungeobbang.app.biz.productCate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.bungeobbang.app.biz.common.JDBCUtil;

@Repository
public class ProductCateDAO {

	private final String INSERT = "INSERT INTO BB_PRODUCT_CATEGORY(PRODUCT_CATEGORY_NAME) VALUES (?)";
    private final String UPDATE = "UPDATE BB_PRODUCT_CATEGORY SET PRODUCT_CATEGORY_NAME = ? WHERE PRODUCT_CATEGORY_NUM = ?";
    private final String DELETE = "DELETE FROM BB_PRODUCT_CATEGORY WHERE PRODUCT_CATEGORY_NUM = ?";
    private final String SELECTALL = "SELECT PRODUCT_CATEGORY_NUM, PRODUCT_CATEGORY_NAME FROM BB_PRODUCT_CATEGORY "
    		+ "ORDER BY PRODUCT_CATEGORY_NUM ";
    
    public boolean insert(ProductCateDTO productCateDTO) {
        System.out.println("log: ProductCategory insert start");
        Connection conn = JDBCUtil.connect();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(INSERT);
            pstmt.setString(1, productCateDTO.getProductCategoryName()); 
            System.out.println("log: parameter getProductCateName : " + productCateDTO.getProductCategoryName());

            if (pstmt.executeUpdate() <= 0) {
                System.err.println("log: ProductCategory insert execute fail");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("log: ProductCategory insert SQLException fail");
            return false;
        } finally {
            if (!JDBCUtil.disconnect(conn, pstmt)) {
                System.err.println("log: ProductCategory insert disconnect fail");
                return false;
            }
            System.out.println("log: ProductCategory insert end");
        }
        System.out.println("log: ProductCategory insert true");
        return true;
    }
    
    public boolean update(ProductCateDTO productCateDTO) {
        System.out.println("log: ProductCategory update start");
        Connection conn = JDBCUtil.connect();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(UPDATE);
            pstmt.setString(1, productCateDTO.getProductCategoryName()); 
            pstmt.setInt(2, productCateDTO.getProductCategoryNum());     
            System.out.println("log: parameter getProductCateName : " + productCateDTO.getProductCategoryName());
            System.out.println("log: parameter getProductCateNum : " + productCateDTO.getProductCategoryNum());

            if (pstmt.executeUpdate() <= 0) {
                System.err.println("log: ProductCategory update execute fail");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("log: ProductCategory update SQLException fail");
            return false;
        } finally {
            if (!JDBCUtil.disconnect(conn, pstmt)) {
                System.err.println("log: ProductCategory update disconnect fail");
                return false;
            }
            System.out.println("log: ProductCategory update end");
        }
        System.out.println("log: ProductCategory update true");
        return true;
    }
    
    public boolean delete(ProductCateDTO productCateDTO) {
        System.out.println("log: ProductCategory delete start");
        Connection conn = JDBCUtil.connect();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(DELETE);
            pstmt.setInt(1, productCateDTO.getProductCategoryNum()); 
            System.out.println("log: parameter getProductCateNum : " + productCateDTO.getProductCategoryNum());

            if (pstmt.executeUpdate() <= 0) {
                System.err.println("log: ProductCategory delete execute fail");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("log: ProductCategory delete SQLException fail");
            return false;
        } finally {
            if (!JDBCUtil.disconnect(conn, pstmt)) {
                System.err.println("log: ProductCategory delete disconnect fail");
                return false;
            }
            System.out.println("log: ProductCategory delete end");
        }
        System.out.println("log: ProductCategory delete true");
        return true;
    }
    
    public ArrayList<ProductCateDTO> selectAll(ProductCateDTO productCateDTO) {
        System.out.println("log: ProductCategory selectAll start");
        ArrayList<ProductCateDTO> datas = new ArrayList<>();
        Connection conn = JDBCUtil.connect();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SELECTALL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ProductCateDTO data = new ProductCateDTO();
                data.setProductCategoryNum(rs.getInt("PRODUCT_CATEGORY_NUM")); 
                data.setProductCategoryName(rs.getString("PRODUCT_CATEGORY_NAME")); 
                datas.add(data); 
                System.out.print("| result " + data.getProductCategoryNum());
            }
            rs.close();
            System.out.println("end");
        } catch (SQLException e) {
            System.err.println("log: ProductCategory selectAll SQLException fail");
            datas.clear(); 
        } finally {
            if (!JDBCUtil.disconnect(conn, pstmt)) {
                System.err.println("log: ProductCategory selectAll disconnect fail");
                datas.clear(); 
            }
            System.out.println("log: ProductCategory selectAll end");
        }
        System.out.println("log: ProductCategory selectAll return datas");
        return datas;
    }

    private ProductCateDTO selectOne(ProductCateDTO productCateDTO) {
        ProductCateDTO data = null;
        return data;
    }
}
