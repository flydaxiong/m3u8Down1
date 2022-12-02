package com.xq.m3u8down.controller;


import com.xq.m3u8down.dao.TagMapper;
import com.xq.m3u8down.dao.VideoMapper;
import com.xq.m3u8down.dto.LayuiPage;
import com.xq.m3u8down.dto.VideoDTO;
import com.xq.m3u8down.entity.TagEntity;
import com.xq.m3u8down.entity.VideoEntity;
import com.xq.m3u8down.service.m3u8.M3u8Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/20 20:54
 */

@RestController
public class IndexController {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private M3u8Handler m3u8Handler;
   // 1111
    @GetMapping("/video")
    public Mono<LayuiPage<VideoDTO>> getList(@Param("page") Integer page,
            @Param("limit") Integer limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Order.desc("id")));
        Page<VideoEntity> entityPage = videoMapper.findAll(pageRequest);
        entityPage.stream().forEach(e -> e.getTags());
        Page<VideoDTO> map = entityPage.map(VideoDTO::convert);
        return Mono.just(LayuiPage.convert(map));
    }



    @GetMapping("/tag")
    public Flux<TagEntity> getTagList() {
        List<TagEntity> tagEntityList = tagMapper.findAll();
        return Flux.fromIterable(tagEntityList);
    }

    @PatchMapping("/video/{id}")
    public Mono<Void> downVideo(@PathVariable Long id,
            @RequestParam(value = "path", required = false)String path) {
        if (path == null || path.isEmpty()) {
            path = "/media/F/back/test/%s.mp4";
        }

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        final String finalPath = path + "/%s.mp4";

        new Thread(() -> {
            videoMapper.findById(id)
                    .ifPresent(entity -> {
                        m3u8Handler.down(entity.getVideoUrl(), String.format(finalPath, entity.getTitle()));
                    });
        }).start();


        return Mono.empty();
    }

}
