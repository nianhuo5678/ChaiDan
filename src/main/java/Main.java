/**
 * Created by Simon on 2017/8/28.
 */
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Request request = new Request();
        String url = "http://3c.163.com/api/shoppingCart/getShoppingCartInfo.do";
        String res = request.getResponse(url);
        BetCode betCode = new BetCode();
        ArrayList<BetCode> chaiDanBetCode1,chaiDanBetCode2;
        FileOperation file = new FileOperation();

        String str1 = "{\"msg\":\"SUCC\",\"result\":[{\"betcode\":\"01,02,03,04,05,06,07,08,09,10|01\",\"multiple\":100}],\"code\":0}";
        String str2 = "{\"msg\":\"SUCC\",\"result\":[{\"betcode\":\"01,02,03,04,05,06,07,08,09,10|01\",\"multiple\":24},{\"betcode\":\"01,02,03,04,05,06,07,08,09,10|01\",\"multiple\":25},{\"betcode\":\"01,02,03,04,05,06,07,08,09,10|01\",\"multiple\":25},{\"betcode\":\"01,02,03,04,05,06,07,08,09,10|01\",\"multiple\":25},{\"betcode\":\"01,02,03,04,05,06,07,08,09,10|01\",\"multiple\":1}],\"code\":0}";
        chaiDanBetCode1 = betCode.chaidan(str1);
        chaiDanBetCode2 = betCode.chaidan(str2);
        file.writeFile(chaiDanBetCode1,"chaiDanBetCode1.txt");
        file.writeFile(chaiDanBetCode2,"chaiDanBetCode2.txt");
        betCode.compareChaiDan(chaiDanBetCode1,chaiDanBetCode2);

    }
}
