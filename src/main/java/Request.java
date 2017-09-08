

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.net.URI;
import java.net.URISyntaxException;

public class Request {
    CloseableHttpClient httpClient = null;

    public Request() {
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
    }

    public String getResponse(String betCode) {

        URI uri = null;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("10.110.42.39")
                    .setPath("/info/listSubBetCodeInfo.do")
                    .setParameter("lotteryId","1")
                    .setParameter("betCode",betCode)
                    .setParameter("multiple","1")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse httpResponse = null;
        String responseStr=null;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            responseStr = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return responseStr;
    }

    public ArrayList<BetCode> transferStr(String jsonStr) {
        JSONObject successObject = JSONObject.fromObject(jsonStr).getJSONObject("result").getJSONObject("success");
        JSONArray betCodes = successObject.getJSONArray("list");
        ArrayList<BetCode> betCodeArrayList = new ArrayList<BetCode>();
        for (int i = 0; i < betCodes.size(); i++) {
            String betCodeStr = betCodes.getJSONObject(i).getString("code");
            int multiple = Integer.parseInt(betCodes.getJSONObject(i).getString("multiple"));
            //拆分多个行注
            String redBallStr,blueBallStr;
            if (betCodeStr.contains(";")) {
                String[] bcList = betCodeStr.split(";");
                for (String str : bcList) {
                    redBallStr = str.split("\\|")[0];
                    blueBallStr = str.split("\\|")[1];
                    storeBetCode(betCodeArrayList, str, redBallStr, blueBallStr, multiple);
                }
            } else {
                redBallStr = betCodeStr.split("\\|")[0];
                blueBallStr = betCodeStr.split("\\|")[1];
                storeBetCode(betCodeArrayList, betCodeStr, redBallStr, blueBallStr, multiple);
            }
        }
        return betCodeArrayList;
    }

    private void storeBetCode(ArrayList<BetCode> betCodeArrayList, String betCodeStr,
                              String redBallStr, String blueBallStr, int multiple) {
        BetCode betCode = new BetCode();
        betCode.setMultiple(multiple);
        //区分胆拖和复式
        if(betCodeStr.contains("$")) {
            ArrayList<Integer> danBalls = Util.StringToIntArray(betCodeStr.
                    split("\\|")[0].split("\\$")[0]);
            ArrayList<Integer> tuoBalls = Util.StringToIntArray(betCodeStr.
                    split("\\|")[0].split("\\$")[1]);
            ArrayList<Integer> blueBalls = Util.StringToIntArray(betCodeStr.split("\\|")[1]);
            betCode.setDanBalls(danBalls);
            betCode.setTuoBalls(tuoBalls);
            betCode.setBlueBalls(blueBalls);
            betCodeArrayList.add(betCode);
        } else {
            ArrayList<Integer> redBalls = Util.StringToIntArray(redBallStr);
            ArrayList<Integer> blueBalls = Util.StringToIntArray(blueBallStr);
            betCode.setRedBalls(redBalls);
            betCode.setBlueBalls(blueBalls);
            betCodeArrayList.add(betCode);
        }
    }
}
