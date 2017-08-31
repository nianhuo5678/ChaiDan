import sun.java2d.pipe.AATextRenderer;

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

    @Override
    public int hashCode() {
        return redBalls.hashCode() + blueBalls.hashCode() + multiple;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BetCode) {
            BetCode bc = (BetCode) obj;
            return redBalls.equals(bc.getRedBalls()) &&
                    blueBalls.equals(bc.getBlueBalls()) &&
                    multiple == bc.getMultiple();
        }
        return false;
    }

    public ArrayList<BetCode> chaidan (String str) {
        Request request = new Request();
        ArrayList<BetCode> betCodeArrayList = request.transferStr(str);
        ArrayList<BetCode> chaiDanBetCode = new ArrayList<BetCode>();

        for (int i = 0; i < betCodeArrayList.size(); i++) {
            //调用拆复式或拆胆拖方法
            if (betCodeArrayList.get(i).getRedBalls() != null) {
                chaiFuShi(betCodeArrayList.get(i), chaiDanBetCode);
            } else {
                chaiDantuo(betCodeArrayList.get(i), chaiDanBetCode);
            }
        }
        return chaiDanBetCode;
    }

    public void chaiFuShi(BetCode betCode, ArrayList<BetCode> chaiDanBetCode) {
        ArrayList<int[]> redCodeCombination = new ArrayList<int[]>();
        ArrayList<int[]> blueCodeCombination = new ArrayList<int[]>();
        //拆分红球排列组合
        redCodeCombination = Util.combinationSelect(redCodeCombination, betCode.getRedBalls(),
                0, new int[6], 0);
        //拆分蓝球排列组合
        blueCodeCombination = Util.combinationSelect(blueCodeCombination, betCode.getBlueBalls(),
                0, new int[1], 0);

        BetCode bc = null;
        for(int[] i : redCodeCombination) {
            for (int[] j : blueCodeCombination) {
                bc = new BetCode();
                //存入红球
                bc.setRedBalls(Util.ArrayToArrayList(i));
                //存入蓝球
                bc.setBlueBalls(Util.ArrayToArrayList(j));
                //存入倍数
                bc.setMultiple(betCode.getMultiple());
                Util.addWithMultiple(chaiDanBetCode, bc);
            }
        }
    }

    public void chaiDantuo(BetCode betCode, ArrayList<BetCode> chaiDanBetCode) {

        ArrayList<int[]> tuoBallCombination = new ArrayList<int[]>();
        BetCode bc = null;
        //拆分拖码排列组合
        tuoBallCombination = Util.combinationSelect(tuoBallCombination, betCode.getTuoBalls(),
                0, new int[6 - betCode.getDanBalls().size()], 0);
        //合并胆码、拖码以及蓝球
        for(int[] i : tuoBallCombination) {
            for(int j = 0; j < betCode.getBlueBalls().size(); j++) {
                bc = new BetCode();
                ArrayList<Integer> temp = new ArrayList<Integer>();
                ArrayList<Integer> tempB = new ArrayList<Integer>();
                //胆码拖码合并之后存入红球
                temp.addAll(Util.ArrayToArrayList(i));
                temp.addAll(betCode.getDanBalls());
                bc.setRedBalls(temp);
                //存入蓝球
                tempB.add(betCode.getBlueBalls().get(j));
                bc.setBlueBalls(tempB);
                //存入倍数
                bc.setMultiple(betCode.getMultiple());
                Util.addWithMultiple(chaiDanBetCode, bc);
            }
        }
    }

    public void compareChaiDan(ArrayList<BetCode> chaiDanBetCode1, ArrayList<BetCode> chaiDanBetCode2) {
        if (chaiDanBetCode1.size() != chaiDanBetCode2.size()) {
            System.out.println("Not the same!");
            System.out.println("chaiDanBetCode1:" + chaiDanBetCode1.size());
            System.out.println("chaiDanBetCode2:" + chaiDanBetCode2.size());
        } else {
            for (BetCode bc : chaiDanBetCode1) {
                if ( !chaiDanBetCode2.contains(bc) ) {
                    System.out.println("Not the same!");
                    System.out.println(bc.getRedBalls() + "|" + bc.getBlueBalls()
                            + "#" + bc.multiple
                            + " in chaiDanBetCode1, but not in chaiDanBetCode2");
                    return;
                }
            }
            System.out.println("The same!");
            System.out.println("chaiDanBetCode1 size: " + chaiDanBetCode1.size());
            System.out.println("chaiDanBetCode2 size: " + chaiDanBetCode2.size());
            totalMoney(chaiDanBetCode1);
        }
    }

    public void totalMoney(ArrayList<BetCode> chaiDanBetCode) {
        int totalMultiple = 0;
        for (BetCode bc : chaiDanBetCode) {
            totalMultiple += bc.getMultiple();
        }
        System.out.println("Total multiple is " + totalMultiple
            + " Total money is :" + totalMultiple * 2);
    }
}
