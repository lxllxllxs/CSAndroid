package com.yiyekeji.coolschool.utils;

import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import java.util.List;

/**
 * Created by lxl on 2017/1/11.
 */
public class CommonUtils {
    private int cellId = 0;
    private int lac = 0;

    /**
     * 功能描述：通过手机信号获取基站信息
     * # 通过TelephonyManager 获取lac:mcc:mnc:cell-id
     * # MCC，Mobile Country Code，移动国家代码（中国的为460）；
     * # MNC，Mobile Network Code，移动网络号码（中国移动为0，中国联通为1，中国电信为2）；
     * # LAC，Location Area Code，位置区域码；
     * # CID，Cell Identity，基站编号；
     * # BSSS，Base station signal strength，基站信号强度。
     *
     * @author android_ls
     */

    public static void getCellLoaction(Context context) {

        TelephonyManager mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        // 返回值MCC + MNC
        String operator = mTelephonyManager.getNetworkOperator();
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        // 中国移动和中国联通获取LAC、CID的方式
        GsmCellLocation location = (GsmCellLocation) mTelephonyManager.getCellLocation();
        int lac = location.getLac();
        int cellId = location.getCid();

        LogUtil.i("getCellLoaction", " MCC = " + mcc + "\t MNC = " + mnc + "\t LAC = " + lac + "\t CID = " + cellId);

        // 中国电信获取LAC、CID的方式
                /*CdmaCellLocation location1 = (CdmaCellLocation) mTelephonyManager.getCellLocation();
                lac = location1.getNetworkId();
                cellId = location1.getBaseStationId();
                cellId /= 16;*/
        // 获取邻区基站信息
        List<NeighboringCellInfo> infos = mTelephonyManager.getNeighboringCellInfo();
        StringBuffer sb = new StringBuffer("总数 : " + infos.size() + "\n");
        for (NeighboringCellInfo info1 : infos) { // 根据邻区总数进行循环
            sb.append(" LAC : " + info1.getLac()); // 取出当前邻区的LAC
            sb.append(" CID : " + info1.getCid()); // 取出当前邻区的CID
            sb.append(" BSSS : " + (-113 + 2 * info1.getRssi()) + "\n"); // 获取邻区基站信号强度
        }
        LogUtil.i("getCellLoaction", " 获取邻区基站信息:" + sb.toString());

    }

}
