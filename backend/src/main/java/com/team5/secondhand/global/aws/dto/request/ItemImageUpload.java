package com.team5.secondhand.global.aws.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemImageUpload {

    private final List<MultipartFile> itemImages;

}
