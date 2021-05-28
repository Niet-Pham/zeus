package vn.tech.website.store.jsf.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.controller.frontend.AuthorizationFEController;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

public class AuthSocketFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthSocketFilter.class);
	private FilterConfig config = null;

	@Override
	public void init(FilterConfig filterConfig) {
		this.config = filterConfig;
		LOGGER.info("Initializing AuthSocketFilter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		LOGGER.info("RequestURI: " + req.getRequestURI());
		HttpSession session = req.getSession();
        AuthorizationController authAdmin = (session != null) ? (AuthorizationController) session
                .getAttribute("authorizationController") : null;
        AuthorizationFEController authUser = (session != null) ? (AuthorizationFEController) session
                .getAttribute("authorizationFEController") : null;
        boolean inPageAdmin = request.getParameterMap().get("be") != null;
		boolean allowAdmin = authAdmin != null && authAdmin.hasLogged() && inPageAdmin;
		boolean allowUser = authUser != null && authUser.hasLogged() && !inPageAdmin;

		if (allowAdmin || allowUser) {
			chain.doFilter(new AuthenticatedRequest(req, allowAdmin ? authAdmin.getAccountDto() : authUser.getAccountDto()), response);
		} else {
//			returnForbidden(res, "Forbidden");
			chain.doFilter(new AuthenticatedRequest(req, null), response);
		}
	}

	private void returnForbidden(HttpServletResponse response, String message)
			throws IOException {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
	}

	private static class AuthenticatedRequest extends HttpServletRequestWrapper {

		private AccountDto accountDto;

		public AuthenticatedRequest(HttpServletRequest request, AccountDto accountDto) {
			super(request);
			this.accountDto = accountDto;
		}

		@Override
		public Principal getUserPrincipal() {
			return () -> StringUtil.toJson(accountDto);
		}
	}

	@Override
	public void destroy() {
		LOGGER.info("Destroy AuthSocketFilter");
	}
}
