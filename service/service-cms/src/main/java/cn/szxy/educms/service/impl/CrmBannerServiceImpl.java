package cn.szxy.educms.service.impl;

import cn.szxy.commonutils.R;
import cn.szxy.educms.client.OssClient;
import cn.szxy.educms.entity.CrmBanner;
import cn.szxy.educms.mapper.CrmBannerMapper;
import cn.szxy.educms.service.CrmBannerService;
import cn.szxy.servicebase.exception.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author Jack
 * @since 2020-09-05
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Autowired
    private OssClient ossClient;
    /**
     * 查询所有幻灯片
     * @return
     */
    @Cacheable(value = "banner",key = "'selectIndexList'") //添加redis缓存注解
    @Override
    public List<CrmBanner> selectAllBanner() {
        //根据id进行降序排列，查询前两条数据

        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        //拼接sql
        wrapper.last("limit 2");
        List<CrmBanner> banners = baseMapper.selectList(wrapper);
        return banners;
    }

    @CacheEvict(value = "banner", allEntries=true)
    @Override
    public void removeBannerById(String id) {
        //通过id查询获取文件名称
        CrmBanner banner = baseMapper.selectById(id);
        String fileName = banner.getFileName();


        if(!StringUtils.isEmpty(fileName)){
            //使用远程接口删除
            R result = ossClient.deleteFile(fileName);
            if(result.getCode() == 20001){
                throw new CustomException(20001,result.getMessage());
            }
        }


        //删除幻灯片信息
        baseMapper.deleteById(id);
    }
}
