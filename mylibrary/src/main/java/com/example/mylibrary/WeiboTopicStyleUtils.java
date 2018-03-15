package com.example.mylibrary;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyanjiao
 * @data on 2017/9/14 18:14
 * @desc 对文字进行编译 仿照微博添加话题的样子
 */
public class WeiboTopicStyleUtils {
    private static WeiboTopicStyleUtils instance;
    private List<String> topicBeanList = new ArrayList<>();
    private SparseIntArray topicMap = new SparseIntArray();


    public static WeiboTopicStyleUtils getInstance() {
        if (null == instance) {
            synchronized (WeiboTopicStyleUtils.class) {
                if (instance == null) {
                    instance = new WeiboTopicStyleUtils();
                }
            }
        }
        return instance;
    }

    public SpannableString addTopic(String currentText, String topicContent) {
        if (TextUtils.isEmpty(topicContent)) {
            return null;
        }
        //                选择的要添加话题
        String addTopic = "#" + topicContent + "#";
        for (int i = 0; i < topicBeanList.size(); i++) {
            if (topicBeanList.get(i).equals(addTopic)) {
                return null;
            }
        }
        for (int i = 0; i < topicBeanList.size(); i++) {
            if (currentText.contains(topicBeanList.get(i))) {
                topicMap.put(currentText.indexOf(topicBeanList.get(i)), currentText.indexOf(topicBeanList.get(i)) + topicBeanList.get(i).length());
            }
        }

//                   设置原有字符串的颜色
        SpannableString spannableString = new SpannableString(currentText + addTopic);
        for (int i = 0; i < topicMap.size(); i++) {
            Integer key = topicMap.keyAt(i);
            spannableString
                    .setSpan(new ForegroundColorSpan(Color.BLUE), key, topicMap.get(key), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        spannableString
                .setSpan(new ForegroundColorSpan(Color.BLUE), currentText.length(), currentText.length() + addTopic.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        topicBeanList.add(addTopic);
        return spannableString;
    }

    public SpannableString removeTopic(String currentText, int selectionStart) {
        if (topicBeanList == null || topicBeanList.size() == 0) {
            return null;
        }
        int lastPos = 0;
        Iterator<String> iterator = topicBeanList.iterator();
        while (iterator.hasNext()) {
            String topicBean = iterator.next();
            if (currentText.contains(topicBean)) {
                if ((lastPos = currentText.indexOf(topicBean, lastPos)) == -1) {
                    break;
                }
                int length = topicBean.length();
                if (selectionStart != 0 && selectionStart >= lastPos && selectionStart <= (lastPos + length - 1)) {
                    String currentStr = currentText.substring(0, lastPos) + currentText
                            .substring(lastPos + topicBean.length() - 1); //字符串替换，删掉符合条件的字符串
                    iterator.remove(); //删除对应实体
                    topicMap.clear();
                    for (int j = 0; j < topicBeanList.size(); j++) {
                        if (currentStr.contains(topicBeanList.get(j))) {
                            topicMap
                                    .put(currentStr.indexOf(topicBeanList.get(j)), currentStr.indexOf(topicBeanList.get(j)) + topicBeanList.get(j).length());
                        }
                    }
//                   设置原有字符串的颜色
                    SpannableString spannableString = new SpannableString(currentStr);
                    for (int m = 0; m < topicMap.size(); m++) {
                        Integer key = topicMap.keyAt(m);
                        spannableString
                                .setSpan(new ForegroundColorSpan(Color.BLUE), key, topicMap.get(key), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    return spannableString;
                }
            } else {
//                lastPos += ("#" + topicBean + "#").length();
            }
        }
        return null;
    }


    public void removeAllTopic() {
        topicBeanList.clear();
        topicMap.clear();
    }

    public SpannableString changeTopicColor(List<String> topicList, String text) {
        removeAllTopic();
        Map<Integer, Integer> topicMap = new HashMap<>();
        for (int j = 0; j < topicList.size(); j++) {
            if (text.contains(topicList.get(j))) {
                topicMap.put(text.indexOf("@" + topicList.get(j)), text.indexOf("@" + topicList.get(j)) + topicList.get(j).length() + 1);
            }
        }

        SpannableString spannableString = new SpannableString(text);
        try {
            for (Integer key : topicMap.keySet()) {
                spannableString
                        .setSpan(new ForegroundColorSpan(Color.BLUE), key, topicMap.get(key), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return spannableString;
    }

}
