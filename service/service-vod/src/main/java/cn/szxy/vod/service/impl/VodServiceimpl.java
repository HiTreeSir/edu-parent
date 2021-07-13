package cn.szxy.vod.service.impl;

import cn.szxy.servicebase.exception.CustomException;
import cn.szxy.vod.service.VodService;
import cn.szxy.vod.utils.AliyunVodSDKUtils;
import cn.szxy.vod.utils.ConstantPropertiesUtil;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import jdk.internal.util.xml.impl.Input;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VodServiceimpl implements VodService {

    /**
     * 阿里云视频上传
     * @param file
     * @return 返回上传视频id
     */
    @Override
    public String uploadAlyVideo(MultipartFile file) {


        try {
            String fileName =  file.getOriginalFilename();   //上传文件的原始名称
            String title =  fileName.substring(0,fileName.lastIndexOf('.')); //上传文件的标题 去掉后缀名
            InputStream inputStream = file.getInputStream(); //上传文件输入流

            UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET, title, fileName, inputStream);
            /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
            //request.setShowWaterMark(true);
            /* 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档 https://help.aliyun.com/document_detail/57029.html */
            //request.setCallback("http://callback.sample.com");
            /* 自定义消息回调设置，参数说明参考文档 https://help.aliyun.com/document_detail/86952.html#UserData */
            //request.setUserData(""{\"Extend\":{\"test\":\"www\",\"localId\":\"xxxx\"},\"MessageCallback\":{\"CallbackURL\":\"http://test.test.com\"}}"");
            /* 视频分类ID(可选) */
            //request.setCateId(0);
            /* 视频标签,多个用逗号分隔(可选) */
            //request.setTags("标签1,标签2");
            /* 视频描述(可选) */
            //request.setDescription("视频描述");
            /* 封面图片(可选) */
            //request.setCoverURL("http://cover.sample.com/sample.jpg");
            /* 模板组ID(可选) */
            //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
            /* 工作流ID(可选) */
            //request.setWorkflowId("d4430d07361f0*be1339577859b0177b");
            /* 存储区域(可选) */
            //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
            /* 开启默认上传进度回调 */
            // request.setPrintProgress(true);
            /* 设置自定义上传进度回调 (必须继承 VoDProgressListener) */
            // request.setProgressListener(new PutObjectProgressListener());
            /* 设置应用ID*/
            //request.setAppId("app-1000000");
            /* 点播服务接入点 */
            //request.setApiRegionId("cn-shanghai");
            /* ECS部署区域*/
            // request.setEcsRegionId("cn-shanghai");
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId = null;
            if (response.isSuccess()) {
                videoId =  response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId =  response.getVideoId();
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过id删除上传到阿里云视频中的id
     * @param videoId
     */
    @Override
    public void removeVideoById(String videoId) {
        try{
            //初始化
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建请求对象
            //一次只能批量删20个
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);


            //获取响应
            DeleteVideoResponse response = client.getAcsResponse(request);
            System.out.print("RequestId = " + response.getRequestId() + "\n");

        }catch (ClientException e){
            throw new CustomException(20001, "视频删除失败");
        }
    }

    @Override
    public void removeMoreVideoByIds(List videoIdList) {
        try{
            //初始化
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //创建请求对象
            //一次只能批量删20个
            DeleteVideoRequest request = new DeleteVideoRequest();
            String videoIds = String.join(",", videoIdList);
            request.setVideoIds(videoIds);

            //获取响应
            DeleteVideoResponse response = client.getAcsResponse(request);

            System.out.print("RequestId = " + response.getRequestId() + "\n");

        }catch (ClientException e){
            throw new CustomException(20001, "视频删除失败");
        }
    }


}
