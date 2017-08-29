/**
 * Created by Simon on 2017/8/28.
 */
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Request request = new Request();
        String url = "http://3c.163.com/api/shoppingCart/getShoppingCartInfo.do";
        String res = request.getResponse(url);
//        String str = "{\"msg\":\"SUCC\",\"result\":[{\"betcode\":\"01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16|01\",\"multiple\":1}],\"code\":0}";
        String str ="{\"msg\":\"SUCC\",\"result\":[{\"betcode\":\"01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16|01\",\"multiple\":1},{\"betcode\":\"17$01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16|01\",\"multiple\":1}],\"code\":0}";
        BetCode betCode = new BetCode();
        betCode.chaidan(str);

    }
}
