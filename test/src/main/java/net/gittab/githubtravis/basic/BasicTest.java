package net.gittab.githubtravis.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * BasicTest.
 *
 * @author xiaohua zhou
 **/
public class BasicTest {

    public static void main(String[] args) {

        double c = 0.125d;
        double d = 0.124d;
        double e = 0.123d;

        double result = c - d;
        System.out.println (result);
        double result1 = d - e;
        System.out.println (result1);

        float x = 0.1256f;
        float y = 0.1255f;
        float z = 0.1254f;

        float result2 = x - y;
        System.out.println (result2);
        float result3 = y - z;
        System.out.println (result3);

        System.out.println (1.0 / 0);

        double a = 0.0;

        System.out.println (0.0 / 0.0);

        m1(1);

    }

    public static void m1(String s1){
        System.out.println("String " + s1);
    }

    public static void m1(Integer s1){
        System.out.println("Integer " + s1);
    }

    public static void m1(Double s1){
        System.out.println("Double " + s1);
    }

    public static void m1(double s1){
        System.out.println("double " + s1);
    }

    <T, Ali> String get(String string, T t, Ali ali) {

        return string;
    }

    static class Solution{

        static Map<Character, Character> CHAR_MAP = new HashMap<>(4);

        static {
            CHAR_MAP.put(')', '(');
            CHAR_MAP.put(']', '[');
            CHAR_MAP.put('}', '{');
        }

        private static boolean isValid(String str){

            if(str == null || str.isEmpty()){
                return false;
            }

            char c = str.charAt(0);

            if(CHAR_MAP.containsKey(c)){
                return false;
            }

            Stack<Character> stack = new Stack<>();
            stack.push(c);

            for(int i = 1; i < str.length(); i++){
                c = str.charAt(i);
                if(CHAR_MAP.containsValue(c)){
                    stack.push(c);
                }else{
                    char top = stack.isEmpty()? '#' : stack.pop();
                    if(top != CHAR_MAP.get(c)){
                        return false;
                    }
                }
            }

            return stack.isEmpty();
        }

        public static void main(String[] args) {
            String str = "((([[[]]]{{}}))))";
            System.out.println(isValid(str));
        }


    }


}
