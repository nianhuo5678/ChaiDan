import sun.java2d.pipe.AATextRenderer;

import java.util.*;

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
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        //拼接红球
        Util.combineBalls(stringBuilder, redBalls);

        //拼接蓝球
        Util.combineBalls(stringBuilder, blueBalls);

        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        return redBalls.hashCode() + blueBalls.hashCode() + multiple;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BetCode) {
            BetCode bc = (BetCode) obj;
            boolean result = redBalls.equals(bc.getRedBalls()) &&
                    blueBalls.equals(bc.getBlueBalls()) &&
                    multiple == bc.getMultiple();
            return result;
        }
        return false;
    }

    /**
     * 调用拆胆拖方法或拆复式方法
     * @param str
     * @return 6+1拆单结果
     */
    public Map<Long, Integer> chaidan (String str) {
        long startTime = System.currentTimeMillis();
        Request request = new Request();
        ArrayList<BetCode> betCodeArrayList = request.transferStr(str);
        Map<Long, Integer> chaiDanBetCode = new TreeMap<>();
        for (int i = 0; i < betCodeArrayList.size(); i++) {
            //调用拆复式或拆胆拖方法
            if (betCodeArrayList.get(i).getRedBalls() != null) {
                chaiFuShi(betCodeArrayList.get(i), chaiDanBetCode);
            } else {
                chaiDantuo(betCodeArrayList.get(i), chaiDanBetCode);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Chaidan Time:" + (endTime - startTime));
        return chaiDanBetCode;
    }

    /**
     * 复式的拆单方法
     * @param betCode 将要被拆成6+1的betCode对象
     * @param chaiDanBetCode 存储拆单后6+1betCode的数据结构
     */
    public void chaiFuShi(BetCode betCode, Map<Long, Integer> chaiDanBetCode) {

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

    /**
     * 胆拖的拆单方法
     * @param betCode 将要被拆成6+1的betCode对象
     * @param chaiDanBetCode 存储拆单后6+1betCode的数据结构
     */
    public void chaiDantuo(BetCode betCode, Map<Long, Integer> chaiDanBetCode) {

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
                //胆码拖码合并之后存入红球
                temp.addAll(Util.ArrayToArrayList(i));
                temp.addAll(betCode.getDanBalls());
                Collections.sort(temp);
                bc.setRedBalls(temp);
                //存入蓝球
                if (betCode.getBlueBalls().size() == 1) {
                    bc.setBlueBalls(betCode.getBlueBalls());
                } else {
                    bc.setBlueBalls(new ArrayList<Integer>(betCode.getBlueBalls().subList(j,j+1)));
                }
                //存入倍数
                bc.setMultiple(betCode.getMultiple());
                Util.addWithMultiple(chaiDanBetCode, bc);
            }
        }
    }

    /**
     * 比较两种方式的拆单结果
     * @param chaiDanBetCode1 方法1的拆单结果
     * @param chaiDanBetCode2 方法2的拆单结果
     * @return 比较结果
     */
    public boolean compareChaiDan(Map<Long, Integer> chaiDanBetCode1, Map<Long, Integer> chaiDanBetCode2) {
        long startTime = System.currentTimeMillis();
        if (chaiDanBetCode1.size() != chaiDanBetCode2.size()) {
            System.out.println("Not the same!");
            System.out.println("chaiDanBetCode1:" + chaiDanBetCode1.size());
            System.out.println("chaiDanBetCode2:" + chaiDanBetCode2.size());
            return false;
        } else {
//            Iterator<Map.Entry<Long, Integer>> iterator = chaiDanBetCode1.entrySet().iterator();
            Iterator<Map.Entry<Long, Integer>> iterator1 = chaiDanBetCode1.entrySet().iterator();
            Iterator<Map.Entry<Long, Integer>> iterator2 = chaiDanBetCode2.entrySet().iterator();
            while (iterator1.hasNext()) {
                Map.Entry entry1 = iterator1.next();
                Map.Entry entry2 = iterator2.next();
                if ( !entry1.equals(entry2) ) {
                    System.out.println("Not the same!");
                    System.out.println("chaiDanBetCode1:" + entry1.getKey() + " " + entry1.getValue());
                    System.out.println("chaiDanBetCode2:" + entry2.getKey() + " " + entry2.getValue());
                    return false;
                }
            }
//            while (iterator.hasNext()) {
//                Map.Entry entry = iterator.next();
//                Object key = entry.getKey();
//                Object value = entry.getValue();
//                //先查看key存不存在
//                if ( !chaiDanBetCode2.containsKey(key) ) {
//                    System.out.println("Not the same!");
//                    System.out.println(key + " in chaiDanBetCode1, but not in chaiDanBetCode2");
//                    return false;
//                } else {
//                    //再查看key对应的value(即倍数)是否相等
//                    if ( !chaiDanBetCode2.containsValue(value) ) {
//                        System.out.println("Not the same!");
//                        System.out.println("key: " + key + "in chaiDanBetCode1,value="
//                                + value + "but not in chaiDanBetCode2");
//                        return false;
//                    }
//                }
//            }
//            totalMoney(chaiDanBetCode1);
            long endTime = System.currentTimeMillis();
            System.out.println("compare Time: " + (endTime - startTime));
            return true;
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
