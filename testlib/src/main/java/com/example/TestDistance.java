package com.example;

/**
 * Created by lxl on 2017/2/6.
 */
public class TestDistance {
    static String s="<?XMLVERSION=\"1.0\"ENCODING=\"GBK\"?><YYXML><HEAD><RECODE>0000</ RECODE><REMSG>成功处理</REMSG></HEAD><BODY><CODELIST><CODE><NAME>收款</NAME><VALUE>YW_SK</VALUE></CODE><CODE><NAME>付款</NAME><VALUE>YW_FK</ VALUE></CODE></CODELIST></BODY></YYXML>";

    public static void main(String[] args){

        String s2 =s.replaceAll(" ","").replace("\\\\","");

        System.out.println(s2.length()+"'");
    }



    // 计算两点距离
    private final static double EARTH_RADIUS = 6378137.0;
    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
