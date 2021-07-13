package cn.szxy.aclservice.service.impl;


import cn.szxy.aclservice.entity.Role;
import cn.szxy.aclservice.entity.RolePermission;
import cn.szxy.aclservice.entity.User;
import cn.szxy.aclservice.entity.UserRole;
import cn.szxy.aclservice.mapper.RoleMapper;
import cn.szxy.aclservice.service.RolePermissionService;
import cn.szxy.aclservice.service.RoleService;
import cn.szxy.aclservice.service.UserRoleService;
import cn.szxy.aclservice.service.UserService;
import cn.szxy.servicebase.exception.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;
    //根据用户获取角色数据
    @Override
    public Map<String, Object> findRoleByUserId(String userId) {
        //查询所有的角色
        List<Role> allRolesList =baseMapper.selectList(null);

        //根据用户id，查询用户拥有的角色id
        List<UserRole> existUserRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", userId).select("role_id"));

        List<String> existRoleList = existUserRoleList.stream().map(c->c.getRoleId()).collect(Collectors.toList());

        //对角色进行分类
        List<Role> assignRoles = new ArrayList<Role>();
        for (Role role : allRolesList) {
            //已分配
            if(existRoleList.contains(role.getId())) {
                assignRoles.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoles", assignRoles);
        roleMap.put("allRolesList", allRolesList);
        return roleMap;
    }

    //根据用户分配角色
    @Override
    public void saveUserRoleRealtionShip(String userId, String[] roleIds) {
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));

        List<UserRole> userRoleList = new ArrayList<>();
        for(String roleId : roleIds) {
            if(StringUtils.isEmpty(roleId)) continue;
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);

            userRoleList.add(userRole);
        }
        userRoleService.saveBatch(userRoleList);
    }

    @Override
    public List<Role> selectRoleByUserId(String id) {
        //根据用户id拥有的角色id
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", id).select("role_id"));
        List<String> roleIdList = userRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        List<Role> roleList = new ArrayList<>();
        if(roleIdList.size() > 0) {
            roleList = baseMapper.selectBatchIds(roleIdList);
        }
        return roleList;
    }

    @Override
    public void deleteRoleAndRre(String id) {
        //判断用户角色中是否有由用户角色时当前要删除的角色
        QueryWrapper<UserRole> wrapperUser = new QueryWrapper<>();
        wrapperUser.eq("role_id",id);
        List<UserRole> userRoles = userRoleService.list(wrapperUser);
        System.out.println("---------------" + userRoles);
        if(!userRoles.isEmpty()){
            throw new CustomException(20001,"该角色有用户使用不能删除！");
        }
        //删除角色权限表中的数据
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",id);

        rolePermissionService.remove(wrapper);
        //再次删除角色表中的角色
        baseMapper.deleteById(id);
    }

    //批量删除角色表和角色职位表中的数据
    @Override
    public void deleteReleAndRres(List<String> idList) {
        //判断用户RolePermission角色权限表中的是否有该删除的角色
        QueryWrapper<UserRole> wrapperUser = new QueryWrapper<>();
        for (String roleId : idList) {
            wrapperUser.eq("role_id",roleId);
            List<UserRole> list = userRoleService.list(wrapperUser);
            if(!list.isEmpty()){
                throw new CustomException(20001,"该角色有用户使用不能批量删除！");
            }
        }
        //QueryWrapper<RolePermission> userRolePer = new QueryWrapper<>();
        //删除角色权限表中的数据
        System.out.println(idList);
        for (String roleId : idList) {
            //userRolePer.eq("role_id",roleId);
            rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id",roleId));
        }

        //删除Role角色表中的信息
        baseMapper.deleteBatchIds(idList);
    }
}
