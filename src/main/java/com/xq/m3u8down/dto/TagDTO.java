package com.xq.m3u8down.dto;

import com.xq.m3u8down.entity.TagEntity;
import com.xq.m3u8down.entity.VideoEntity;
import org.springframework.beans.BeanUtils;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/21 23:02
 */
public class TagDTO {
    private Long id;
    private String name;

    public static TagDTO convert(TagEntity tagEntity) {
        TagDTO tagDTO =new TagDTO();
        BeanUtils.copyProperties(tagEntity, tagDTO);
        return tagDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
