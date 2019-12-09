package pers.me.monday.controller.responseEntity;

import java.util.ArrayList;
import java.util.List;


public class ListDataWrapper<T> {
    public ListDataWrapper(ArrayList<T> list){
        this.data = list;
    }
    private List<T> data = null;
}
