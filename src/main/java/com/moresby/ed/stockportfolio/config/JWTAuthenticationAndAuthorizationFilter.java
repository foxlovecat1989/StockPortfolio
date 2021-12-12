package com.moresby.ed.stockportfolio.config;

import com.moresby.ed.stockportfolio.service.JWTService;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JWTAuthenticationAndAuthorizationFilter extends BasicAuthenticationFilter{

    private JWTService jwtService;

    public JWTAuthenticationAndAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length == 0){
            chain.doFilter(request, response);
            return;
        }

        Optional<Cookie> tokenCookieOpt =
                Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals("token"))
                        .findFirst();


        if(tokenCookieOpt.isEmpty()){
            chain.doFilter(request, response);
            return;
        }

        if(jwtService == null){
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext  =
                    WebApplicationContextUtils.getWebApplicationContext(servletContext);

            assert webApplicationContext != null;
            jwtService = webApplicationContext.getBean(JWTService.class);

        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(tokenCookieOpt.get().getValue());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String jwtToken){
        try{
            String payload = jwtService.validateToken(jwtToken);
            JsonParser parser = JsonParserFactory.getJsonParser();
            Map<String, Object> payloadMap = parser.parseMap(payload);
            String user = payloadMap.get("user").toString();
            String role = payloadMap.get("role").toString();
            List<GrantedAuthority> roles = new ArrayList<>();
            GrantedAuthority grantedAuthority = () -> "ROLE_" + role;
            roles.add(grantedAuthority);

            return new UsernamePasswordAuthenticationToken(user, null, roles);
        }
        catch (Exception e) {
            // token is invalid
            return null;
        }

    }
}
