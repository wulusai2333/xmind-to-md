package com.yxy.to.md;

import java.io.IOException;
import java.util.function.Consumer;

public class ToMdUtils {

    private ToMdUtils() {
    }

    /**
     * 转 POS|Xmind MD
     * @param filePath
     * @param stringBuilderConsumer 输出到 stringBuilderConsumer （注意会多次调用）
     * @return
     *      @see ToMdInterface
     * @throws IOException
     */
    public static ToMdInterface toMD(String filePath, Consumer<StringBuilder> stringBuilderConsumer) throws IOException {
        // 获取后缀
        String[] split = filePath.split("\\.");
        String suffix = split[split.length - 1];

        // 获取具体实现
        ToMdInterface instance;
        if (suffix.equalsIgnoreCase("pos")) {
            // 检查是否为格式的pos文件
            if (isNewPosFile(filePath)) {
                instance = NewPosToMd.getInstance();
            } else {
                instance = PosToMd.getInstance();
            }
        } else {
            instance = XMindToMd.getInstance();
        }

        // 执行
        instance.toMD(filePath, stringBuilderConsumer);
        return instance;
    }

    /**
     * 简单检查是否为格式的pos文件
     * @param filePath
     * @return
     */
    private static boolean isNewPosFile(String filePath) {
        // 可以通过检查文件内容特征来判断
        // .pos文件通常具有特定的结构，比如有editorVersion字段等
        return filePath.endsWith(".pos");
    }
}
