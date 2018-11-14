package ch17.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MemberDBBean;

public class ListProcessURI implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		MemberDBBean db = MemberDBBean.getInstance();
		request.setAttribute("members", db.getList());

		request.setAttribute("message", "리스트 출력");
		return "/ch17/list.jsp"; // ���� ���
	}

}
