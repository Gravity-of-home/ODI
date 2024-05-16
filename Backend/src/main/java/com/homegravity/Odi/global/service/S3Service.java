package com.homegravity.Odi.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.homegravity.Odi.global.entity.S3Folder;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder.profile}")
    private String profileImageFolder;

    @Value("${cloud.aws.s3.folder.receipt}")
    private String receiptFolder;

    @Value("${cloud.aws.s3.folder.report}")
    private String reportFolder;

    /*
    S3 파일 업로드
     */
    public String saveFile(MultipartFile multipartFile, S3Folder s3folder) {
        // 파일 이름 생성
        String fileName = createFileName(multipartFile.getOriginalFilename());
        String key = s3folder.toString() + fileName;
        try {
            // S3 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            // 버킷에 파일 저장
            amazonS3.putObject(bucket, key, multipartFile.getInputStream(), metadata);
            // 파일 주소 반환
            return amazonS3.getUrl(bucket, key).toString();
        } catch (IOException e) {
            log.info("S3 파일 저장 실퍠");
            throw new BusinessException(ErrorCode.S3_SAVE_ERROR, ErrorCode.S3_SAVE_ERROR.getMessage());
        }

    }

    /*
    S3 버킷 파일 삭제
     */
    public void deleteFile(String originalFilenUrl)  {
        try {
            // URL 주소에서 key값 생성
            String key = originalFilenUrl.split("/")[3];
            // 버킷에 파일 삭제
            amazonS3.deleteObject(bucket, key);
        } catch (Exception e) {
            log.info("S3 파일 삭제 실패");
            throw new BusinessException(ErrorCode.S3_DELETE_ERROR, ErrorCode.DELETE_ERROR.getMessage());
        }
    }

    /*
    파일 이름 생성
     */
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    /*
    파일의 확장자명
     */
    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        }catch(StringIndexOutOfBoundsException e) {
            throw new BusinessException(ErrorCode.FILE_FORMAT_NOT_EXIST, ErrorCode.FILE_FORMAT_NOT_EXIST.getMessage());
        }
    }

    /*
    파일 폴더명
     */
    public String getFileFolder(S3Folder s3Folder) {

        String folder = "";
        if(s3Folder == S3Folder.PROFILE_IMAGE) {
            folder = profileImageFolder;
        } else if(s3Folder ==S3Folder.RECEIPT){
            folder = receiptFolder;
        } else if(s3Folder == S3Folder.REPORT) {
            folder = reportFolder;
        }
        return folder;
    }

}
