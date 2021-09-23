package com.spring.kodo.util;

import com.spring.kodo.util.enumeration.MultimediaType;

public class FileTypeUtil
{
    public static MultimediaType getMultimediaType(String url)
    {
        switch (url)
        {
            case "png":
            case "jpg":
            case "jpeg":
            case "gif":
                return MultimediaType.IMAGE;
            case "doc":
            case "docx":
            case "pdf":
                return MultimediaType.DOCUMENT;
            case "mp4":
            case "mov":
                return MultimediaType.VIDEO;
            case "zip":
                return MultimediaType.ZIP;
            default:
                return MultimediaType.EMPTY;
        }
    }
}
