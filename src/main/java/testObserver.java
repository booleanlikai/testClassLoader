import java.io.File;
import java.net.URL;

public class testObserver {
    public static void main(String[] args) {
        testObserver testObserver = new testObserver();
        URL base = testObserver.getClass().getResource("");
        String path = base.getPath();
        Reader reader = new Reader();
        ClassFileObserver classFileObserver = new ClassFileObserver(path);
        classFileObserver.addObserver(reader);
        classFileObserver.startObserver();
    }

}
