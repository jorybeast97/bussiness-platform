package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.entity.Notice;
import com.fanhao.businessplatform.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public boolean addNotice(final Notice notice) {
        return noticeMapper.insert(notice) > 0;
    }

    public boolean deleteNoticeById(final Integer id) {
        return noticeMapper.deleteById(id) > 0;
     }

    public boolean updateNotice(final Notice notice) {
        return noticeMapper.updateById(notice) > 0;
    }

    public Notice selectNoticeById(final Integer id) {
        return noticeMapper.selectById(id);
    }

    public List<Notice> selectAllNotice(final Integer pageNum,
                                        final Integer pageSize) {
        IPage<Notice> page = new Page<>(pageNum, pageSize);
        page = noticeMapper.selectPage(page, null);
        return page.getRecords();
    }
}
