package com.yxy.to.md;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxy.to.md.core.AbstractToMD;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 格式.pos文件转换为Markdown工具类
 *
 * @author lingma
 */
public class NewPosToMd extends AbstractToMD implements ToMdInterface {

    private static final NewPosToMd INSTANCE = new NewPosToMd();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private NewPosToMd() {
    }

    public static NewPosToMd getInstance() {
        return INSTANCE;
    }

    /**
     * @param filePath              .pos 文件位置
     * @param stringBuilderConsumer 处理响应的函数（注意会多次调用）
     * @throws IOException
     */
    @Override
    public void toMD(String filePath, Consumer<StringBuilder> stringBuilderConsumer) throws IOException {
        String json = IOUtil.toString(new FileInputStream(new File(filePath)));

        JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
        processNode(jsonNode.get("children"), 0, stringBuilderConsumer);
    }

    private void processNode(JsonNode node, int level, Consumer<StringBuilder> consumer) {
        if (node == null || !node.isArray()) {
            return;
        }

        for (JsonNode child : node) {
            processChildNode(child, level, consumer);
        }
    }

    private void processChildNode(JsonNode node, int level, Consumer<StringBuilder> consumer) {
        if (node == null) {
            return;
        }

        String title = node.has("title") ? node.get("title").asText() : "";
        // 处理标题中的HTML标签
        title = title.replaceAll("<br>", "")
                .replaceAll("<b>", "")
                .replaceAll("</b>", "")
                .replaceAll("&nbsp;", " ");

        // 获得MD语法
        GetTop get = new GetTop(level, title).invoke();

        // 处理超链接
        Optional.ofNullable(node.get("link"))
                .ifPresent(link -> {
                    if (link.has("value")) {
                        setLink(link.get("value").asText(), get);
                    }
                });

        StringBuilder str = new StringBuilder();
        // 处理标题
        addThis(get, str, level);

        // 处理笔记/注释
        if (node.has("note")) {
            String note = node.get("note").asText();
            if (!note.isEmpty()) {
                // 添加适当的缩进
                String indent = "";
                for (int i = 0; i < level + 1; i++) {
                    indent += "  ";
                }
                str.append(indent).append("> ").append(note.replace("\n", "\n> ")).append("\n\n");
            }
        }

        // 调用处理函数
        consumer.accept(str);

        // 递归处理子节点
        if (node.has("children")) {
            processNode(node.get("children"), level + 1, consumer);
        }
    }
}
