import java.io.File;

public class ClassHotLoader {
    public static ClassHotLoader intacnse = null;
    private CustomClassLoader customClassLoader;


    public static ClassHotLoader getInstance() {
        if (intacnse == null) {
            synchronized (ClassHotLoader.class) {
                if (intacnse == null) {
                    intacnse = new ClassHotLoader();
                }
            }
        }
        return intacnse;
    }

    public Class<?> loadClass(File file) throws ClassNotFoundException {
        synchronized (this){
            customClassLoader=new CustomClassLoader();
            System.out.println("getFindClass_:"+file.getName());
            Class<?> findClass=customClassLoader.findClass(file);
            if(findClass!=null){
                return findClass;
            }
        }
        System.out.println("loadClass:"+file.getName());
        return customClassLoader.loadClass(file.getName());
    }

}
