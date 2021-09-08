//package com.example.mybatisdemo.utils;
//
///**
// * @author
// * @version 1.0
// * @team 魔法城堡
// * @date 2021/4/19 11:41
// */
//public class PdfHandleTest {
//    //    @SuppressWarnings("deprecation")
//
//
//    public static final String SRC = "C:/Users/tym/Desktop/template.pdf";
//    public static final String DEST = "C:/Users/tym/Desktop/test1.pdf";
//    public static void main(String[] args) throws IOException, DocumentException {
//        File file = new File(DEST);
//        file.getParentFile().mkdirs();
//        new PdfConversion1().manipulatePdf(SRC, DEST);
//    }
//
//    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
//        PdfReader reader = new PdfReader(src);
//        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
//        float[] result = PdfConversion.getKeyWords(src); //1
//        PdfContentByte canvas = stamper.getOverContent((int) result[2]);
////        float height=595;
////        System.out.println(canvas.getHorizontalScaling());
//        float x,y;
//        x= result[0];//2
//        y = result[1];//3
//        canvas.saveState();
//        canvas.setColorFill(BaseColor.WHITE);
//        canvas.rectangle(x, y, 40, 20);//设置覆盖面的大小
//
//        canvas.fill();
//        canvas.restoreState();
//        //开始写入文本
//        canvas.beginText();
//        //BaseFont bf = BaseFont.createFont(URLDecoder.decode(CutAndPaste.class.getResource("/AdobeSongStd-Light.otf").getFile()), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
//        Font font = new Font(bf,10,Font.BOLD);
//        //设置字体和大小
//        canvas.setFontAndSize(font.getBaseFont(), 15);
//        //设置字体的输出位置
//        canvas.setTextMatrix(x, y-1);
//        //要输出的text
//        canvas.showText("田田田" );
//
//        //设置字体的输出位置
//        canvas.setFontAndSize(font.getBaseFont(), 20);
//        canvas.setTextMatrix(x, y-90);
//        //要输出的text
//        canvas.showText("多退少补" );
//
//        canvas.endText();
//        stamper.close();
//        reader.close();
//        System.out.println("complete");
//    }
//
//}
