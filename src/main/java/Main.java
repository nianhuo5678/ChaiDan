/**
 * Created by Simon on 2017/8/28.
 */
import java.io.*;
import java.util.ArrayList;

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
                String str1 = "{\"msg\":\"SUCC\",\"result\":[{\"betcode\":\"" + betCodeStr + "\",\"multiple\":1}],\"code\":0}";
                String str2 = request.getResponse(betCodeStr);
                BetCode betCode = new BetCode();
                ArrayList<BetCode> chaiDanBetCode1,chaiDanBetCode2;
                FileOperation file = new FileOperation();
                chaiDanBetCode1 = betCode.chaidan(str1);
                chaiDanBetCode2 = betCode.chaidan(str2);
//                file.writeFile(chaiDanBetCode1,"chaiDanBetCode1.txt");
//                file.writeFile(chaiDanBetCode2,"chaiDanBetCode2.txt");
                boolean result = betCode.compareChaiDan(chaiDanBetCode1,chaiDanBetCode2);
                sb.append(result);
                sb.append(",");
                sb.append(betCodeStr);
                sb.append("\n");
            }
            bw.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
