package model.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class JDBCUtil {

   private static final String driverName="oracle.jdbc.driver.OracleDriver";
   private static final String url="jdbc:oracle:thin:@localhost:1521:xe";
   private static final String userName="BREADFISH"; // sql 계정이름
   private static final String password="1234"; // 비밀번호
   
   public static Connection connect() {
      Connection conn=null;
      
      try {
         Class.forName(driverName);
      } catch (ClassNotFoundException e) {
         System.err.println("Class.forName(driverName) fail");
      } finally {
         System.out.println("드라이버를 메모리에 로드(load,적재)");
      }
      
      try {
         conn=DriverManager.getConnection(url, userName, password);
      } catch (SQLException e) {
         System.err.println("Connection fail");
      } finally {
         System.out.println("연결 객체 확보");
      }
      
      return conn;
   }
   
   public static boolean disconnect(Connection conn, PreparedStatement pstmt) {
      try {
    	 if(pstmt == null || conn == null) {
    		 System.err.println("pstmt, conn null");
    		 return false;
    	 }
         pstmt.close();
         conn.close();
      } catch (SQLException e) {
         System.err.println("pstmt, conn close fail");
         return false;
      } finally {
         System.out.println("연결 해제");
      }
      return true;
   }
   
}