package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.entity.Achievement;
import com.fanhao.businessplatform.mapper.AchievementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementService {
    @Autowired
    private AchievementMapper achievementMapper;

    public boolean addAchievement(final Achievement achievement) {
        return achievementMapper.insert(achievement) > 0;
    }

    public boolean deleteAchievement(final Integer id) {
        return achievementMapper.deleteById(id) > 0;
    }

    public boolean updateAchievement(final Achievement achievement) {
        return achievementMapper.updateById(achievement) > 0;
    }

    public Achievement selectAchievementById(final Integer id) {
        return achievementMapper.selectById(id);
    }

    public List<Achievement> selectAllAchievement(final Integer pageNum,
                                                  final Integer pageSize) {
        IPage<Achievement> page = new Page<>(pageNum, pageSize);
        page = achievementMapper.selectPage(page, null);
        return page.getRecords();
    }
}
