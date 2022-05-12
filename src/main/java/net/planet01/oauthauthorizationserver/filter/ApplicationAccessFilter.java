package net.planet01.oauthauthorizationserver.filter;

import net.planet01.oauthauthorizationserver.model.entity.User;
import net.planet01.oauthauthorizationserver.model.response.ErrorResponse;
import net.planet01.oauthauthorizationserver.security.CustomUserDetailService;
import net.planet01.oauthauthorizationserver.service.UserService;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApplicationAccessFilter implements Filter {

    private CustomUserDetailService userService;
    private static final String APPLICATION_KEY_DOP = "1ba845917720494e94a1aa2cb35dc00a";
    private static final String APPLICATION_KEY_BACKOFFICE = "3f64c10c291342189cd3cba41ddf867e";


    public ApplicationAccessFilter(CustomUserDetailService userService){
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        User user =  userService.loadUserByUsername(servletRequest.getParameter("username")).getUser();
        String application = servletRequest.getParameter("application_key");
        if(user.isAdmin()){
            if(!application.equals(APPLICATION_KEY_BACKOFFICE)){
                byte[] responseToSend = restResponseBytes(new ErrorResponse(403,"You're not authorized to access this application"));
                ((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
                ((HttpServletResponse) servletResponse).setStatus(403);
                servletResponse.getOutputStream().write(responseToSend);
                return;
            }
        } else {
            if(!application.equals(APPLICATION_KEY_DOP)){
                byte[] responseToSend = restResponseBytes(new ErrorResponse(403,"You're not authorized to access this application"));
                ((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
                ((HttpServletResponse) servletResponse).setStatus(403);
                servletResponse.getOutputStream().write(responseToSend);
                return;
            }
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
        return serialized.getBytes();
    }
}
