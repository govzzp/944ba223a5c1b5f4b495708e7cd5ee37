package pers.me.monday.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class TokenAuthFilter implements Filter {
    @Autowired
    private RedisTemplate<String,String> redisTemplate02;

    abstract  String getType();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String id = null;


        var UID = req.getHeader("UID");
        if(UID!=null){
            res.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }


        var header = req.getHeader("Authorization");
        if(header!=null){
            id = checkSession(header,getType());
            if(id!=null){
                var reqWrapper = new HeaderMapRequestWrapper(req);
                reqWrapper.addHeader("UID",id);
                chain.doFilter(reqWrapper, response);
            }
        }
        res.setStatus(HttpStatus.NOT_FOUND.value());
    }
    private String checkSession(String session,String type){
        var slice =  session.split(" ");
        if(redisTemplate02==null){
            System.out.println("RedisTemplate in TokenAuthFilter unAutowired !");
        }
        if(slice.length==2){
             var tokenSlice= slice[1].split("-");
             var id = tokenSlice[1];
             var session_id = tokenSlice[0];
             var session_id_real = redisTemplate02.opsForValue().get("session-"+type+":"+id);
             if(session_id.equals(session_id_real)){
                 return id;
             }
        }
        return null;
    }
}
