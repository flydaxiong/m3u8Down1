package com.xq.m3u8down.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/20 23:16
 */
@Table(name = "t_tag")
@Entity
@Data
public class TagEntity {
    @Id
    private Long id;
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private VideoEntity videoList;

    @ManyToOne
    @JoinColumn(name = "video_entity_id")
    private VideoEntity videoEntity;

    public VideoEntity getVideoEntity() {
        return videoEntity;
    }

    public void setVideoEntity(VideoEntity videoEntity) {
        this.videoEntity = videoEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity tagEntity = (TagEntity) o;
        return id.equals(tagEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
