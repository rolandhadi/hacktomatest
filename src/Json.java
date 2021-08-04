package hacktomatest;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("unchecked")
class Json {

    private JSONObject json_obj;
    private String json_text;
    private String json_file;

    Json(File new_json_file) throws IOException {

        FilesFolders file = new FilesFolders();
        json_file = new_json_file.getAbsolutePath();
        if (file.directoryExist(json_file)) {
            json_text = file.read_file(json_file);
        }
        else {
            json_text = "";
        }
        jsonParseText(json_text);

    }

    Json(String new_json_text) throws IOException {

        jsonParseText(new_json_text);

    }

    Json(JSONObject new_json_object) throws IOException {

        json_text = json_obj.toJSONString();

    }

    private boolean jsonParseText(String new_json_text) {

        JSONParser parser = new JSONParser();
        try {
            if (new_json_text.isEmpty()) {
                json_obj = new JSONObject();
            }
            else {
                Object obj = parser.parse(new_json_text);
                json_obj = (JSONObject) obj;
            }
        }
        catch (ParseException e) {
            new ErrorLogs().app_log(new StackTrace(e).error_text);
        }
        return true;

    }

    @SuppressWarnings("WeakerAccess")
    boolean saveAs(String new_json_file) throws IOException, ScriptException {

        json_file = new_json_file;
        return save();

    }

    @SuppressWarnings("WeakerAccess")
    boolean save() throws IOException, ScriptException {
        json_text = json_obj.toJSONString();
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scriptEngine = manager.getEngineByName("JavaScript");
        scriptEngine.put("jsonString", json_text);
        scriptEngine.eval("json_result = JSON.stringify(JSON.parse(jsonString), null, 2)");
        String prettyPrintedJson = (String) scriptEngine.get("json_result");
        new FilesFolders().writeFile(json_file, prettyPrintedJson);
        return true;
    }

    String get(String new_keys) throws IOException {

        return get(new_keys, json_obj);

    }

    private String get(String new_keys, JSONObject new_json_obj) throws IOException {

        String[] keys;
        keys = new_keys.split("\\.");
        String value = "";
        Integer k = -1;

        for (String key : keys) {
            k++;
            if (new_json_obj.get(key) instanceof JSONObject) {
                String new_set_key = glue(keys, '.', k + 1, keys.length);
                if (!new_set_key.isEmpty()) {
                    return get(new_set_key, (JSONObject) new_json_obj.get(key));
                }
                else {
                    JSONObject out_json_obj = (JSONObject) new_json_obj.get(key);
                    return out_json_obj.toJSONString();
                }

            }
            else if (new_json_obj.get(key) instanceof JSONArray) {
                return ((JSONArray) new_json_obj.get(key)).toJSONString();
            }
            else {
                return (String) new_json_obj.get(key);
            }
        }
        return "";
    }

    void set(String new_keys, String new_value) throws IOException {

        String[] keys;
        keys = new_keys.split("\\.");

        Integer k = 0;
        for (String key : keys) {
            if (k < keys.length - 1) {
                if (!json_obj.containsKey(keys[k])) {
                    String new_set_key = glue(keys, '.', k + 1, keys.length);
                    json_obj.put(keys[k], set_(new_set_key, new_value, new JSONObject()));
                    return;
                }
                else {
                    String new_set_key = glue(keys, '.', k + 1, keys.length);
                    JSONObject old_json_obj = (JSONObject) json_obj.get(keys[k]);
                    json_obj.remove(keys[k]);
                    json_obj.put(keys[k], set_(new_set_key, new_value, old_json_obj));
                    return;
                }
            }
            k++;
        }
    }

    private Object set_(String new_keys, String new_value, JSONObject new_json_obj) {
        String[] keys = new_keys.split("\\.");
        Integer k = 0;
        for (String key : keys) {
            if (k < keys.length - 1) {
                if (!new_json_obj.containsKey(keys[k])) {
                    String new_set_key = glue(keys, '.', k + 1, keys.length);
                    new_json_obj.put(keys[k], set_(new_set_key, new_value, new_json_obj));
                }
                else {
                    String new_set_key = glue(keys, '.', k + 1, keys.length);
                    JSONObject old_json_obj = (JSONObject) new_json_obj.get(keys[k]);
                    new_json_obj.remove(keys[k]);
                    new_json_obj.put(keys[k], set_(new_set_key, new_value, old_json_obj));
                }
                return new_json_obj;
            }
            else {
                new_json_obj.put(keys[k], new_value);
                return new_json_obj;
            }
        }
        return new_json_obj;
    }

    private String glue(String[] keys, Character glue, Integer start_from, Integer end_to) {
        String glued_string = "";
        for (Integer i = start_from;
             i < end_to;
             i++) {
            glued_string += keys[i] + glue;
        }
        if (glued_string.length() > 0 && glued_string.charAt(glued_string.length() - 1) == glue) {
            glued_string = glued_string.substring(0, glued_string.length() - 1);
        }
        return glued_string;
    }

}
