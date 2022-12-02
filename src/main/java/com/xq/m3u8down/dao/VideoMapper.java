package com.xq.m3u8down.dao;

import com.xq.m3u8down.entity.VideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/20 23:28
 */
public interface VideoMapper extends JpaRepository<VideoEntity, Long> {

    default Optional<Long> findTop() {
        PageRequest pageRequest = PageRequest.of(1, 1, Sort.Direction.DESC, "id");
        Page<VideoEntity> topOne = this.findAll(pageRequest);
        return topOne.get().findFirst().map(VideoEntity::getId);
    }

    List<VideoEntity> findAllByComefromTitle(String comefromTitle);
}
