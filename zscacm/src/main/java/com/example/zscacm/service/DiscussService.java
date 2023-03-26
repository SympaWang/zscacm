package com.example.zscacm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zscacm.entity.Discuss;
import com.example.zscacm.mapper.DiscussMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class DiscussService {

    @Autowired
    private DiscussMapper discussMapper;

    public int addDiscuss(Discuss discuss) {

        discuss.setCreateTime(new Date());
        return discussMapper.insert(discuss);
    }

    public List<Discuss> getDiscussPage(int pageNum, int limit) {
        Page<Discuss> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(limit);

        QueryWrapper<Discuss> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("type", "update_time");

        return discussMapper.selectPage(page, wrapper).getRecords();
    }

    public int getDiscussCount() {
        return Math.toIntExact(discussMapper.selectCount(new QueryWrapper<>()));
    }

}
