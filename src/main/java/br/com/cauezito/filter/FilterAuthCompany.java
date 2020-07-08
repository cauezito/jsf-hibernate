package br.com.cauezito.filter;

import java.io.IOException;

import javax.inject.Inject;
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

import br.com.cauezito.entity.Company;
import br.com.cauezito.util.JPAUtil;

@WebFilter(urlPatterns = { "/company/*" })
public class FilterAuthCompany implements Filter {

	@Inject
	private JPAUtil jpaUtil;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		jpaUtil.getEntityManager();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		Company company = (Company) session.getAttribute("companyOn");
		String url = req.getServletPath();
		if (!url.equalsIgnoreCase("company/company.xhtml") && company == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.xhtml");
			dispatcher.forward(request, response);
			return;
		} else {
			chain.doFilter(request, response);
		}
		
	}

}
