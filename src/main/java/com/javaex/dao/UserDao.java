package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public void getConnection() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/web_db";
			conn = DriverManager.getConnection(url, "web", "web");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}// getConnection()

	public void close() {

		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}// close()

	public int userInsert(UserVo userVo) throws SQLException {

		int count = -1;

		this.getConnection();

		// 3. SQL문 준비 / 바인딩 / 실행

		// SQL문 준비
		String query = "";
		query += " insert into users ";
		query += " values(null, ?, ?, ?, ?) ";

		// 바인딩
		pstmt = conn.prepareStatement(query);

		pstmt.setString(1, userVo.getId());
		pstmt.setString(2, userVo.getPassword());
		pstmt.setString(3, userVo.getName());
		pstmt.setString(4, userVo.getGender());

		// 실행
		count = pstmt.executeUpdate();

		// 4.결과처리
		System.out.println(count + "건 등록되었습니다.");

		// 5. 자원정리
		this.close();

		return count;

	}

	public UserVo selectUserByIdPw(UserVo userVo) {

		UserVo authUser = null;

		this.getConnection();

		try {

			// SQL문 준비
			String query = "";
			query += " select no, name, id, password ";
			query += " from users ";
			query += " where id=? and password=?";

			// 바인딩
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPassword());

			// 실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String id = rs.getString("id");
				String password = rs.getString("password");

				authUser = new UserVo();
				authUser.setNo(no);
				authUser.setName(name);
				authUser.setId(id);
				authUser.setPassword(password);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return authUser;
	}

	public void userUpdate(UserVo userVo) {

		int count = -1;

		this.getConnection();

		try {

			String query = "";
			query += " update user set name=?, password=?, gender=? ";
			query += " where no=? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, userVo.getName());
			pstmt.setString(2, userVo.getPassword());
			pstmt.setString(3, userVo.getGender());
			pstmt.setInt(4, userVo.getNo());

			System.out.println("usldfhnaskj");

			count = pstmt.executeUpdate();

			System.out.println(count + "건 수정되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		this.close();

	}

}
