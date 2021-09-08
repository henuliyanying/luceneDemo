//package com.example.mybatisdemo.utils;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.Field.Store;
//import org.apache.lucene.document.StringField;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.index.IndexWriterConfig.OpenMode;
//import org.apache.lucene.index.Term;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.TermQuery;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.store.SimpleFSDirectory;
//import org.apache.lucene.util.Version;
//import com.mongodb.DBObject;//这是与数据库对应的mongodb数据库
///**
// * 创建数据索引
// *
// *[@author](http://weibo.com/n/author)liuyang
// *[@version](http://weibo.com/n/version)2015.08.06
// */
//public class BuildIndex {
//    private static String filePath = "C:/Users/365/Desktop/8月/Lucene/buildIndex";
//    private static String filePathAdd = "C:/Users/365/Desktop/8月/Lucene/buildIndex2";
//    /**
//     * 创建索引
//     */
//    public void buildIndex() {
//        try {
//// 如果文件夹不存在，则需要首次创建索引
//// 否则，只需增量索引
//            File file = new File(filePath);
//            if (!((file.exists()) && (file.listFiles().length > 1))) {
//                this.firstIndex();
//            } else {
//                this.updateIndex();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 初始化索引库indexWriter 一旦
//     * indexWriter创建完成，再改变IndexWriterConfig的配置，对indexWriter将不产生影响
//     *
//     *[@param](http://weibo.com/n/param)OpenModeType
//     *[@param](http://weibo.com/n/param)fileURL
//     *[@return](http://weibo.com/n/return)
//     */
//    private IndexWriter initLucene(OpenMode OpenModeType, String fileURL) {
//        try {
//// 创建分词器 analyzer
//// 对原有句子按照空格进行了分词 所有的大写字母都可以能转换为小写的字母
//// 可以去掉一些没有用处的单词，例如"is","the","are"等单词，也删除了所有的标点
//            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
//// 创建IndexWriterConfig
//// Windows系统用SimpleFSDirectory，其它系统用NIOFSDirectory
//            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
//                    Version.LUCENE_46, analyzer);
//            indexWriterConfig.setOpenMode(OpenModeType);
//// 创建目录
//            Directory fileDir = new SimpleFSDirectory(new File(fileURL));
//// 创建索引库
//            IndexWriter indexWriter = new IndexWriter(fileDir,
//                    indexWriterConfig);
//            return indexWriter;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    /**
//     * 第一次创建索引
//     */
//    private void firstIndex() {
//        IndexWriter indexWriter = null;
//        try {
//// 获取数据
//            Getdatas getdatas = new Getdatas();
//            List results = getdatas.getDatas();
//// 若数据为空或者不存在，则返回；否则添加索引
//            if ((results.size() == 0) || null == results) {
//                return;
//            } else {
//// 获取索引库
//                indexWriter = this.initLucene(OpenMode.CREATE_OR_APPEND,
//                        filePath);
//// 添加Fields
//                this.addFields(results, indexWriter);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//// 若第一次打开索引文件需要commint 否则会报no segment*
//            try {
//                if (null != indexWriter) {
//                    indexWriter.commit();
//                    indexWriter.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//    }
//    /**
//     * 更新索引 lucene本身不支持更新
//     *
//     * 通过删除索引然后再建立索引来更新
//     */
//    private void updateIndex() {
//        IndexReader indexReader = null;
//        IndexWriter indexWriterAdd = null;
//        try {
//// 获取数据
//            Getdatas getdatas = new Getdatas();
//            List results = getdatas.getDatas();
//            File fileAdd = new File(filePathAdd);
//// 如果文件夹不存在，创建
//            if (!fileAdd.exists()) {
//                fileAdd.mkdir();
//            }
//// 创建IndexReader
//            File file = new File(filePath);
//            Directory dir = FSDirectory.open(file);
//            indexReader = DirectoryReader.open(dir);
//            long startTime = System.currentTimeMillis();
//// 检索最新添加的数据是否索引
//            List updateDatas = new ArrayList();
//            for (DBObject updateData : results) {
//// 是否在索引库 标识符
//                boolean flag = this.isInIndex(updateData.get("_id").toString(),
//                        indexReader);
//                if (flag) {
//// 将不在索引库的数据，添加到updateDatas中
//                    updateDatas.add(updateData);
//                }
//            }
//            long endTime = System.currentTimeMillis();
//            System.out.println("剔除数据耗时：" + (endTime - startTime) + "ms");
//// 添加索引
//            if ((updateDatas.size() == 0) || (null == updateDatas)) {
//                return;
//            } else {
//// indexWriter.deleteAll();
//                if (!((file.exists()) && (file.listFiles().length > 3))) {
//                    return;
//                } else if (!((fileAdd.exists()) && (fileAdd.listFiles().length > 3))) {
//                    indexWriterAdd = this.initLucene(OpenMode.CREATE_OR_APPEND,
//                            filePathAdd);
//                    this.addFields(updateDatas, indexWriterAdd);
//                } else {
//                    indexWriterAdd = this.initLucene(OpenMode.CREATE,
//                            filePathAdd);
//                    this.addFields(updateDatas, indexWriterAdd);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != indexWriterAdd) {
//                    indexWriterAdd.commit();
//// indexWriterAdd.close();
//                }
//                if (null != indexReader) {
//                    indexReader.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//    }
//    /**
//     * 添加Fields
//     *
//     *[@param](http://weibo.com/n/param)results
//     *[@param](http://weibo.com/n/param)indexWriter
//     */
//    private void addFields(List results, IndexWriter indexWriter) {
//        try {
//            long startTime = System.currentTimeMillis();
//            for (int i = 0; i < results.size(); i++) {
//// 创建Document
//                Document doc = new Document();
//// 创建Field
//                Field idField = new StringField("_id", results.get(i).toMap()
//                        .get("_id").toString(), Store.YES);
//                Field countdateField = new StringField("countdate", results
//                        .get(i).toMap().get("countdate").toString(), Store.YES);
//                Field averpriceField = new LongField("averprice",
//                        (Long) results.get(i).toMap().get("averprice"),
//                        Store.YES);
//                Field countField = new IntField("count", (Integer) results
//                        .get(i).toMap().get("count"), Store.YES);
//                Field appField = new StringField("app", results.get(i).toMap()
//                        .get("app").toString(), Store.YES);
//// 添加索引
//                doc.add(idField);
//                doc.add(countdateField);
//                doc.add(averpriceField);
//                doc.add(countField);
//                doc.add(appField);
//// 将索引添加到实时中去
//                indexWriter.addDocument(doc);
//            }
//            long endTime = System.currentTimeMillis();
//            System.out.println("创建索引耗时：" + (endTime - startTime) + "ms");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//// 若第一次打开索引文件需要commint 否则会报no segment*
//            try {
//                if (null != indexWriter) {
//                    indexWriter.commit();
//// indexWriter.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//    }
//    /**
//     * 检查数据是否在索引库中
//     *
//     *[@param](http://weibo.com/n/param)id
//     *[@param](http://weibo.com/n/param)p_indexReader
//     *[@return](http://weibo.com/n/return)
//     */
//    private boolean isInIndex(String id, IndexReader p_indexReader) {
//        boolean flag = true;
//        try {
//            for (int i = 0; i < p_indexReader.numDocs(); i++) {
//                Document doc = p_indexReader.document(i);
//                if (id.equals(doc.get("_id"))) {
//                    flag = false;
//                    return flag;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return flag;
//    }
//    /**
//     * 查询
//     */
//    public void search(String path) {
//        IndexReader indexReader2 = null;
//        try {
//            File file = new File(path);
//            if (!((file.exists()) && (file.listFiles().length < 2))) {
//                Directory dir = FSDirectory.open(new File(path));
//                indexReader2 = DirectoryReader.open(dir);
//                IndexSearcher searcher = new IndexSearcher(indexReader2);
//                TermQuery query = new TermQuery(new Term("app", "test1"));
//                TopDocs hits = searcher.search(query, 10000);
//                System.out
//                        .println("total " + indexReader2.maxDoc() + " datas!");
//// for (ScoreDoc scoreDoc : hits.scoreDocs) {
//// Document doc = searcher.doc(scoreDoc.doc);
//// System.out.println("_id:" + doc.get("_id") +
//// "----countdate:"
//// + doc.get("countdate") + "----averprice:"
//// + doc.get("averprice") + "----count:"
//// + doc.get("count") + "----app:" + doc.get("app"));
//// }
//                System.out.println("find " + hits.scoreDocs.length
//                        + " results!");
//            } else {
//                return;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != indexReader2) {
//                    indexReader2.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
