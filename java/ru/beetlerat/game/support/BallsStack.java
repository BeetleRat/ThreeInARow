package ru.beetlerat.game.support;

import java.util.ArrayList;
import java.util.List;

public class BallsStack {
    private List<Integer> rows;
    private List<Integer> columns;
    private int size;

    public BallsStack(){
        size=0;
        rows=new ArrayList<>();
        columns=new ArrayList<>();
    }

    public void push(int row, int column){
        size++;
        rows.add(size-1,row);
        columns.add(size-1,column);
    }

    public int[] pop(){
        if(size>0){
            int responseMass[] = new int[2];
            responseMass[0]=rows.get(size-1);
            responseMass[1]=columns.get(size-1);
            rows.remove(size-1);
            columns.remove(size-1);
            size--;
            return responseMass;
        }
        return null;
    }

    public int[] showByIndex(int index){
        int responseMass[] = new int[2];
        if(size==0||index<0||index>=size){
            return responseMass;
        }
        responseMass[0]=rows.get(index);
        responseMass[1]=columns.get(index);
        return responseMass;
    }
    public void removeByIndex(int index){
        if(size>0&&index>=0&&index<size){
            rows.remove(index);
            columns.remove(index);
            size--;
        }
    }

    public int[] showTop(){

        int responseMass[] = new int[2];
        responseMass[0]=-1;
        responseMass[1]=-1;
        if(size==0){
            return responseMass;
        }
        responseMass[0]=rows.get(size-1);
        responseMass[1]=columns.get(size-1);
        return responseMass;
    }

    public int getSize() {
        return size;
    }
}
