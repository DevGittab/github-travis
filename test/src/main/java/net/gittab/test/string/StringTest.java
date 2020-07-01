package net.gittab.test.string;

/**
 * StringTest.
 *
 * @author xiaohua zhou
 **/
public class StringTest {

    public static void main(String[] args) {
        String str1 = "test";
        String str2 = str1.intern();
        System.out.println(str1.hashCode() + "<----->" + str2.hashCode());
        System.out.println(str1 == str2);

        String str3 = new String("test1");
        String str4 = str3.intern();
        String str5 = "test1";
        System.out.println(str3.hashCode() + "<----->" + str4.hashCode());
        System.out.println(str3 == str5);
        System.out.println(str5 == str4);
    }
}
