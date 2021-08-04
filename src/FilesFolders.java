package hacktomatest;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

class FilesFolders {

    boolean fileCopy(String source, String destination) throws IOException {
        Files.copy(new File(source).toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return true;
    }

    boolean makeFolders(String path) throws IOException {
        return new File(path).mkdirs();
    }

    boolean directoryExist(String path) throws IOException {
        return new File(path).exists();
    }

    boolean deleteFile(String path) throws IOException {
        return new File(path).delete();
    }

    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    String read_file(String path) throws IOException {
        return readFile(path, StandardCharsets.UTF_8);
    }

    boolean writeFile(String path, String content) throws IOException {
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.write(content);
        writer.close();
        return true;
    }

}
