package com.fanhao.businessplatform.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.fanhao.businessplatform.common.constant.DataConstant;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Component
public class DataCreaterUtils {

    @Autowired
    private EmployeeMapper mapper;

    public void create() {
        Employee employee = new Employee();
        employee.setUsername(UUID.randomUUID().toString());
        employee.setPassword(SecureUtil.md5("123456"));
        employee.setName(getRandomName());
        employee.setAddress(getRandomSchool() + ":" + RandomUtil.randomInt() + "号");
        employee.setGender(RandomUtil.randomBoolean());
        employee.setPhone(String.valueOf(RandomUtil.randomLong(13500000000L, 17700000000L)));
        employee.setEmail(getRandomEmail());
        employee.setDepartment(RandomUtil.randomInt(1, 7));
        employee.setPosition(getPosition());
        employee.setRole("normal");
        employee.setBirthday(new Date());
        employee.setIdCard(RandomUtil.randomString(11));
        employee.setSchool(getRandomSchool());
        employee.setContractStartDate(new Date());
        employee.setQuitDate(null);
        employee.setStatus(false);
        employee.setWorkAge(1);
        employee.setRemark("");
        mapper.insert(employee);
    }

    public String getRandomEmail() {
        String prefix = RandomUtil.randomString(8);
        int k = RandomUtil.randomInt(0, 5);
        if (k == 0) prefix += "@foxmail.com";
        if (k == 1) prefix += "@gamil.com";
        if (k == 2) prefix += "@163.com";
        if (k == 3) prefix += "@qq.com";
        if (k == 4) prefix += "@126.com";
        if (k == 5) prefix += "@tt.com";
        return prefix;
    }


    /**
     * 随机获取职位
     * @return
     */
    public String getPosition() {
        String[] position = DataConstant.POSTION;
        int index = RandomUtil.randomInt(0, position.length - 1);
        return position[index];
    }

    /**
     * 获取随机学校
     * @return
     */
    public String getRandomSchool() {
        String[] school = DataConstant.SCHOOL;
        int index = RandomUtil.randomInt(0, school.length - 1);
        return school[index];
    }

    /**
     * 获取随机中文姓名
     * @return
     */
    public String getRandomName() {
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
    private String getChinese() {
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
