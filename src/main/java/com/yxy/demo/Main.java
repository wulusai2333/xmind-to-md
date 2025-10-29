package com.yxy.demo;

import com.yxy.to.md.ToMdUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private Main() {
    }

    public static void main(String[] args) throws IOException {
        // xmind
//        ToMdUtils.toMD(
//                "C:\\Users\\xxx\\Downloads\\Redis-test.xmind",
//                i -> System.out.print(i.toString())
//        );

        // pos 
        ToMdUtils.toMD(
                "D:\\xxx\\xmind-to-md\\src\\main\\resources\\TL.pos",
                i -> writeToFile(i.toString(), "D:\\xxx\\xmind-to-md\\src\\main\\resources\\TL.md")
        );
    }
    
    /**
     * 将指定内容追加写入到文件中
     * 
     * @param content 要写入的内容
     * @param filePath 目标文件路径
     */
    private static void writeToFile(String content, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.append(content);
        } catch (IOException e) {
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }
}