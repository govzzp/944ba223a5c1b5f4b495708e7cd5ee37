package pers.me.monday.filter;

import javax.servlet.annotation.WebFilter;

//目前是每个student或teacher前缀的filter都会发起一波验证,未来可能会改为只针对特定需要验证的路径
@WebFilter(value="/v1/teacher/*")
class TeacherTokenAuthFilter extends TokenAuthFilter {
    @Override
    String getType() {
        return "teacher";
    }
}
