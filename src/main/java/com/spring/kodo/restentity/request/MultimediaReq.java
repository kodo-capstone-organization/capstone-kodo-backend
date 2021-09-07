package com.spring.kodo.restentity.request;

import com.spring.kodo.entity.Multimedia;
import org.springframework.web.multipart.MultipartFile;

public class MultimediaReq
{
    private Multimedia multimedia;
    private MultipartFile multipartFile; // leave null if not updating

    public MultimediaReq(Multimedia multimedia, MultipartFile multipartFile) {
        this.multimedia = multimedia;
        this.multipartFile = multipartFile;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
