import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CustomClassLoader extends ClassLoader {


    public CustomClassLoader(ClassLoader parent) {
        super(parent);
    }

    public CustomClassLoader() {

    }


    protected Class<?> findClass(File file) throws ClassNotFoundException {
        byte[] classByte = null;
        classByte = readClassFile(file);
        if (classByte == null || classByte.length == 0) {
            throw new ClassNotFoundException("ClassNotFound:" + file.getName());
        }
        String name = file.getName().replaceAll(".class", "");
        return this.defineClass(name, classByte, 0, classByte.length);
    }

    private byte[] readClassFile(File file) throws ClassNotFoundException {
        if (!file.exists() && file.isDirectory()) {
            throw new ClassNotFoundException("ClassNotFound:" + file.getName());
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int available = fis.available();
            int bufferSize = Math.max(Math.min(1024, available), 256);
            ByteBuffer buf = ByteBuffer.allocate(bufferSize);
            byte[] bytes = null;
            FileChannel channel = fis.getChannel();
            while (channel.read(buf) > 0) {
                buf.flip();
                bytes = traslateArray(bytes, buf);
                buf.clear();
            }
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIOQuiet(fis);
        }
        return null;
    }

    public byte[] traslateArray(byte[] bytes, ByteBuffer buffer) {
        if (bytes == null) {
            bytes = new byte[0];
        }
        byte[] _array = null;
        if (buffer.hasArray()) {
            _array = new byte[buffer.limit()];
            System.arraycopy(buffer.array(), 0, _array, 0, _array.length);
        } else {
            _array = new byte[0];
        }

        byte[] _implyArray = new byte[bytes.length + _array.length];
        System.arraycopy(bytes, 0, _implyArray, 0, bytes.length);
        System.arraycopy(_array, 0, _implyArray, bytes.length, _array.length);
        bytes = _implyArray;
        return bytes;

    }

    public static void closeIOQuiet(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
