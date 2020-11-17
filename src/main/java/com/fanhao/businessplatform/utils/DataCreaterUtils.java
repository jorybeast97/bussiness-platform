package com.fanhao.businessplatform.utils;

import cn.hutool.core.util.RandomUtil;
import com.fanhao.businessplatform.common.constant.DataConstant;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class DataCreaterUtils {

    /**
     * 获取随机学校
     * @return
     */
    public static String getRandomSchool() {
        String[] school = DataConstant.SCHOOL;
        int index = RandomUtil.randomInt(0, school.length - 1);
        return school[index];
    }

    /**
     * 获取随机中文姓名
     * @return
     */
    public static String getRandomName() {
        String[] firstName = DataConstant.FIRST_NAME;
        int index = RandomUtil.randomInt(0, firstName.length - 1);
        String name = firstName[index]; //获得一个随机的姓氏
        boolean status = RandomUtil.randomBoolean();
        if (status) name += getChinese() + getChinese();
        else name += getChinese();
        return name;
    }

    /**
     * 产生随机中文字
     * @return
     */
    private static String getChinese() {
        String str = null;
        int highPos, lowPos;
        Random random = new Random();
        highPos = (176 + Math.abs(random.nextInt(71)));//区码，0xA0打头，从第16区开始，即0xB0=11*16=176,16~55一级汉字，56~87二级汉字
        random=new Random();
        lowPos = 161 + Math.abs(random.nextInt(94));//位码，0xA0打头，范围第1~94列

        byte[] bArr = new byte[2];
        bArr[0] = (new Integer(highPos)).byteValue();
        bArr[1] = (new Integer(lowPos)).byteValue();
        try {
            str = new String(bArr, "GB2312");	//区位码组合成汉字
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
