package pers.me.monday.controller.responseEntity;

public class DataWrapper<T> {
    public T data = null;
    public DataWrapper(T o){
        this.data=o;
    }
}
