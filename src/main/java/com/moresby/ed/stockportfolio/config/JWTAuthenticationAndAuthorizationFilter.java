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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JWTAuthenticationAndAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTService jwtService;
    private static final int INDEX_OF_BEARER_START_IN_TOKEN = 7;

    public JWTAuthenticationAndAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer")){
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

        UsernamePasswordAuthenticationToken authentication = getAuthentication(header);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String header){
        String jwtToken = header.substring(INDEX_OF_BEARER_START_IN_TOKEN);
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


