import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class FileOperation {

    public void writeFile(ArrayList<BetCode> chaiDanBetCode, String fileName) {
        String path = fileName;
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        StringBuilder sb = new StringBuilder();
        try {
            fw = new FileWriter(path,true);
            bw = new BufferedWriter(fw);
            for (BetCode betCode : chaiDanBetCode) {
                sb.append(betCode.getRedBalls());
                sb.append("|");
                sb.append(betCode.getBlueBalls());
                sb.append("#");
                sb.append(betCode.getMultiple());
                sb.append("\n");
            }
            bw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //关闭
        try {
            bw.close();
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
