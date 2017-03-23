/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

public class Pair<U,T> {
    private U first;
    private T second;
    public Pair(U first, T second){
        this.first=first;
        this.second=second;
    }
    
    public U getFirst (){
        return this.first;
    }
    
    public T getSecond (){
        return this.second;
    }
}
