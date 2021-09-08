//package com.example.mybatisdemo.utils;
//
//
//import com.spire.pdf.*;
//import java.awt.*;
//import com.spire.pdf.general.find.PdfTextFind;
//
//
//
///**
// * @author
// * @version 1.0
// * @team 魔法城堡
// * @date 2021/4/19 11:50
// */
//public class findAndHighlightText {
//    public static void main(String[] args) throws Exception {
////加载示例PDF文档
//
//        PdfDocument pdf = new PdfDocument();
//
//        pdf.loadFromFile("D:\\TeachingPlan.pdf");
//
//        PdfTextFind[] result = null;
//
////遍历文档每一页
//
//        for (int i = 0; i < pdf.getPages().getCount(); i++) {
////获取特定页
//
//            PdfPageBase page = pdf.getPages().get(i);
//
//            result = page.findText("good").getFinds();
//
//            for (PdfTextFind find : result) {
////高亮显示查找结果
//
//                find.applyHighLight(Color.yellow);
//
//            }
//
////保存文档
//
//            pdf.saveToFile("result.pdf");
//
//            pdf.close();
//
//        }
//
//    }
//
//
//}
