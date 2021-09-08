package com.example.mybatisdemo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mybatisdemo.eneities.BasicStageWord;
import com.example.mybatisdemo.eneities.BasicWord;
import com.example.mybatisdemo.eneities.BasicWordStage;
import com.example.mybatisdemo.eneities.WordStage;
import com.example.mybatisdemo.service.BasicStageWordService;
import com.example.mybatisdemo.service.BasicWordService;
import com.example.mybatisdemo.service.WordStageService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/22 9:26
 *     private String wordID;
 *     private String wordStage;
 *     private String wordTimes;
 *     private String wordFrequency;
 *     private String wordCreater;
 *     private String createTime;
 *     private String wordChecker;
 *     private String checkTime;
 */
@RestController
public class WordStageController {
    @Autowired
    private WordStageService wordStageService;
    @Autowired
    private BasicWordService basicWordService;
    @Autowired
    private BasicStageWordService basicStageWordService;

    /**
     * 创建单词索引
     * @throws IOException
     */
    @RequestMapping("/createWordIndex")
    public void createIndexWord() throws Exception {
        //1、采集数据
        List<BasicStageWord> basicWordStages = basicStageWordService.getAllWordDetail();
        //2、创建Document文档对象
        List<Document> documents = new ArrayList<Document>();
        for (BasicStageWord basicWord : basicWordStages){
            Document document = new Document();
            // Document文档中添加Field域
            // Store.YES:表示存储到文档域中
            //主键id
            document.add(new StringField("id", String.valueOf(basicWord.getId()), Field.Store.YES));
            //单词ID
            document.add(new StringField("w_id",basicWord.getW_id(), Field.Store.YES));//w_id 单词id 不分词 索引 存储
            //单词拼写 (保存小写索引)TextField:  分词     StringField不分词
            document.add(new StringField("w_spell",basicWord.getW_spell(), Field.Store.YES));//w_spell 单词拼写 不分词 索引 存储
            //单词类型
            document.add(new StringField("w_type",basicWord.getW_type(), Field.Store.YES));//w_type 单词类型 不分词 索引 存储
            //单词意思
            document.add(new StringField("w_mean",basicWord.getW_mean(), Field.Store.YES));//w_mean 单词意思 不分词 索引 存储
            //单词音标
            document.add(new StringField("w_pronunce",basicWord.getW_pronunce(), Field.Store.YES));//w_pronunce 单词音标 不分词 索引 存储
            //单词级别
            document.add(new StringField("w_stage",basicWord.getW_stage(), Field.Store.YES));//w_stage 单词级别 不分词 索引 存储
            //单词词频
            document.add(new StringField("w_frequency",basicWord.getW_frequency(),Field.Store.YES));// 不分词 索引 存储
            documents.add(document);
        }
        long startTime = System.currentTimeMillis();
        // 3. 创建Analyzer分词器,分析文档，对文档进行分词
        Analyzer analyzer = new StandardAnalyzer();
        // 4. 创建Directory对象,声明索引库的位置
        Directory directory = MMapDirectory.open(Paths.get("E:\\word\\dir"));
        // 5. 创建IndexWriteConfig对象，写入索引需要的配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //控制写入一个新的segment前内存中保存的document的数目，设置较大的数目可以加快建索引速度。
        config.setMaxBufferedDocs(50000);
        // 6.创建IndexWriter写入对象
        IndexWriter indexWriter = new IndexWriter(directory, config);
        //设置100000个文档合并为一个段
        indexWriter.forceMerge(5000);
        // 7.写入到索引库，通过IndexWriter添加文档对象document
        for (Document doc : documents) {
            indexWriter.addDocument(doc);
        }
        // 8.释放资源
        indexWriter.close();
        long endTime = System.currentTimeMillis();
        System.out.println("======运行时间为:===" + (endTime - startTime) + "ms");
    }


    @RequestMapping("/selectIndexWord")
    public JSONObject lyyselectBasicAndStage(String word, String stage)throws Exception  {
        System.out.println("+++++进来lucene写的查询方法了+++++");
        JSONArray resultarray=new JSONArray();
        JSONObject resulttotal=new JSONObject();
        long startTime = System.currentTimeMillis();
        System.out.println("*******lyyselectBasicAndStage*********"+word);
        System.out.println("*******lyyselectBasicAndStage*********"+stage);

        //创建组合查询对象(组合查询对象)
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        BooleanQuery.setMaxClauseCount(20000);
        //创建分词器
        Analyzer analyzer = new StandardAnalyzer();
//        IKAnalyzer ikAnalyzer = new IKAnalyzer();  //对于英文单词，把所有字母转为小写(搜索时不区分大小写)

        try {
            //单词 小写 去查询
//            TermQuery querySpell =new TermQuery(new Term("w_spell", word));
            Query query1 = new QueryParser("w_spell",analyzer).parse(word);
            builder.add(query1, BooleanClause.Occur.MUST);
            TermQuery queryStage =new TermQuery(new Term("w_stage", stage));
            builder.add(queryStage,BooleanClause.Occur.MUST);
            //4. 创建Directory目录对象, 指定索引库的位置
            Directory directory = MMapDirectory.open(Paths.get("E:\\word\\dir"));
            //5. 创建输入流对象
            IndexReader reader = DirectoryReader.open(directory);
            //6. 创建搜索对象
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            //7. 搜索并获取搜索结果
            Integer num = 3;
            TopDocs topDocs =  indexSearcher.search(builder.build(), num);
            //8. 获取查询到的总条数
            System.out.println("+++++长度为+++++"+topDocs.totalHits);
            //9. 获取查询到的结果集（评分最高的totalnumber个结果）
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            //10. 遍历结果集封装返回的数据
            System.out.println("+++++++++++"+topDocs.scoreDocs.length);

            if(scoreDocs !=null) {
                for(int i= 0;i <topDocs.scoreDocs.length;i++) {
                    //通过查询到的文档编号, 找到对应的文档对象
                    Document document = reader.document(scoreDocs[i].doc);
                    //封装BasicWord对象
                    BasicStageWord basicStageWord = new BasicStageWord();
                    basicStageWord.setId(Integer.parseInt(document.get("id")));
                    basicStageWord.setW_id(document.get("w_id"));
                    basicStageWord.setW_mean(document.get("w_mean"));
                    basicStageWord.setW_spell(document.get("w_spell"));
                    basicStageWord.setW_pronunce(document.get("w_pronunce"));
                    basicStageWord.setW_type(document.get("w_type"));
                    basicStageWord.setW_stage(document.get("w_stage"));
                    basicStageWord.setW_frequency(document.get("w_frequency"));
                    resultarray.add(basicStageWord);
                }
            }
            resulttotal.put("result", resultarray);
            resulttotal.put("code", 200);
            resulttotal.put("message", "success");
        } catch (Exception e) {
            resulttotal.put("code", 501);
            resulttotal.put("message", "failed!has exception in Indexdatabase service!");
            e.printStackTrace();
            return resulttotal;
        }

        long endTime = System.currentTimeMillis();
        System.out.println("====消耗时间为=========" + (endTime - startTime) + "ms");
        return resulttotal;
    }




    @RequestMapping("/selectAll")
    public void selectLianhe(){
        List<BasicStageWord> basicWordStages = basicStageWordService.getAllWordDetail();
        System.out.printf(String.valueOf(basicWordStages.size()));
    }

}
