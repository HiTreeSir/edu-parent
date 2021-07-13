package cn.szxy.educms.service;

import cn.szxy.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author Jack
 * @since 2020-09-05
 */
public interface CrmBannerService extends IService<CrmBanner> {
    /**
     * 查询所有幻灯片
     * @return
     */
    List<CrmBanner> selectAllBanner();

    /**
     * 删除幻灯片，并且删除上传到阿里云的图片
     * @param id
     */
    void removeBannerById(String id);
}
