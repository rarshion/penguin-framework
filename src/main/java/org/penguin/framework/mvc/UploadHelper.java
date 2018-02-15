package org.penguin.framework.mvc;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.penguin.framework.FrameworkConstant;
import org.penguin.framework.mvc.bean.Params;
import org.penguin.framework.mvc.bean.Multipart;
import org.penguin.framework.mvc.bean.Multiparts;
import org.penguin.framework.mvc.fault.UploadException;
import org.penguin.framework.util.FileUtil;
import org.penguin.framework.util.StreamUtil;
import org.penguin.framework.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author ： Martin
 * Date : 18/2/13
 * Description :
 * Version : 2.0
 */
public class UploadHelper {
    private static final Logger logger = LoggerFactory.getLogger(UploadHelper.class);

    /**
     * FileUpload 对象（用于解析所上传的文件）
     */
    private static ServletFileUpload fileUpload;

    /**
     * 初始化
     */
    public static void init(ServletContext servletContext) {
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        fileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
        int uploadLimit = FrameworkConstant.UPLOAD_LIMIT;
        if (uploadLimit != 0) {
            fileUpload.setFileSizeMax(uploadLimit * 1024 * 1024); // 单位为 M
        }
    }

    /**
     * 判断请求是否为 multipart 类型
     */
    public static boolean isMultipart(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建 multipart 请求参数列表
     */
    public static List<Object> createMultipartParamList(HttpServletRequest request) throws Exception {
        List<Object> paramList = new ArrayList<Object>();
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        List<Multipart> multipartList = new ArrayList<Multipart>();
        List<FileItem> fileItemList;
        try {
            fileItemList = fileUpload.parseRequest(request);
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            throw new UploadException(e);
        }
        for (FileItem fileItem : fileItemList) {
            String fieldName = fileItem.getFieldName();
            if (fileItem.isFormField()) {
                String fieldValue = fileItem.getString(FrameworkConstant.UTF_8);
                fieldMap.put(fieldName, fieldValue);
            } else {
                String fileName = FileUtil.getRealFileName(fileItem.getName());
                if (StringUtil.isNotEmpty(fileName)) {
                    long fileSize = fileItem.getSize();
                    String contentType = fileItem.getContentType();
                    InputStream inputSteam = fileItem.getInputStream();
                    Multipart multipart = new Multipart(fieldName, fileName, fileSize, contentType, inputSteam);
                    multipartList.add(multipart);
                }
            }
        }
        paramList.add(new Params(fieldMap));
        paramList.add(new Multiparts(multipartList));
        return paramList;
    }

    /**
     * 上传文件
     */
    public static void uploadFile(String basePath, Multipart multipart) {
        try {
            if (multipart != null) {
                String filePath = basePath + multipart.getFileName();
                FileUtil.createFile(filePath);
                InputStream inputStream = new BufferedInputStream(multipart.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream, outputStream);
            }
        } catch (Exception e) {
            logger.error("上传文件出错！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量上传文件
     */
    public static void uploadFiles(String basePath, Multiparts multiparts) {
        for (Multipart multipart : multiparts.getAll()) {
            uploadFile(basePath, multipart);
        }
    }
}
