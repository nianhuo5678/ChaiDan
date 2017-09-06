/**
 * Created by Simon on 2017/8/28.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Request request = new Request();
        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        StringBuilder sb = null;
        File oldFile = new File("result.txt");
        if (oldFile.exists()) {
            oldFile.delete();
        }
        try {
            fileReader = new FileReader("betcode.txt");
            fileWriter = new FileWriter("result.txt");
            br = new BufferedReader(fileReader);
            bw = new BufferedWriter(fileWriter);
            sb = new StringBuilder();
            String str = null;
            int lines = 1;
            while ( (str =br.readLine()) != null) {
                System.out.println("Lines:" + lines);
                lines++;
                String betCodeStr = str;
//                String str1 = "{\"msg\":\"SUCC\",\"result\":[{\"betcode\":\"" + betCodeStr + "\",\"multiple\":1}],\"code\":0}";
//                String str2 = request.getResponse(betCodeStr);
                String str1 = "{\"msg\":\"SUCC\",\"result\":[{\"betcode\":\"" +
                        str.split("\t")[0] +
                        "\",\"multiple\":1}],\"code\":0}";
                String str2 = str.split("\t")[1];
                BetCode betCode = new BetCode();
//                ArrayList<BetCode> chaiDanBetCode1,chaiDanBetCode2;
                HashMap<String, Integer> chaiDanBetCode1, chaiDanBetCode2;
                FileOperation file = new FileOperation();
                chaiDanBetCode1 = betCode.chaidan(str1);
                chaiDanBetCode2 = betCode.chaidan(str2);
//                file.writeFile(chaiDanBetCode1,"chaiDanBetCode1.txt");
//                file.writeFile(chaiDanBetCode2,"chaiDanBetCode2.txt");
                boolean result = betCode.compareChaiDan(chaiDanBetCode1,chaiDanBetCode2);
                if (!result) {
                    break;
                }
                sb.append(result);
                sb.append("\t");
                sb.append(betCodeStr);
                sb.append("\n");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            bw.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fileReader.close();
                bw.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
