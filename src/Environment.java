package hacktomatest;


import java.io.*;
import java.util.Properties;

class Environment {

    private String env = "";
    private Properties property = new Properties();


    Environment(String env) {
        this.env = env;
    }

    void set(String key, String new_value) throws IOException {

        property.setProperty(key, new_value);
        property.store(new FileOutputStream(env), null);

    }

    String get(String key) throws IOException {
        property.load(new FileInputStream(env));
        return property.getProperty(key);
    }

}
