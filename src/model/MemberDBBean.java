package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDBBean {

	//MemberDBBean ���� ��ü ���� <- �� ���� ��ü�� �����ؼ� ����
	private static MemberDBBean instance = new MemberDBBean();
			
	//MemberDBBean ��ü�� �����ϴ� �޼ҵ�
	public static MemberDBBean getInstance() {
		
		return instance;
	}
	private MemberDBBean() {}
	
	public ArrayList<Member> getList(){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		ArrayList<Member> memberlist = null;
		Member bean = null;
		
		try {
			memberlist = new ArrayList<Member>();
			
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource)envCtx.lookup("jdbc/TestDB");
			
			conn = ds.getConnection();
			String sql = "select * from member";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				bean = new Member();
				bean.setId(rs.getString("id"));
				bean.setPasswd(rs.getString("passwd"));
				bean.setName(rs.getString("name"));
				bean.setReg_date(rs.getTimestamp("reg_date"));
				bean.setAddress(rs.getString("address"));
				bean.setTel(rs.getString("tel"));
				
				memberlist.add(bean);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(SQLException ex) {}
			if(pstmt != null) try {pstmt.close();} catch(SQLException ex) {}
			if(conn != null) try { conn.close(); } catch(SQLException ex) {}
			}
		
		
		
		return memberlist;
	}

}
	

