import java.io.File;

public class FileType {
    private File file;
    private long lastLoadTime;

    public FileType(File file) {
        this.file = file;
        this.lastLoadTime = -1;
    }

    public FileType(File file,long lastLoadTime) {
        this.file = file;
        this.lastLoadTime = lastLoadTime;
    }
    public FileType() {
        this.lastLoadTime = -1;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getLastLoadTime() {
        return lastLoadTime;
    }

    public void setLastLoadTime(long lastLoadTime) {
        this.lastLoadTime = lastLoadTime;
    }
}
