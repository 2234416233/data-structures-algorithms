package cumt.tj.learn.offer.alibaba;

import java.util.*;

/**
 * Created by sky on 17-8-25.
 *
 *
 * 在快递公司干线运输的车辆使用中，存在着单边车和双边车的两种使用场景，
 * 例如 北京中心-杭州中心，两个分拨中心到彼此的单量对等，则可以开双边车（即同一辆车可以往返对开），
 * 而当两个中心的对发单量不对等时，则会采用单边车，
 * 并且双边车的成本是低于单边车的，即将两辆对开的单边车合并为一辆往返的双边车是能够节省运力成本的
 * 单边车优化原则：
 * 将单边车优化的规则进行可抽象为以下三种（A,B,C均表示分拨中心）：
 * 规则-1: A-B单边车，B-A单边车 优化方案：将A-B和B-A的两辆单边车合并为双边；
 * 规则-2: A-B单边车，B-C单边车，C-A单边车 优化方案：将A-B、B-C、C-A的三辆单边车优化为一辆环形往返车；
 * 规则-3: A-B单边车，C-A单边车，B、C同省 优化方案：当B、C同省，将A-B、C-A两辆单边优化为一辆环形往返
 * 问题如下：
 * 以某快递公司的实际单边车数据为例（线路ID编码;出分拨中心; 出分拨中心所在省;到达分拨中心;到达分拨中心所在省；车型；）
 * ，进行优化，优化的规则参照以上，并且优先级依次降低，合并的时候需要考虑车型（分为17.5m和9.6m两种）：
 * 1、相同车型才能进行合并；
 * 2、两辆同方向的9.6m可以与一辆17.5m的对开车型合并优化
 * 说明：优化输出结果按照规则分类，
 * 例如rule1： 2016120001+2016120002表示将单边车线路ID编码为2016120001和2016120002按照规则1合并优化
 */
public class KuaiDiYouHua {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<UnilateralLine> lineList = new ArrayList<UnilateralLine>();
        while (scanner.hasNextLine()) {
            String[] options = scanner.nextLine().split(";");
            if (options.length < 5) {
                break;
            }
            lineList.add(new UnilateralLine(options[0], options[1], options[2], options[3], options[4], options[5]));
        }
        scanner.close();

        // wirte your code here
        List<String> result = calculateUnilateral(lineList);

        for (String str : result) {
            System.out.println(str);
        }
    }
    public static List<String> calculateUnilateral(List<UnilateralLine> lineList) {
        List<String> result = new ArrayList<String>();

        //出发分拔中心对应的车辆
        Map<String,ArrayList<UnilateralLine>> sCenMap=new HashMap<String, ArrayList<UnilateralLine>>();
        //到达分拔中心对应的车辆
        Map<String,ArrayList<UnilateralLine>> eCenMap=new HashMap<String, ArrayList<UnilateralLine>>();
        //省份
        Map<String,ArrayList<UnilateralLine>> sProMap=new HashMap<String, ArrayList<UnilateralLine>>();
        Map<String,ArrayList<UnilateralLine>> eProMap=new HashMap<String, ArrayList<UnilateralLine>>();

        //对车辆信息进行遍历
        Iterator<UnilateralLine> iterator=lineList.iterator();
        while (iterator.hasNext()){

            UnilateralLine current=iterator.next();
            //整合信息
            if(sCenMap.get(current.sCen)==null) sCenMap.put(current.sCen,new ArrayList<UnilateralLine>());
            Collections.addAll(sCenMap.get(current.sCen),current);

            if(eCenMap.get(current.eCen)==null) eCenMap.put(current.eCen,new ArrayList<UnilateralLine>());
            Collections.addAll(eCenMap.get(current.eCen),current);

            if(eProMap.get(current.ePro)==null) eProMap.put(current.ePro,new ArrayList<UnilateralLine>());
            Collections.addAll(eProMap.get(current.ePro),current);

            if(sProMap.get(current.sPro)==null) sProMap.put(current.sPro,new ArrayList<UnilateralLine>());
            Collections.addAll(sProMap.get(current.sPro),current);

        }

        iterator=lineList.iterator();

        while (iterator.hasNext()){

            //查看是否有满足规则1的进行合并
            UnilateralLine current=iterator.next();
            //出发分拔与当前目的分拔相同的车
            ArrayList<UnilateralLine> eCenIdList=sCenMap.get(current.eCen);
            //目的分拔与当前出发分拔相同的车
            ArrayList<UnilateralLine> sCenIdList=eCenMap.get(current.sCen);
            for(int i=0;i<eCenIdList.size();i++){
                if(eCenIdList.get(i).eCen==current.sCen){
                    result.add(current.id+"+"+eCenIdList.get(i).id);
                }
            }

            //查看是否满足规则2的进行合并
            //查看是否满足规则3的进行合并

        }

        return result;
    }
    public static class UnilateralLine {
        private String id;
        private String sCen;//出发分拨
        private String sPro;//出发省
        private String eCen;//到达分拨
        private String ePro;//到达省
        //9.6m/17.5m
        private String tType;//车型
        public UnilateralLine(String id, String sCen, String sPro, String eCen, String ePro,String tType) {
            this.id = id;this.sCen = sCen;this.sPro = sPro;this.eCen = eCen;this.ePro = ePro;this.tType = tType;}
        public String getId() {return id;}
        public void setId(String id) {this.id = id;}
        public String getSCen() {return sCen;}
        public void setSCen(String ePro) {this.ePro = ePro;}
        public String getSPro() {return sPro;}
        public void setSPro(String sPro) {this.sPro = sPro;}
        public String getECen() {return eCen;}
        public void setECen(String eCen) {this.eCen = eCen;}
        public String getEPro() {return ePro;}
        public void setEPro(String ePro) {this.ePro = ePro;}
        public String getTType() {return tType;}
        public void setTType(String tType) {this.tType = tType;}
    }

}