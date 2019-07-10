import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class Reader implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("begin___Observer");
        Map<String, FileType> fileMap = (Map<String, FileType>) arg;
        CustomClassLoader customClassLoader = new CustomClassLoader(this.getClass().getClassLoader());

        fileMap.forEach((n, fileType) -> {
            try {
                customClassLoader.findClass(fileType.getFile());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        try {
            Class clazz = customClassLoader.loadClass("test");
            Object o1 = clazz.getConstructor().newInstance();
            System.out.println("sssssssssssssssssss");
            Method method = clazz.getMethod("springd");
            method.invoke(o1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
