package cn.szxy.aclservice.service;

import cn.szxy.aclservice.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface UserService extends IService<User> {

    // 从数据库中取出用户信息
    User selectByUsername(String username);
    //删除用户以及用户角色表中的信息
    void deleteUserAndRoleInfo(String id);
    //批量删除用户和用户角色表中的数据
    void deleteUserAndRoleInfos(List<String> idList);
}
