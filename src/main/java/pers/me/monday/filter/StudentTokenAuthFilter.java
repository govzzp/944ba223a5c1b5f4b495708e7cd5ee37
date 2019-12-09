package pers.me.monday.filter;


import javax.servlet.annotation.WebFilter;


@WebFilter(value="/v1/student/*")
public class StudentTokenAuthFilter extends TokenAuthFilter {
    @Override
    String getType() {
        return "student";
    }
}

