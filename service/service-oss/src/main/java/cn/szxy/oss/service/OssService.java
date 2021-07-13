package cn.szxy.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OssService {
    /**
     * 文件上传
     * @param file
     * @return
     */
    String uploadFileAvatar(MultipartFile file);

    /**
     * oss文件删除
     * @param fileName
     */
    void deleteFileAvater(String fileName);

    List uploadFileAvatarReturnFileName(MultipartFile file);
}
