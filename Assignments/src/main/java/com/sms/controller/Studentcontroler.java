package com.sms.controller;

import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;

import com.sms.dto.StudentDto;
import com.sms.service.StudentService;
import com.sms.service.StudentServiceImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/StudentController")
public class StudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	StudentService studentService = new StudentServiceImpl();
	private final static String STUDENT_LIST_PAGE="studentlist.jsp";
	private final static String STUDENT_CREATE_PAGE="studentform.jsp";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getParameter("actions");
		String forward="";
		if(action.equalsIgnoreCase("student_list"))
		{
			forward=STUDENT_LIST_PAGE;
			request.setAttribute("students", studentService.getAllStudentInfo());
		} else if(action.equalsIgnoreCase("add_new")) {
			forward= STUDENT_CREATE_PAGE;
		} else if(action.equalsIgnoreCase("edit")) {
			int id=Integer.parseInt(request.getParameter("id"));
			request.setAttribute("student", studentService.getStudentById(id));
			forward=STUDENT_CREATE_PAGE;
		} else if(action.equalsIgnoreCase("delete")) {
			int id=Integer.parseInt(request.getParameter("id"));
			
			studentService.deleteStudentInfo(id);
			request.setAttribute("students", studentService.getAllStudentInfo());
			forward = STUDENT_LIST_PAGE;
		}
		RequestDispatcher rd=request.getRequestDispatcher(forward);
		rd.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StudentDto studentDto = new StudentDto();
		studentDto.setName(request.getParameter("name"));
		studentDto.setCollege(request.getParameter("college"));
		studentDto.setEmail(request.getParameter("email"));
		studentDto.setGender(request.getParameter("gender"));
		studentDto.setRoll(Integer.parseInt(request.getParameter("roll")));
//		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			Date dob = dateFormat.parse(request.getParameter("dob"));
//			studentDto.(dob);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		String[] department=request.getParameterValues("departments");
		String depart="";
		for (String d: department)
		{
			depart = depart + d + "/";
		}
		studentDto.setDepartment(depart);

		String studentId=request.getParameter("id");
		if(studentId==null || studentId.isEmpty()) {
			studentService.saveStudentInfo(studentDto);
		} else {
			int id=Integer.parseInt(studentId);
			studentDto.setId(id);
			studentService.updateStudentInfo(studentDto);
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(STUDENT_LIST_PAGE);
		request.setAttribute("students", studentService.getAllStudentInfo());
		rd.forward(request, response);
	}

}