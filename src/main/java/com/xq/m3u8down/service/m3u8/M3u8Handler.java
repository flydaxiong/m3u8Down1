package com.xq.m3u8down.service.m3u8;

import com.xq.m3u8down.dto.Pair;
import com.xq.m3u8down.service.http.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Description: m3u8解析器
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/19 23:54
 */
@Service
public class M3u8Handler {
    private static final Logger logger = LoggerFactory.getLogger(M3u8Handler.class);
    private static final Pattern pattern = Pattern.compile("https://[\\w\\./]+\\.ts");

    /**
     * 根据uri下载M3U8文件为mp3，返回文件目录
     *
     * @param uri
     * @return
     */
    public void down(String uri, String filePath) {
        logger.info("save {} -> {}", filePath, uri);
        M3u8Info m3u8Info = analyze(uri);
        Map<Integer, URI> uriMap = m3u8Info.getUriMap();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (OutputStream outputStream = Files.newOutputStream(file.toPath())) {

            int total = uriMap.size();
            AtomicInteger atomicInteger = new AtomicInteger(0);
            logger.info("start down ts file. total: {}", total);
            uriMap.entrySet().stream().parallel()
                    .map(Pair::of)
                    .map(Pair.valueMap(uriItem -> HttpHandler.exchangeGetBytes(uriItem).block()))
//                    .peek(pair -> logger.info("第{}文件下载完成！{}/{} ", pair.getKey(), atomicInteger.incrementAndGet(), total))

                    .map(Pair.valueMap(bytes -> this.decrypt(bytes, m3u8Info.getKey(), m3u8Info.getIv(), m3u8Info.getMethod())))
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(Pair::getKey))
                    .forEachOrdered(pair -> {
                        try {
                            outputStream.write(pair.getValue());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            outputStream.flush();
            logger.info("end down to {}", filePath);
        } catch (Exception e) {
            file.delete();
            logger.error("{}下载失败", uri, e);
        }
    }

    private M3u8Info analyze(String m3u8Uri) {
        String m3u8Str = HttpHandler.exchangeGetString(URI.create(m3u8Uri)).block();
        String relativeUrl = m3u8Uri.substring(0, m3u8Uri.lastIndexOf("/") + 1);
        M3u8Info info = new M3u8Info();
        info.setRelativeUrl(relativeUrl);
        String[] lines = m3u8Str.split("\\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            //如果含有此字段，则获取加密算法以及获取密钥的链接
            if (line.contains("EXT-X-KEY")) {
                this.getInfo(line, info, relativeUrl);
            }

            if (line.contains("#EXTINF")) {
                String nextLine = lines[++i];
                int endIndex = nextLine.lastIndexOf(".ts");
                int startIndex = nextLine.lastIndexOf("index") + 5;
                String index = nextLine.substring(startIndex, endIndex);

                if (!nextLine.startsWith("http")) {
                    nextLine = relativeUrl + nextLine;
                }
                info.addUri(Integer.parseInt(index), URI.create(nextLine));
            }
        }

        logger.info(info.toString());
        return info;
    }

    private void getInfo(String line, M3u8Info info, String relativeUrl) {
        String[] split = line.split(",");
        for (String s1 : split) {
            if (s1.contains("METHOD")) {
                info.setMethod(s1.split("=", 2)[1]);
                continue;
            }
            if (s1.contains("URI")) {
                String key = s1.split("=", 2)[1];
                key = key.replaceAll("\"", "");
                if (!key.startsWith("http")) {
                    key = relativeUrl + key;
                }
                info.setKey(HttpHandler.exchangeGetBytes(URI.create(key)).block());
                continue;
            }
            if (s1.contains("IV")) {
                String iv = s1.split("=", 2)[1];
                if (iv.startsWith("0x")) {
                    info.setIv(this.hex2Bytes(iv.substring(2)));
                } else {
                    info.setIv(iv.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }

    private byte[] decrypt(byte[] data, byte[] sKey, byte[] ivByte, String method) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(sKey, "AES");
            if (ivByte.length != 16) {
                ivByte = new byte[16];
            }
            //如果m3u8有IV标签，那么IvParameterSpec构造函数就把IV标签后的内容转成字节数组传进去
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return cipher.doFinal(data, 0, data.length);
        } catch (Exception e) {
            logger.error("一条记录解密失败！");
            return null;
        }
    }

    private byte[] hex2Bytes(String hexStr) {
        char[] chars = hexStr.toCharArray();
        byte[] bytes = new byte[(chars.length + 1) / 2];
        for (int i = chars.length - 1; i >= 0; i -= 2) {
            int index = i / 2;
            bytes[index] = hex2Byte(chars[i - 1], chars[i]);
        }
        return bytes;
    }

    private byte hex2Byte(char c1, char c2) {
        byte byte1 = (byte) (Character.digit(c1, 16) << 4);
        byte byte2 = (byte) Character.digit(c2, 16);
        return (byte) (byte1 + byte2);
    }

    private Stream<String> match(String m3u8) {

        Matcher matcher = pattern.matcher(m3u8);// 指定要匹配的字符串

        Stream.Builder<String> builder = Stream.<String>builder();

        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            builder.add(matcher.group());//获取当前匹配的值
        }

        return builder.build();
    }

}
