import java.util.ArrayList;

public class BetCode {

    //红球
    private ArrayList<Integer> redBalls;

    //蓝球
    private ArrayList<Integer> blueBalls;

    //倍数
    private int multiple;

    //胆码
    private ArrayList<Integer> danBalls;

    //拖码
    private ArrayList<Integer> tuoBalls;

    public ArrayList<Integer> getDanBalls() {
        return danBalls;
    }

    public void setDanBalls(ArrayList<Integer> danBalls) {
        this.danBalls = danBalls;
    }

    public ArrayList<Integer> getTuoBalls() {
        return tuoBalls;
    }

    public void setTuoBalls(ArrayList<Integer> tuoBalls) {
        this.tuoBalls = tuoBalls;
    }

    public ArrayList<Integer> getRedBalls() {
        return redBalls;
    }

    public void setRedBalls(ArrayList<Integer> redBalls) {
        this.redBalls = redBalls;
    }

    public ArrayList<Integer> getBlueBalls() {
        return blueBalls;
    }

    public void setBlueBalls(ArrayList<Integer> blueBalls) {
        this.blueBalls = blueBalls;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public void chaidan (String str) {
        Request request = new Request();
        ArrayList<BetCode> betCodeArrayList = request.transferStr(str);
        ArrayList<String> chaiDanBetCode = new ArrayList<String>();

        for (int i = 0; i < betCodeArrayList.size(); i++) {
            //调用拆复式或拆胆拖方法
            if (betCodeArrayList.get(i).getRedBalls() != null) {
                chaiFuShi(betCodeArrayList.get(i), chaiDanBetCode);
            } else {
                chaiDantuo(betCodeArrayList.get(i), chaiDanBetCode);
            }
        }
    }

    public void chaiFuShi(BetCode betCode, ArrayList<String> chaiDanBetCode) {
        ArrayList<int[]> redCodeCombination = new ArrayList<int[]>();
        ArrayList<int[]> blueCodeCombination = new ArrayList<int[]>();
        //拆分红球排列组合
        redCodeCombination = Util.combinationSelect(redCodeCombination, betCode.getRedBalls(),
                0, new int[6], 0);
        //拆分蓝球排列组合
        blueCodeCombination = Util.combinationSelect(blueCodeCombination, betCode.getBlueBalls(),
                0, new int[1], 0);

        StringBuilder sb = null;
        for(int[] i : redCodeCombination) {
            for (int[] j : blueCodeCombination) {
                sb = new StringBuilder();
                //拼接红球
                for (int k = 0; k < i.length; k++) {
                    if (i[k] < 10) {
                        sb.append("0");
                    }
                    sb.append(i[k]);
                    sb.append(",");
                }
                //删除最后一个逗号
                sb.deleteCharAt(sb.length() - 1);
                //增加竖线
                sb.append("|");
                //拼接蓝球
                for (int l = 0; l < j.length; l++) {
                    if (j[l] < 10) {
                        sb.append(j[l]);
                    }
                    sb.append(j[l]);
                    sb.append(",");
                }
                //删除最后一个逗号
                sb.deleteCharAt(sb.length() - 1);

                chaiDanBetCode.add(sb.toString());
                System.out.println(sb.toString());
            }
        }
    }

    public void chaiDantuo(BetCode betCode, ArrayList<String> chaiDanBetCode) {

    }
}
