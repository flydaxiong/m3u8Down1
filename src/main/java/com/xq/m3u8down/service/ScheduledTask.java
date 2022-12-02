package com.xq.m3u8down.service;

import com.xq.m3u8down.dao.TagMapper;
import com.xq.m3u8down.dao.VideoMapper;
import com.xq.m3u8down.dto.PageDataDTO;
import com.xq.m3u8down.entity.TagEntity;
import com.xq.m3u8down.entity.VideoEntity;
import com.xq.m3u8down.service.http.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/21 0:03
 */
@Service
public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private TagMapper tagMapper;

    /**
     * 每天定时查询新增的数据，补充至数据库中
     */
//    @Scheduled(fixedRate = 24, timeUnit = TimeUnit.HOURS, initialDelay = 0)
    public void task() {
        ParameterizedTypeReference<PageDataDTO<VideoEntity>> reference = new ParameterizedTypeReference<PageDataDTO<VideoEntity>>() {
        };
        URI uri = URI.create(getSyncUrls(1));
        logger.info("start sync video. url：{}", uri);
        PageDataDTO<VideoEntity> videoRes = HttpHandler.exchangeGet(uri, reference).block();

        if (videoRes == null) {
            logger.warn("sync video response is empty. ");
            return;
        }
        List<VideoEntity> data = videoRes.getData();
        List<TagEntity> tagEntityList = data.stream().map(VideoEntity::getTags)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        tagMapper.saveAll(tagEntityList);
        tagMapper.flush();
        videoMapper.saveAll(data);
        videoMapper.flush();
        logger.info("sync video success. ");
    }

    private String getSyncUrls(long page) {
        String urlFormat = "https://nnp35.com/upload_json_live/%s/videolist_%s_%02d_%s_-_-_100_%s.json";
        Object[] params = new Object[5];
        LocalDateTime localTime = LocalDateTime.now();
        String date = localTime.format(DateTimeFormatter.BASIC_ISO_DATE);
        int hour = localTime.getHour();
        params[0] = date;
        params[1] = date;
        params[2] = (hour / 2) * 2;
        params[3] = "2";
        // 第几页
        params[4] = page;
        return String.format(urlFormat, params);
    }

}
