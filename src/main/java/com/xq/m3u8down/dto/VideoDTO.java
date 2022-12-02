package com.xq.m3u8down.dto;

import com.xq.m3u8down.entity.TagEntity;
import com.xq.m3u8down.entity.VideoEntity;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/21 23:01
 */
public class VideoDTO {
    private Long id;
    private String title;
    private String thumb;
    private String preview;
    private String panorama;
    private String videoUrl;
    private Long comefrom;
    private String comefromTitle;
    private Integer isVip;
    private List<TagDTO> tags;

    public static VideoDTO convert(VideoEntity videoEntity) {
        VideoDTO videoDTO =new VideoDTO();
        BeanUtils.copyProperties(videoEntity, videoDTO);
        return videoDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPanorama() {
        return panorama;
    }

    public void setPanorama(String panorama) {
        this.panorama = panorama;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Long getComefrom() {
        return comefrom;
    }

    public void setComefrom(Long comefrom) {
        this.comefrom = comefrom;
    }

    public String getComefromTitle() {
        return comefromTitle;
    }

    public void setComefromTitle(String comefromTitle) {
        this.comefromTitle = comefromTitle;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
