package cn.szxy.aclservice.service.impl;


import cn.szxy.aclservice.entity.User;
import cn.szxy.aclservice.entity.UserRole;
import cn.szxy.aclservice.mapper.UserMapper;
import cn.szxy.aclservice.service.RoleService;
import cn.szxy.aclservice.service.UserRoleService;
import cn.szxy.aclservice.service.UserService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;
    @Override
    public User selectByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public void deleteUserAndRoleInfo(String id) {
        //先删除用户角色表中的信息
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id",id));

        //在删除用户
        baseMapper.deleteById(id);
    }

    @Override
    public void deleteUserAndRoleInfos(List<String> idList) {
        //先删除用户角色表中的数据
        for (String userid : idList) {
            userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id",userid));
        }
        //在删除用户信息
        baseMapper.deleteBatchIds(idList);
    }
}
