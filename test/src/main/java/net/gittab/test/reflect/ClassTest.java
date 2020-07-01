package net.gittab.test.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * ClassTest.
 *
 * @author xiaohua zhou
 **/
public class ClassTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        Class c1 = Class.forName("net.gittab.test.reflect.Employee");

        Class c2 = Employee.class;

        Employee employee = new Employee("test", "19", "basketball", 12, "1", 100);

        Class c3 = employee.getClass();

        if(c1 == c2 && c1 == c3){
            System.out.println("c1 c2 c3 是同一个对象");
            System.out.println(c1);
        }

        Employee employee1 = (Employee) c1.newInstance();
        employee1.sayHello();

        Constructor<Employee> constructor = c1.getConstructor(String.class, String.class, String.class);
        Employee employee2 = constructor.newInstance("test", "12", "test");
        employee2.sayHello();

        Field field = c1.getField("totalNum");
        System.out.println(Modifier.toString(field.getModifiers()));


        String modifiers = Modifier.toString(c1.getModifiers());
        System.out.println("modifiers is " + modifiers);
    }
}
