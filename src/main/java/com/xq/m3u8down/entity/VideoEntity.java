package com.xq.m3u8down.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/20 23:07
 */
@Table(name = "t_video")
@Entity
@Data
public class VideoEntity {
    @Id
    private Long id;
    private String title;
    private String thumb;
    private String preview;
    private String panorama;
    private String videoUrl;
    private Long comefrom;
    private String comefromTitle;
    private Integer isVip;

    private LocalDateTime saveTime = LocalDateTime.now();

    @OneToMany(targetEntity = TagEntity.class, fetch = FetchType.EAGER)
    @JoinTable(name = "t_tag")
    private List<TagEntity> tags;


}
