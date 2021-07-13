package cn.szxy.aclservice.service;

import cn.szxy.aclservice.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface RoleService extends IService<Role> {

    //根据用户获取角色数据
    Map<String, Object> findRoleByUserId(String userId);

    //根据用户分配角色
    void saveUserRoleRealtionShip(String userId, String[] roleId);

    List<Role> selectRoleByUserId(String id);
    //删除角色一级角色分配表中的数据
    void deleteRoleAndRre(String id);
    //批量删除角色表和角色职位表中的数据
    void deleteReleAndRres(List<String> idList);
}
