/**
 * Created by Simon on 2017/8/28.
 */
public class Main {
    public static void main(String[] args) {
        Request sednRequest = new Request();
        String url = "http://jmeter.apache.org/index.html";
        String res = sednRequest.getResponse(url);
        System.out.println(res);
    }
}
