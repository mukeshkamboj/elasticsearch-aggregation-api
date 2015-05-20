
package es;

import java.util.ArrayList;
import java.util.List;

public class TempClass {

    private static class User{
        public String name;
        public long count;
    }
    
    
    public static void main(String[] args) {
       
        temp();
        
        System.out.println("end");
        
        
    }
    
    public static void temp(){
        List<User> tempList =new ArrayList<>();
        
        User temp=null;
        for (int i = 0; i < 140000; i++) {
            temp = new User();
            temp.name="userName"+i;
            temp.count=i;
            tempList.add(temp);
        }
        System.out.println(tempList.size());
    }
    
}
