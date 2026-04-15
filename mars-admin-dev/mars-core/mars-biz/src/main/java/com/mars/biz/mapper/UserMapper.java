package com.mars.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.biz.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息表 Mapper
 * 
 * @author Mars
 * @date 2026-04-12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
