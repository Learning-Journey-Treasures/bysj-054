package com.controller;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
 





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.entity.Admin;
 
import com.entity.Cun;
import com.entity.Xian;
import com.service.AdminService;
 
import com.service.CunService;
import com.service.XianService;
import com.util.PageUtil;
import com.util.UploadUtil;
 

@Controller
@RequestMapping("")
public class   AdminController {
	private static final int rows = 10;
	@Autowired
	private AdminService adminService;
	@Autowired
	private CunService cunService;
	@Autowired
	private XianService xianService;
	/**
	 * 管理员登录
	 * @return
	 */
	@RequestMapping("/adminlogin")
	public String adminlogin(Admin admin, HttpServletRequest request, HttpSession session) {
		 String username =request.getParameter("username");
		 String pwd =request.getParameter("pwd");
		 
		 String role =request.getParameter("role");
			String urlString=""; 		
//		1表示是管理员，2 是参与者，3是执行者
		 if(role.equals("1"))
		{	 
			 Admin admin2=adminService.adminlogin(username, pwd);
			 if (admin2!=null) 
			 {
				 request.setAttribute("msg", "登录成功!");
				session.setAttribute("username", username);
				session.setAttribute("pwd", pwd);
				session.setAttribute("role", "管理员");
				return "admin/Main";
			}
			 request.setAttribute("isFlag", "1");
			 request.setAttribute("msg", "用户名或密码错误!");
				 
		 }
		 if(role.equals("2"))
		 {
			 
			 Xian  xian=xianService.userlogin(username, pwd);
			 System.out.print(username);
			 System.out.print(pwd);
			 if (xian!=null) {
				 request.setAttribute("msg", "登录成功!"); 
					session.setAttribute("username", username);
					session.setAttribute("pwd", pwd);
					session.setAttribute("xm", xian.getFname());
					session.setAttribute("role", "县级");
					return "admin/Main";
			}
			 
			 request.setAttribute("isFlag", "1");
			 request.setAttribute("msg", "用户名或密码错误!");
			 
		}
		 if(role.equals("3"))
		 {
			 
			 Cun  cun=cunService.userlogin(username, pwd);
			 System.out.print(username);
			 System.out.print(pwd);
			 if (cun!=null) {
				 request.setAttribute("msg", "登录成功!"); 
					session.setAttribute("username", username);
					session.setAttribute("pwd", pwd);
					session.setAttribute("xm", cun.getFname());
					session.setAttribute("xj", cun.getXian());
					session.setAttribute("cun", cun.getCunname());
					session.setAttribute("role", "村级");
					return "admin/Main";
			}
			 
			 request.setAttribute("isFlag", "1");
			 request.setAttribute("msg", "用户名或密码错误!");
			 
		}
		 
	
			 	return "admin/Login";
	 
	}
 
	/**
	 * 退出
	 * @return
	 */ 
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("username");
	 
		return "admin/Login";
	}
	/**
 * 获取列表信息
 * @param request
 * @return
 */
	@RequestMapping("/adminList")
	public String adminList(HttpServletRequest request) {
		request.setAttribute("flag", 2);
		int page=1;
		if(request.getParameter("page")==null)
		{
			page=1;
		}
		else
		{
		page=Integer.parseInt(request.getParameter("page"));
		}
		request.setAttribute("page", page);
		request.setAttribute("adminList", adminService.getList(page, rows));
		request.setAttribute("pageHtml", PageUtil.getPageHtml(request,adminService.getTotal(), page, rows)); 

		return "/admin/admin_list";
 	}
	/**
 * 添加信息
 * @param request
 * @return
 */
	@RequestMapping("/adminAdd")
	public String adminAdd(Admin admin, HttpServletRequest request) {
			adminService.add(admin);
			return "redirect:adminList";
 	}
	/**
 * 更新信息
 * @param request
 * @return
 */
	@RequestMapping("/adminEdit")
	public String adminEdit(int id,  HttpServletRequest request) {
			request.setAttribute("flag", 2);
			request.setAttribute("admin", adminService.get(id));
		return "/admin/adminEdit";
 	}
	/**
 * 更新信息
 * @param request
 * @return
 */
	@RequestMapping("/adminUpdate")
	public String adminUpdate(Admin admin, HttpServletRequest request) {
			adminService.update(admin);
			return "redirect:adminList";
 	}
	/**
 * 修改密码
 * @param request
 * @return
 */
	@RequestMapping("/adminpwd")
	public String adminpwd(Admin admin, HttpServletRequest request, HttpSession session) {
		String pwd1=request.getParameter("pwd1").toString();
		String pwd2=request.getParameter("pwd2").toString();
		String pwd3=request.getParameter("pwd3").toString();
		String username= session.getAttribute("username").toString();
		String spwd= session.getAttribute("pwd").toString();
		
		if (pwd1.equals(spwd)) 
		{
				if (!pwd2.equals(pwd3)) 
			{
				request.setAttribute("msg", "2次输入的新密码不一致!");
			}
			else
			{
				admin.setUsername(username);
				admin.setPwd(pwd3);
				adminService.adminpwd(admin);
				request.setAttribute("msg", "操作成功!");
				return "/admin/adminpwd";
			} 
		} 
		else
		{
			request.setAttribute("msg", "旧密码错误!");
	    	return "/admin/adminpwd";
			
		} 
	
		return "/admin/adminpwd";
	}
	/**
 * 删除信息
 * @param request
 * @return
 */
	@RequestMapping("/adminDelete")
	public String adminDelete(int id, HttpServletRequest request) {
			adminService.delete(id);
			return "redirect:adminList";
 	}
}
