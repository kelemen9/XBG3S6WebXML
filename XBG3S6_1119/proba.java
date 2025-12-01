import org.json.simple.JSONObject;

public class proba {
    public static void main(String[] args) {
        JSONObject obj = new JSONObject();
        obj.put("nev", "Teszt");
        System.out.println(obj.toJSONString());
    }
}
