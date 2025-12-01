package XBG3S6JSON.JSONParseXBG3S6;

import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONWriteXBG3S6 {
    public static void main(String[] args) {
        JSONArray lessons = new JSONArray();
        lessons.add(createLesson("Adatkezelés XML-ben", "szerda", "8", "10", "A1/310", "Prof. Dr. Kovács László", "gazdaságinformatikus"));
        lessons.add(createLesson("Adatkezelés XML-ben", "szerda", "10", "12", "L/103", "Dr. Bednarik László", "gazdaságinformatikus"));
        lessons.add(createLesson("Vállalati információs rendszerek fejlesztése", "szerda", "14", "16", "L/103", "Dr. Sasvári Péter", "gazdaságinformatikus"));
        lessons.add(createLesson("Vállalati információs rendszerek fejlesztése", "szerda", "18", "20", "L/103", "Dr. Sasvári Péter", "gazdaságinformatikus"));

        JSONObject root = new JSONObject();
        root.put("ora", lessons);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("XBG3S6_orarend", root);

        fileWrite(jsonObject, "orarendXBG3S6.json");
        consoleWrite(jsonObject);
    }

    private static void fileWrite(JSONObject jsonObject, String fileName) {
        try(FileWriter writer = new FileWriter(fileName)){
            writer.write(indentJson(jsonObject.toJSONString()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void consoleWrite(JSONObject jsonObject) {
        System.out.println("JSON dokumentum tartalma:\n");
        JSONObject root = (JSONObject) jsonObject.get("XBG3S6_orarend");
        JSONArray lessons = (JSONArray) root.get("ora");
        for(int i=0; i<lessons.size(); i++) {
            JSONObject lesson = (JSONObject) lessons.get(i);
            JSONObject time = (JSONObject) lesson.get("idopont");
            System.out.println("Tárgy: "+lesson.get("targy"));
            System.out.println("Időpont: "+time.get("nap")+" "+time.get("tol")+"-"+time.get("ig"));
            System.out.println("Helyszín: "+lesson.get("helyszin"));
            System.out.println("Oktató: "+lesson.get("oktato"));
            System.out.println("Szak: "+lesson.get("szak")+"\n");
        }
    }

    private static String indentJson(String json) {
        String out = "";
        int indent = 0;
        for (int i = 0; i < json.length()-1; i++) {
            out += json.charAt(i);
            if (json.charAt(i) == ',') {
                out += "\n" + "  ".repeat(indent>0 ? indent : 0);
            } else if (json.charAt(i) == '{' | json.charAt(i) == '[') {
                indent++;
                out += "\n" + "  ".repeat(indent>0 ? indent : 0);
            }else if ((json.charAt(i+1) == '}' || json.charAt(i+1) == ']')) {
                indent--;
                out += "\n" + "  ".repeat(indent>0 ? indent : 0);
            }
        }
        out+=json.charAt(json.length()-1);
        return out;
    }

    private static JSONObject createLesson(String subject, String day, String from, String to, String place, String teacher, String major) {
        JSONObject lesson = new JSONObject();
        JSONObject time = new JSONObject();
        time.put("nap", day);
        time.put("tol", from);
        time.put("ig", to);
        lesson.put("targy", subject);
        lesson.put("idopont", time);
        lesson.put("helyszin", place);
        lesson.put("oktato", teacher);
        lesson.put("szak", major);
        return lesson;
    }
}
