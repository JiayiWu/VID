package com.vid.filter;

import com.vid.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * Created by song on 17-2-11.
 * <p>
 * 登录过滤器，判断用户是否登录
 */
@WebFilter(value = "/*")
public class LoginFilter implements Filter {

    /**
     * login对应的url
     */
    private static String loginURL = null;

    static {
        Properties properties = new Properties();

        try {
            InputStream inputStream = LoginFilter.class.getClassLoader()
                    .getResourceAsStream("url.properties");

            properties.load(inputStream);

            loginURL = properties.getProperty("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
       /*do nothing*/
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 访问的url
        String url = request.getServletPath();

        // 判断url是否为 /login
        // 若是，不做处理
        // 否则，判断用户是否登录
//        if (!loginURL.equals(url)) {
//            HttpSession session = request.getSession(false);

            // TODO 删除桩数据
            injectUser(request);

//            if (session == null || session.getAttribute("user") == null) {
//                PrintWriter out = servletResponse.getWriter();
//
//                out.print("{\"status\":false,\"info\":\"用户未登录\",\"object\":null}");
//                return;
//            }
//        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        /*do nothing*/
    }

    private void injectUser(HttpServletRequest request) {
        HttpSession session = request.getSession();

        User user = new User("username", "password", "tom");
        session.setAttribute("user", user);
        session.setAttribute("username", "username");
    }
}