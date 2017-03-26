package com.yiyekeji.coolschool.api;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * HTML 解析工具类
 *
 */
public class HtmlParser {

    /**
     * 得到栋数对应的value
     */
    public static List<Map<String, String>> parseHtmlForElect(String html) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        Pattern pattern = Pattern.compile("<option value=\"(.*)\">(.*)</option>");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            map = new HashMap<String, String>();
            map.put("apartValue", matcher.group(1));
            String apart = matcher.group(2);
            map.put("apartID", (apart.split("栋"))[0]);
            list.add(map);
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }


    /**
     * 解析课程html Note: 同一节课，会有>=1个课程 为此用List<Lesson>来存贮
     *周1-7，课时1-5
     * @param html 课程表html
     * @return map[周数，对应当日的课程列表]
     */
    public static Map<Integer, Map<Integer, List<Lesson>>> parseHtmlForLesson(String html) {
        Map<Integer, Map<Integer, List<Lesson>>> map = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            HashMap<Integer, List<Lesson>> hashmap = new HashMap<Integer, List<Lesson>>();
            for (int j = 1; j <= 5; j++)
                hashmap.put(j, new ArrayList<Lesson>());
            map.put(i, hashmap);
        }
        Pattern p = Pattern.compile("<td valign=top align=center>(.*)</td>");
        Matcher m = p.matcher(html);
        int day = 1, lessonTime = 1;
        while (m.find()) {
            List<Lesson> mlist = new ArrayList<Lesson>();
            String s = m.group(1).trim();
            s = s.replaceAll("<br>", "&nbsp;");
            String[] ms = s.split("&nbsp;");
            int j = 0;
            int count = 0;
            Lesson l = null;
            while (j < ms.length) {
                while (ms[j].equals(""))
                    j++;
                switch (count) {
                    case 0:
                        l = new Lesson();
                        l.LessonName = ms[j++].trim();
                        count++;
                        break;

                    case 1:
                        l.Time = ms[j++].trim();
                        count++;
                        break;
                    case 2:
                        l.address = ms[j++].trim();
                        count++;
                        break;

                    case 3:
                        l.Teacher = ms[j++].trim();
                        mlist.add(l);
                        count = 0;
                        break;
                }
            }
            if (ms.length == 0) {
                l = new Lesson();
                mlist.add(l);
            }
            (map.get(day)).put(lessonTime, mlist);
            if (day + 1 > 7) {
                day = 1;
                lessonTime++;
            } else {
                day++;
            }
        }
        return map;
    }

    /**
     * 解析成绩列表
     *
     * @param html
     *            成绩页页代码
     * @param list
     *            成绩列表
     * @return 成绩列表
     */
    public static List<ScoreRecord> parseHtmlForScore(String html,
                                                      List<ScoreRecord> list) {
        String[] subHtml = html
                .split("<p class=MsoNormal><span lang=EN-US>&nbsp;</span></p>");
        // System.out.println(subHtml.length);
        // System.out.println(subHtml[subHtml.length-3]);

        int temp = 0;
        for (int a = 1; a < (subHtml.length - 3); a++) {
            String regex = "<p class=MsoNormal align=center style='text-align:center'>([0-9a-zA-Z]+)</p>"; // 课程代码
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(subHtml[a]);
            while (m.find()) {
                int count = m.groupCount();
                for (int i = 1; i <= count; i++) {
                    ScoreRecord sr = new ScoreRecord();
                    sr.lessonCode = m.group(i);
                    list.add(sr);
                }
            }

            regex = "<p class=MsoNormal><span style='font-family:宋体'>(.*)</span></p>"; // 课程名称
            p = Pattern.compile(regex);
            m = p.matcher(subHtml[a]);
            int i = temp;
            while (m.find()) {

                list.get(i).lessonName = m.group(1);  //名称
                list.get(i).lessonTerm = a;   //学期
                i++;
                if (i == list.size())
                    break;
            }
            regex = "<p class=MsoNormal align=center style='text-align:center'><span\\s*\\r*style='font-family:宋体'>(.*)</span></p>";// 课程类别（选修or必修or通识课or公选课）
            p = Pattern.compile(regex);
            m = p.matcher(subHtml[a]);
            i = temp;
            while (m.find()) {
                if (m.group(1).equals("必修") || m.group(1).equals("选修")
                        || m.group(1).equals("通识课") || m.group(1).equals("公选课")
                        || m.group(1).equals("重修")) {
                    list.get(i++).category = m.group(1);
                    if (i == list.size())
                        break;
                }
            }
            regex = "<p class=MsoNormal align=center style='text-align:center'><span lang=EN-US>(.*)</span></p>";// 第一个为学分，第二个为成绩
            p = Pattern.compile(regex);
            m = p.matcher(subHtml[a]);
            i = temp;
            boolean sy = true;
            while (m.find()) {
                if (sy) {
                    list.get(i).lessonScore = m.group(1);
                } else {
                    list.get(i).score = m.group(1);
                    i++;
                }
                sy = !sy;
                if (i == list.size())
                    break;
            }
            temp = list.size();
        }
        if (list == null)
            System.out.println("list为空");
        return list;
    }


}
