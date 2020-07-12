package br.com.cauezito.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.cauezito.entity.Person;

@WebFilter(urlPatterns = {"/user/*"})
public class FilterAuthPerson implements Filter {


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		Person person = (Person) session.getAttribute("personOn");
		String url = req.getServletPath();
		if(!url.equalsIgnoreCase("user/user.xhtml") && person == null){
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.xhtml");
			dispatcher.forward(request, response);
			return;
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {}

}
