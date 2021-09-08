//package com.example.mybatisdemo.utils;
//
///**
// * @author
// * @version 1.0
// * @team 魔法城堡
// * @date 2021/4/19 11:39
// */
//public class PdfHandle {
//    // 定义关键字
//    private static String KEY_WORD = "张三";
//    // 定义返回值
//    private static float[] resu = null;
//    // 定义返回页码
//    private static int i = 0;
//
//    /*
//     * 返回关键字所在的坐标和页数 float[0] >> X; float[1] >> Y; float[2] >> page;
//     */
//
//    public static float[] getKeyWords(String filePath)
//    {
//        try
//        {
//            PdfReader pdfReader = new PdfReader(filePath);
//            int pageNum = pdfReader.getNumberOfPages();
//            System.out.println(pageNum);
//            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(
//                    pdfReader);
//
//            // 下标从1开始
//            for (i = 1; i <= pageNum; i++)
//            {
//                pdfReaderContentParser.processContent(i, new RenderListener()
//                {
//
//                    @Override
//                    public void renderText(TextRenderInfo textRenderInfo)
//                    {
//                        String text = textRenderInfo.getText();
//                        if (null != text && text.contains(KEY_WORD))
//                        {
//                            Float boundingRectange = textRenderInfo
//                                    .getBaseline().getBoundingRectange();
//                            resu = new float[3];
//                            System.out.println("======="+text);
//                            resu[0] = boundingRectange.x;
//                            resu[1] = boundingRectange.y;
//                            resu[2] = i;
//                        }
//                    }
//
//                    @Override
//                    public void renderImage(ImageRenderInfo arg0)
//                    {
//                    }
//
//                    @Override
//                    public void endTextBlock()
//                    {
//
//                    }
//
//                    @Override
//                    public void beginTextBlock()
//                    {
//                    }
//                });
//            }
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        return resu;
//    }
//
//}
