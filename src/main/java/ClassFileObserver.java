import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ClassFileObserver extends Observable {

    private ObserverTask observerTask;

    public ClassFileObserver(String path) {
        this.observerTask = new ObserverTask(path, this);
    }

    public void sendChanged(Object objects) {
        System.out.println("*****************************sendChange"+objects);
        super.setChanged();
        super.notifyObservers(objects);
    }

    public void reset(String path) {
        if (observerTask != null && !observerTask.isStop) {
            observerTask.isStop = false;
            observerTask.interrupt();
            observerTask = null;
        }
        observerTask = new ObserverTask(path, this);
    }

    public void startObserver() {
        if (isStop()) {
            System.out.println("start---Observer_Listener");
            observerTask.isStop = false;
            observerTask.start();
        }
    }

    public boolean isStop() {
        return observerTask != null && !observerTask.isStop;
    }

    public void stopObserver() {
        System.out.println("stop-----Observer_Listener");
        observerTask.isStop = true;
    }


    public static class ObserverTask extends Thread {
        private String path;
        private boolean isStop = false;
        private ClassFileObserver observer;
        private static Map<String, FileType> fileMap;

        public ObserverTask(String path, ClassFileObserver obs) {
            this.path = path;
            this.observer = obs;
            this.fileMap = new ConcurrentHashMap<>();
        }


        @Override
        public void run() {
            while (!isStop && this.isAlive()) {
                synchronized (this) {
                    Map<String, FileType> map = getFileLastLoadTime();
                    if (map != null&&map.size()!=0) {
                        observer.sendChanged(map);
                    }
                    try {
                        System.out.println("Thread-Listener");
                        System.out.println("map" + map);
                        System.out.println("stringfile" + fileMap);
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        public Map<String, FileType> getFileLastLoadTime() {
            System.out.println("************************startGetFile");
            Map<String, FileType> stringFileTypeMap = new HashMap<>();
            if (this.path == null) {
                return null;
            }
            File f = new File(this.path);
            getFileLastTime(f, stringFileTypeMap);
            return stringFileTypeMap;
        }


        public void getFileLastTime(File file, Map<String, FileType> map) {
            if (file.exists() && file.isDirectory()) {
                for (File file1 : file.listFiles()) {
                    getFileLastTime(file1, map);
                }
            }
            if (file.isFile() && file.getName().endsWith("class")) {
                FileType fileType = fileMap.get(file.getName());
                long last= file.lastModified();
                if (fileType != null && fileType.getLastLoadTime() != last) {
                    map.put(file.getName(), new FileType(file, fileType.getLastLoadTime()));
                }
                fileMap.put(file.getName(), new FileType(file, file.lastModified()));
            }
        }
    }

}
