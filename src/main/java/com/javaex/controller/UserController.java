package com.javaex.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 업무구분
		String action = request.getParameter("action");

		if ("joinform".equals(action)) {

			// 회원가입폼 불러오기
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");

		} else if ("join".equals(action)) {

			// 파라미터에서 꺼내기
			String id = request.getParameter("id");
			String pw = request.getParameter("pw");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");

			// 꺼낸 데이터를 vo로 묶어주기
			UserVo userVo = new UserVo(id, pw, name, gender);

			UserDao userDao = new UserDao();

			try {
				userDao.userInsert(userVo);
				WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if ("loginform".equals(action)) {

			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");

		} else if ("login".equals(action)) {

			String id = request.getParameter("id");
			String pw = request.getParameter("pw");

			UserVo userVo = new UserVo(id, pw);

			UserDao userDao = new UserDao();
			UserVo authUser = userDao.selectUserByIdPw(userVo);

			if (authUser != null) { // 로그인성공

				HttpSession session = request.getSession();
				session.setAttribute("authUser", authUser);

				WebUtil.redirect(request, response, "/mysite3/main");
			} else { // 로그인실패

				System.out.println("로그인실패");

			}

		}

		else {
			System.out.println("action값을 다시 확인해주세요");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
