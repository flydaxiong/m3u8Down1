package com.xq.m3u8down.dao;

import com.xq.m3u8down.entity.TagEntity;
import com.xq.m3u8down.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/21 17:42
 */
public interface TagMapper extends JpaRepository<TagEntity, Long> {
}
