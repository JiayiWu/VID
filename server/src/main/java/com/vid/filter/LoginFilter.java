package com.vid.filter;

import com.vid.model.User;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Created by song on 17-2-11.
 * <p>
 * 登录过滤器，判断用户是否登录
 */
@WebFilter(value = "/*")
public class LoginFilter implements Filter {

    /**
     * 忽略的url列表
     */
    private List<String> ignoreURL;

    public void init(FilterConfig filterConfig) throws ServletException {
        Properties properties = new Properties();

        try {
            InputStream inputStream = LoginFilter.class.getClassLoader()
                    .getResourceAsStream("url.properties");
            properties.load(inputStream);

            StringTokenizer tokenizer = new StringTokenizer(properties.getProperty("ignore"));
            ignoreURL = new ArrayList<>(tokenizer.countTokens());

            while (tokenizer.hasMoreTokens()) {
                ignoreURL.add(tokenizer.nextToken());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println(request.getMethod());

        if (request.getMethod().equalsIgnoreCase("POST")) {
            // 访问的url
            String url = request.getServletPath();

            // 判断url是否需要忽略
            // 若是，不做处理
            // 否则，判断用户是否登录
            if (!ignoreURL.contains(url)) {
                HttpSession session = request.getSession(false);

                if (session == null || session.getAttribute("userID") == null) {
                    PrintWriter out = servletResponse.getWriter();

                    out.print("{\"status\":false,\"info\":\"用户未登录\",\"object\":null}");
                    return;
                }
            }
        }

        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Origin", "*");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        /*do nothing*/
    }
}
