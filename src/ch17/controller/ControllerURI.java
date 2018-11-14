package ch17.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ControllerURI
 */
@WebServlet(urlPatterns = { "/ControllerURI", "*.do" }, initParams = {
		@WebInitParam(name = "propertyConfig", value = "commandURI.properties") })
public class ControllerURI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> commandMap = new HashMap<String, Object>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ControllerURI() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		// init param 에서 propertyConfig 의 값을 들고옴
		String props = config.getInitParameter("propertyConfig");
		String realFolder = "/property";
		ServletContext context = config.getServletContext();
		System.out.println("servletContext 환경설정 주소 : " + context);
		String realPath = context.getRealPath(realFolder) + "\\" + props; // 자바 경로는 \\ or / 이다 .

		Properties pr = new Properties();
		FileInputStream f = null;

		try {
			f = new FileInputStream(realPath);
			pr.load(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException e) {
				}
		}

		Iterator<?> keyIter = pr.keySet().iterator(); // 읽어 들인 파일에서 key 값 뽑아내기

		while (keyIter.hasNext()) {
			String command = (String) keyIter.next();
			String className = pr.getProperty(command);

			try {
				Class<?> commandClass = Class.forName(className);
				Object commandInstance = commandClass.newInstance();
				commandMap.put(command, commandInstance);

			} catch (ClassNotFoundException e) {
				throw new ServletException(e);
			} catch (InstantiationException e) {
				throw new ServletException(e);
			} catch (IllegalAccessException e) {
				throw new ServletException(e);
			}

		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		this.requestPro(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		this.requestPro(request, response);
	}

	private void requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String view = null;
		CommandProcess com = null; // CommandProcess 인터페이스 다중 상속을 하기위해서
		// web.xml 에서 컨트롤러가 command.properties를 이용할수 있도록 한다 .

		try {
			String command = request.getRequestURI(); // 들어오는 명령어
			if (command.indexOf(request.getContextPath()) == 0) { // 명령어가 들어 왔을때
				System.out.println("command : " + command + "   request.getContextPath()" + request.getContextPath());
				command = command.substring(request.getContextPath().length()); // subString 하면 진짜 명령어만 들어온다 . 즉 ,
																				// message.do 를 반환한다는것이다.
			} // 내 웹 어플리케이션 경로를 없엔다 , 끝트머리만 남김
			com = (CommandProcess) commandMap.get(command); // 맵에 들어 있던 명령어를 가지고 온다 즉 , 클레스가 된다는 말이다
			// 여기서 이 새끼가 맵에서 들고온 Object 형을 Propertise 로 가지고와서 이걸 사용하기 위해서 는 CommandProcess
			// interface를
			// 사용 해야한다 .
			if (com == null) {
				view = "/ch17/home.jsp";
			} else

				view = com.requestPro(request, response);

		} catch (Throwable e) {
			throw new ServletException(e);
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}

}
