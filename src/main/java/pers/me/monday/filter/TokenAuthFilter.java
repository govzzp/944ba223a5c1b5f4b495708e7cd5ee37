package pers.me.monday.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public abstract class TokenAuthFilter implements Filter {

    @Value("${front-end.url}")
    String url;

    @Autowired
    private RedisTemplate<String,String> redisTemplate02;

    abstract  String getType();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String id;


        //由于目前存在两个filter实体类比cors拦截器先执行的情况,所以只能加这么一段尴尬的代码
        if(req.getMethod().equals(HttpMethod.OPTIONS.toString())){
            res.setStatus(HttpStatus.NO_CONTENT.value());
            res.setHeader("Access-Control-Allow-Origin", url);
            res.setHeader("Access-Control-Allow-Credentials", "true");
            res.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
            res.setHeader("Access-Control-Max-Age", "86400");
            res.setHeader("Access-Control-Allow-Headers", "*");
            return;
        }

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
