package com.example.mybatisdemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mybatisdemo.eneities.Question;
import com.example.mybatisdemo.eneities.ResultModel;
import com.example.mybatisdemo.eneities.StuQuestion;
import com.example.mybatisdemo.service.QuestionService;
import com.example.mybatisdemo.service.StuQuestionService;
import com.example.mybatisdemo.utils.MyEnTokenizeStopStemUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tartarus.snowball.ext.PorterStemmer;


import java.io.File;
import java.io.IOException;


import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/3/17 15:55
 */
@RestController
public class questionController {

    @Autowired
    private StuQuestionService stuQuestionService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/getQuestionByS_num")
    public List<StuQuestion> sayHello(){
        List<StuQuestion> stuQuestions = stuQuestionService.getQuestionByS_num("41100016103");
        return stuQuestions;
    }

    @RequestMapping("/getAllQuestion")
    public List<Question> saySad(){
        List<Question> questions = questionService.getAllQuestion();
        return questions;
    }

    /**
     * 创建索引
     * @throws IOException
     */
    @RequestMapping("/createIndex")
    public void createIndexCompareTest() throws IOException {
        // 1. 采集数据
        List<Question> questionList = questionService.getAllQuestion();
        // 2. 创建Document文档对象
        List<Document> documents = new ArrayList<Document>();
        for (Question question : questionList) {
            Document document = new Document();
            // Document文档中添加Field域
            // Store.YES:表示存储到文档域中
            // 题目Id  不分词，索引，存储
//            document.add(new StringField("id", String.valueOf(question.getId()), Field.Store.YES));
            // 题目编号  不分词，索引，存储
            document.add(new StringField("q_num", question.getQ_num(), Field.Store.YES));
            // 题目类型  不分词，索引，存储
            document.add(new StringField("q_single_type", question.getQ_single_type(),
                    Field.Store.YES));
            // 是否是真题  不分词，索引，存储
//            document.add(new StringField("q_isreal", question.getQ_isreal().toString(),
//                    Field.Store.YES));
            // 题目平台 不分词，索引，存储
            document.add(new StringField("q_stage", question.getQ_stage(),
                    Field.Store.YES));
            // 题目内容  分词，索引，不存储
            document.add(new TextField("q_content", question.getQ_content(),
                    Field.Store.YES));
            // 题目难度  分词，索引，存储
            document.add(new IntPoint("q_difficulty", question.getQ_difficulty()));
            document.add(new StoredField("q_difficulty",question.getQ_difficulty()));
            // 题目创造者  不分词，索引，存储
//            document.add(new StringField("q_creater", question.getQ_creater(), Field.Store.YES));
            // 把Document放到list中
            documents.add(document);
        }

        long startTime = System.currentTimeMillis();
        // 3. 创建Analyzer分词器,分析文档，对文档进行分词
        Analyzer analyzer = new StandardAnalyzer();
        // 4. 创建Directory对象,声明索引库的位置
        Directory directory = MMapDirectory.open(Paths.get("E:\\dir"));
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





//    //每页查询3条数据
    public final static Integer PAGE_SIZE = 3;
//
    /**
     * 查询指定条件的题目
     * @param s_num 学生ID
     * @param queryString 关键字
     * @param price 难易度
     * @param stage 平台
     * @param questiontype 题目类型
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/query")
    public ResultModel query(String s_num, String queryString, String price, String stage, String questiontype, Integer page,String type) throws Exception {
//        while (true){
            long startTime = System.currentTimeMillis();

            //1. 需要使用的对象封装
            ResultModel resultModel = new ResultModel();
            //从第几条开始查询
            int start = (page - 1) * PAGE_SIZE;
            //查询到多少条为止
            Integer end = page * PAGE_SIZE;
            //创建分词器
            Analyzer analyzer = new StandardAnalyzer();
            //创建组合查询对象(组合查询对象)
            BooleanQuery.Builder builder = new BooleanQuery.Builder();

            //2. 根据查询关键字封装查询对象
            Query query1 = null;

            queryString= queryString.replace(',', ' ');
            queryString= queryString.replace('"', ' ');
            String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
//        String str1 = "上[]海";
            String newString = queryString.replaceAll(regEx, " ");
            System.out.println("***********"+newString);
            System.out.println("****************");
            query1 = new QueryParser("q_content",analyzer).parse(queryString);
            //将关键字查询对象, 封装到组合查询对象中 ，must相当于and ，
            // 并且的关系；should相当于或者的关系
            //must not 相当于非
            builder.add(query1, BooleanClause.Occur.MUST);

            //3. 根据价格范围封装查询对象
            String[] split = price.split("-");
            Query query2 = IntPoint.newRangeQuery("q_difficulty", Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            //将价格查询对象, 封装到组合查询对象中
            builder.add(query2, BooleanClause.Occur.MUST);
            //
            Query query3 = null;
            query3 = new QueryParser("q_stage",analyzer).parse(stage);
            builder.add(query3, BooleanClause.Occur.MUST);

            //查询指定题型的题目，利用PrefixQuery（查询以 第二个参数 为开头的结果）
            Query query4 = new PrefixQuery(new Term("q_num", questiontype));
            builder.add(query4, BooleanClause.Occur.MUST);

            List<StuQuestion> str = stuQuestionService.getQuestionByS_num(s_num);
            String questions = str.get(0).getQuestions();
            //将String字符转为Json对象
            JSONObject jsonObject = JSON.parseObject(questions);
            //获取嵌套中的json串,细心观察 content为json数组，里面可放多个json对象
            JSONArray jsonArray = jsonObject.getJSONArray("TP");
//        System.out.println("数组的长度"+jsonArray.size());
            //定义StringBuilder对象
            StringBuilder sb1 = new StringBuilder();
            //添加数据，链式
            for (int i=0;i<jsonArray.size();i++){
                String res = jsonArray.getJSONObject(i).getString("QuestionID");
                Query query5 = null;
                query5 = new QueryParser("q_num",analyzer).parse(res);
//            System.out.println("*****query5******"+query5);
                builder.add(query5, BooleanClause.Occur.MUST_NOT);
            }

        Query query6 = null;
        query6 = new QueryParser("q_single_type",analyzer).parse(type);
        builder.add(query6, BooleanClause.Occur.MUST);
            //4. 创建Directory目录对象, 指定索引库的位置
            /**
             * 使用MMapDirectory消耗的查询时间
             * ====消耗时间为=========324ms
             * ====消耗时间为=========18ms
             */
            Directory directory = MMapDirectory.open(Paths.get("E:\\dir"));
            //5. 创建输入流对象
            IndexReader reader = DirectoryReader.open(directory);
            //6. 创建搜索对象
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            //7. 搜索并获取搜索结果
            TopDocs topDocs = indexSearcher.search(builder.build(), end);
            //8. 获取查询到的总条数
            resultModel.setRecordCount(topDocs.totalHits);
            System.out.println("+++++长度为+++++"+topDocs.totalHits);
            //9. 获取查询到的结果集
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            System.out.println("+++++topDocs.scoreDocs长度为+++++"+topDocs.scoreDocs.length);
//            if (topDocs.totalHits < 15){
//                //中断函数，扩大范围 设置更广的查询
//                break;//退出循环，执行下面的语句
//            }else{
                //10. 遍历结果集封装返回的数据
                List<Question> questionList = new ArrayList<>();
                if (scoreDocs != null) {
                    for (int i = start; i < topDocs.scoreDocs.length; i ++) {
                        //通过查询到的文档编号, 找到对应的文档对象
                        Document document = reader.document(scoreDocs[i].doc);
                        //封装Sku对象
                        Question question = new Question();
//                question.setId(Integer.parseInt(document.get("id")));
                        question.setQ_num(document.get("q_num"));
                        question.setQ_single_type(document.get("q_single_type"));
//                question.setQ_isreal(Integer.parseInt(document.get("q_isreal")));
                        question.setQ_stage(document.get("q_stage"));
                        question.setQ_content(document.get("q_content"));
                        question.setQ_difficulty(Integer.parseInt(document.get("q_difficulty")));
//                question.setQ_creater(document.get("q_creater"));
                        questionList.add(question);
                    }
                }
//        System.out.println("list数据"+questionList+questionList.size());
                //封装查询到的结果集
                resultModel.setQuestionList(questionList);
                //封装当前页
                resultModel.setCurPage(page);
                //总页数
                Long pageCount = topDocs.totalHits % PAGE_SIZE > 0 ? (topDocs.totalHits/PAGE_SIZE) + 1 : topDocs.totalHits/PAGE_SIZE;
                resultModel.setPageCount(pageCount);
                long endTime = System.currentTimeMillis();
                System.out.println("====消耗时间为=========" + (endTime - startTime) + "ms");

                return resultModel;

            }
//        }
//        System.out.println("由break跳出来的循环");

//    }

    @RequestMapping("/testFenci")
    public void testFenci(){
        long startime = System.currentTimeMillis();
        Set<String> list = enTokenizeStopStem(" 根据短文内容，从短文后的选项中选出能填入空白处的最佳选项 。\n" +
                "    A. They could enjoy simple things in their lives.\n" +
                "    B. In other words,we make our own happiness\n" +
                "    C. The first secrect of happiness is to enjoy the simple things in life.\n" +
                "    D. If you want to feel happier,do good things to help other people.\n" +
                "    E. The second secrect to living a happy life is to be active. ");
        long endtime = System.currentTimeMillis();
        System.out.println(list);
//        String string = "abc123zxc56qwer89ws5";
//        System.out.println(string .replaceAll("\\d+",""));
        System.out.println("******"+(endtime-startime)+"s");
    }




    public static Set<String> enTokenizeStopStem(String enText){
        Set<String> lastList = new HashSet<>();
        //分词
        Set<String> list = enTokenize(enText);
        String word;
        Iterator<String> iter = list.iterator();
        while(iter.hasNext()){
            word = iter.next();
            //去除停用词：判断是不是停用词，是就删除
            if(MyEnTokenizeStopStemUtil.EnStopwords.isStopword(word)){
                continue;
            }
            lastList.add(word);
        }
        return lastList;
    }

    /**
     * 参考: https://blog.csdn.net/selg1984/article/details/5691414
     * 英文分词
     * 用 非字符 进行分词，分词后每个单词开头的大写会变小写
     */
    public static Set<String> enTokenize(String source){
        Set<String> set = new HashSet<>();
        StringTokenizer str = new StringTokenizer(source,":,\\\" ?!.()____\\t\\n\\r\\f（）……“ :");
        while (str.hasMoreElements()) {
            String res = str.nextToken();
//            System.out.println(res.indexOf("'") != -1);
            if(res.indexOf("'") != -1){
                continue;
            }else{
                set.add(res);
            }
        }
        return set;
    }
    public static Set<String> enTokenize1(String source){
        Set<String> set = new HashSet<>();
        StringTokenizer str = new StringTokenizer(source);
        while (str.hasMoreElements()) {
            String res = str.nextToken();
//            System.out.println(res.indexOf("'") != -1);
            if(res.indexOf("'") != -1){
                continue;
            }else{
                set.add(res);
            }
        }
        return set;
    }
    /**
     * 参考：https://stackoverflow.com/questions/5391840/stemming-english-words-with-lucene
     * 英文 单词 的词干提取
     * 算法: snowball
     * 使用的jar包 lucene-analyzers-smartcn-7.6.0.jar  (包含中英词干提取)
     * 百度网盘共享地址: https://pan.baidu.com/s/15D33Qi88n5S0fAl7Uyudwg    提取码：adhd
     * maven 地址:
     *<dependency>
     *     <groupId>org.apache.lucene</groupId>
     *     <artifactId>lucene-analyzers-smartcn</artifactId>
     *     <version>7.6.0</version>
     * </dependency>
     *
     * 在英语中，一个单词常常是另一个单词的“变种”，如：happy=>happiness，这里happy叫做happiness的词干（stem）。
     * 在信息检索系统中，我们常常做的一件事，就是在Term规范化过程中，提取词干（stemming），即除去英文单词分词变换形式的结尾
     *
     * 对一个'英语单词'进行词干提取
     * 比如 having 提取后就是have ，binded 提取后就是bind
     * 注：不是所有的都能正确提取 ，如 had 提取后还是 had ， happy 和 happiness 提取后都是 happi (算法问题，不同算法结果不一样)
     *
     * 强调：
     * 提取词干的用法：如果你想要拿提取词干的内容去数据库搜索，搜索的字段也必须是词干提取后的内容，不要在原内容字段搜索
     * (这个过程有点像非对称加密校验一样，比较加密后的内容)
     * @param englishWord
     * @return
     */
    public static String enWordStem(String englishWord) {
        PorterStemmer stemmer = new PorterStemmer();
        stemmer.setCurrent(englishWord);
        if(stemmer.stem()){
            return stemmer.getCurrent();
        }
        return englishWord;
    }

    /**
     * 来源：https://blog.csdn.net/qy20115549/article/details/80684455?utm_source=blogxgwz1
     * 工具类 : 去除英文停用词
     */
    public static class EnStopwords {

        /** The hash set containing the list of stopwords */
        protected HashSet m_Words = null;

        /** The default stopwords object (stoplist based on Rainbow) */
        protected static MyEnTokenizeStopStemUtil.EnStopwords m_Stopwords;

        //下面这一小部分是静态代码块，他是自动执行的。而静态方法则是被动执行，需要使用类名来调用。其次静态代码块先于构造函数执行
        //具体可参见网址http://www.cnblogs.com/panjun-Donet/archive/2010/08/10/1796209.html
        static {
            if (m_Stopwords == null) {
                m_Stopwords = new MyEnTokenizeStopStemUtil.EnStopwords();
            }
        }

        /**
         * initializes the stopwords (based on <a href="http://www.cs.cmu.edu/~mccallum/bow/rainbow/" target="_blank">Rainbow</a>).
         */
        //这个是构造函数
        public EnStopwords() {
            m_Words = new HashSet();

            //Stopwords list from Rainbow
            add("a");
            add("able");
            add("about");
            add("above");
            add("according");
            add("accordingly");
            add("across");
            add("actually");
            add("after");
            add("afterwards");
            add("again");
//    add("against");
            add("all");
//    add("allow");
//    add("allows");
//    add("almost");
            add("alone");
            add("along");
            add("already");
            add("also");
//    add("although");
            add("always");
            add("am");
            add("among");
            add("amongst");
            add("an");
            add("and");
            add("another");
            add("any");
            add("anybody");
            add("anyhow");
            add("anyone");
            add("anything");
            add("anyway");
            add("anyways");
            add("anywhere");
            add("apart");
            add("appear");
//    add("appreciate");
            add("appropriate");
            add("are");
            add("around");
            add("as");
            add("aside");
            add("ask");
            add("asking");
            add("associated");
            add("at");
            add("available");
            add("away");
//    add("awfully");
            add("b");
            add("be");
            add("became");
            add("because");
            add("become");
            add("becomes");
            add("becoming");
            add("been");
            add("before");
            add("beforehand");
            add("behind");
            add("being");
            add("believe");
            add("below");
            add("beside");
            add("besides");
//    add("best");
//    add("better");
            add("between");
            add("beyond");
            add("both");
            add("but");
            add("brief");
            add("by");
            add("c");
            add("came");
            add("can");
            add("certain");
            add("certainly");
            add("clearly");
            add("co");
            add("com");
            add("come");
            add("comes");
            add("contain");
            add("containing");
            add("contains");
            add("corresponding");
            add("could");
            add("course");
            add("currently");
            add("d");
            add("definitely");
            add("described");
            add("despite");
            add("did");
            add("different");
            add("do");
            add("does");
            add("doing");
            add("done");
            add("down");
            add("downwards");
            add("during");
            add("e");
            add("each");
            add("edu");
            add("eg");
            add("eight");
            add("either");
            add("else");
            add("elsewhere");
            add("enough");
            add("entirely");
            add("especially");
            add("et");
            add("etc");
            add("even");
            add("ever");
            add("every");
            add("everybody");
            add("everyone");
            add("everything");
            add("everywhere");
            add("ex");
            add("exactly");
            add("example");
            add("except");
            add("f");
            add("far");
            add("few");
            add("fifth");
            add("first");
            add("five");
            add("followed");
            add("following");
            add("follows");
            add("for");
            add("former");
            add("formerly");
            add("forth");
            add("four");
            add("from");
            add("further");
            add("furthermore");
            add("g");
            add("get");
            add("gets");
            add("getting");
            add("given");
            add("gives");
            add("go");
            add("goes");
            add("going");
            add("gone");
            add("got");
            add("gotten");
//    add("greetings");
            add("h");
            add("had");
            add("happens");
//    add("hardly");
            add("has");
            add("have");
            add("having");
            add("he");
            add("hello");
            add("help");
            add("hence");
            add("her");
            add("here");
            add("hereafter");
            add("hereby");
            add("herein");
            add("hereupon");
            add("hers");
            add("herself");
            add("hi");
            add("him");
            add("himself");
            add("his");
            add("hither");
//    add("hopefully");
            add("how");
            add("howbeit");
            add("however");
            add("i");
            add("ie");
            add("if");
//    add("ignored");
            add("immediate");
            add("in");
            add("inasmuch");
            add("inc");
            add("indeed");
            add("indicate");
            add("indicated");
            add("indicates");
            add("inner");
            add("insofar");
            add("instead");
            add("into");
            add("inward");
            add("is");
            add("it");
            add("its");
            add("itself");
            add("j");
            add("just");
            add("k");
            add("keep");
            add("keeps");
            add("kept");
            add("know");
            add("knows");
            add("known");
            add("l");
            //add("last");
            add("lately");
            add("later");
            add("latter");
            add("latterly");
            add("least");
            add("less");
            add("lest");
            add("let");
            add("like");
            add("liked");
            add("likely");
            add("little");
            add("ll"); //added to avoid words like you'll,I'll etc.
            add("look");
            add("looking");
            add("looks");
            add("ltd");
            add("m");
            add("mainly");
            add("many");
            add("may");
            add("maybe");
            add("me");
//    add("mean");
            add("meanwhile");
//    add("merely");
            add("might");
            add("more");
            add("moreover");
            add("most");
            add("mostly");
            add("much");
            add("must");
            add("my");
            add("myself");
            add("n");
            add("name");
            add("namely");
            add("nd");
            add("near");
            add("nearly");
            add("necessary");
            add("need");
            add("needs");
//    add("neither");
//    add("never");
//    add("nevertheless");
            add("new");
            add("next");
            add("nine");
            add("normally");
//    add("novel");
            add("no");
            add("nobody");
            add("non");
            add("none");
            add("noone");
            add("nor");
            add("normally");
            add("not");
            add("n't");
            add("nothing");
            add("novel");
            add("now");
            add("nowhere");
            add("now");
            add("nowhere");
            add("o");
            add("obviously");
            add("of");
            add("off");
            add("often");
            add("oh");
            add("ok");
            add("okay");
//    add("old");
            add("on");
            add("once");
            add("one");
            add("ones");
            add("only");
            add("onto");
            add("or");
            add("other");
            add("others");
            add("otherwise");
            add("ought");
            add("our");
            add("ours");
            add("ourselves");
            add("out");
            add("outside");
            add("over");
            add("overall");
            add("own");
            add("p");
            add("particular");
            add("particularly");
            add("per");
            add("perhaps");
            add("placed");
            add("please");
            add("plus");
            add("possible");
            add("presumably");
            add("probably");
            add("provides");
            add("q");
            add("que");
            add("quite");
            add("qv");
            add("r");
            add("rather");
            add("rd");
            add("re");
            add("really");
            add("reasonably");
            add("regarding");
            add("regardless");
            add("regards");
            add("relatively");
            add("respectively");
            add("right");
            add("s");
            add("said");
            add("same");
            add("saw");
            add("say");
            add("saying");
            add("says");
            add("second");
            add("secondly");
            add("see");
            add("seeing");
//    add("seem");
//    add("seemed");
//    add("seeming");
//    add("seems");
            add("seen");
            add("self");
            add("selves");
            add("sensible");
            add("sent");
            // add("serious");
            // add("seriously");
            add("seven");
            add("several");
            add("shall");
            add("she");
            add("should");
            add("since");
            add("six");
            add("so");
            add("some");
            add("somebody");
            add("somehow");
            add("someone");
            add("something");
            add("sometime");
            add("sometimes");
            add("somewhat");
            add("somewhere");
            add("soon");
            add("sorry");
            add("specified");
            add("specify");
            add("specifying");
            add("still");
            add("sub");
            add("such");
            add("sup");
            add("sure");
            add("t");
            add("take");
            add("taken");
            add("tell");
            add("tends");
            add("th");
            add("than");
            add("thank");
            add("thanks");
            add("thanx");
            add("that");
            add("thats");
            add("the");
            add("their");
            add("theirs");
            add("them");
            add("themselves");
            add("then");
            add("thence");
            add("there");
            add("thereafter");
            add("thereby");
            add("therefore");
            add("therein");
            add("theres");
            add("thereupon");
            add("these");
            add("they");
            add("think");
            add("third");
            add("this");
            add("thorough");
            add("thoroughly");
            add("those");
            add("though");
            add("three");
            add("through");
            add("throughout");
            add("thru");
            add("thus");
            add("to");
            add("together");
            add("too");
            add("took");
            add("toward");
            add("towards");
            add("tried");
            add("tries");
            add("truly");
            add("try");
            add("trying");
            add("twice");
            add("two");
            add("u");
            add("un");
            add("under");
//    add("unfortunately");
//    add("unless");
//    add("unlikely");
            add("until");
            add("unto");
            add("up");
            add("upon");
            add("us");
            add("use");
            add("used");
//    add("useful");
            add("uses");
            add("using");
            add("usually");
            add("uucp");
            add("v");
            add("value");
            add("various");
            add("ve"); //added to avoid words like I've,you've etc.
            add("very");
            add("via");
            add("viz");
            add("vs");
            add("w");
            add("want");
            add("wants");
            add("was");
//    add("way");
            add("we");
            add("welcome");
//    add("well");
            add("went");
            add("were");
            add("what");
//    add("whatever");
            add("when");
            add("whence");
            add("whenever");
            add("where");
            add("whereafter");
            add("whereas");
            add("whereby");
            add("wherein");
            add("whereupon");
            add("wherever");
            add("whether");
            add("which");
            add("while");
            add("whither");
            add("who");
            add("whoever");
            add("whole");
            add("whom");
            add("whose");
            add("why");
            add("will");
            add("willing");
            add("wish");
            add("with");
            add("within");
            add("without");
            add("wonder");
            add("would");
            add("would");
            add("x");
            add("y");
//    add("yes");
            add("yet");
            add("you");
            add("your");
            add("yours");
            add("yourself");
            add("yourselves");
            add("z");
            add("zero");
            // add new
            add("i'm");
            add("he's");
            add("she's");
            add("you're");
            add("i'll");
            add("you'll");
            add("she'll");
            add("he'll");
            add("it's");
            add("don't");
            add("can't");
            add("didn't");
            add("i've");
            add("that's");
            add("there's");
            add("isn't");
            add("what's");
            add("rt");
            add("doesn't");
            add("w/");
            add("w/o");
        }

        /**
         * removes all stopwords
         */
        public void clear() {
            m_Words.clear();
        }

        /**
         * adds the given word to the stopword list (is automatically converted to
         * lower case and trimmed) trim的意思是将空格去掉
         *
         * @param word： the word to add
         */
        //将给定的词添加进停用词列表
        public void add(String word) {
            if (word.trim().length() > 0)
                m_Words.add(word.trim().toLowerCase());
        }

        /**
         * removes the word from the stopword list
         *
         * @param word： the word to remove
         * @return true if the word was found in the list and then removed
         */
        public boolean remove(String word) {
            return m_Words.remove(word);
        }

        /**
         * Returns a sorted enumeration over all stored stopwords
         *
         * @return the enumeration over all stopwords
         */
        public Enumeration elements() {
            Iterator iter;
            Vector      list;

            iter = m_Words.iterator();
            list = new Vector();

            while (iter.hasNext())
                list.add(iter.next());

            // sort list
            Collections.sort(list);

            return list.elements();
        }

        /**
         * Generates a new Stopwords object from the given file
         *
         * @param filename： the file to read the stopwords from
         * @throws Exception if reading fails
         */
        public void read(String filename) throws Exception {
            read(new File(filename));
        }

        /**
         * Generates a new Stopwords object from the given file
         *
         * @param file： the file to read the stopwords from
         * @throws Exception if reading fails
         */
        public void read(File file) throws Exception {
            read(new BufferedReader(new FileReader(file)));
        }

        /**
         * Generates a new Stopwords object from the reader. The reader is
         * closed automatically.
         *
         * @param reader： the reader to get the stopwords from
         * @throws Exception if reading fails
         */
        public void read(BufferedReader reader) throws Exception {
            String   line;

            clear();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // comment?
                if (line.startsWith("#"))
                    continue;
                add(line);
            }

            reader.close();
        }

        /**
         * Writes the current stopwords to the given file
         *
         * @param filename the file to write the stopwords to
         * @throws Exception if writing fails
         */
        public void write(String filename) throws Exception {
            write(new File(filename));
        }

        /**
         * Writes the current stopwords to the given file
         *
         * @param file the file to write the stopwords to
         * @throws Exception if writing fails
         */
        public void write(File file) throws Exception {
            write(new BufferedWriter(new FileWriter(file)));
        }

        /**
         * Writes the current stopwords to the given writer. The writer is closed
         * automatically.
         *
         * @param writer the writer to get the stopwords from
         * @throws Exception if writing fails
         */
        public void write(BufferedWriter writer) throws Exception {
            Enumeration   enm;

            // header
            writer.write("# generated " + new Date());
            writer.newLine();

            enm = elements();

            while (enm.hasMoreElements()) {
                writer.write(enm.nextElement().toString());
                writer.newLine();
            }

            writer.flush();
            writer.close();
        }

        /**
         * returns the current stopwords in a string
         *
         * @return the current stopwords
         */
        public String toString() {
            Enumeration   enm;
            StringBuffer  result;

            result = new StringBuffer();
            enm    = elements();
            while (enm.hasMoreElements()) {
                result.append(enm.nextElement().toString());
                if (enm.hasMoreElements())
                    result.append(",");
            }

            return result.toString();
        }

        /**
         * Returns true if the given string is a stop word.
         *
         * @param word the word to test
         * @return true if the word is a stopword
         */
        public boolean is(String word) {
            return m_Words.contains(word.toLowerCase());
        }

        /**
         * Returns true if the given string is a stop word.
         *
         * @param str the word to test
         * @return true if the word is a stopword
         */
        //m_Stopwords是stopwords类的实例化对象，因此可以调用里面的所有对象
        public static boolean isStopword(String str) {
            return m_Stopwords.is(str.toLowerCase());
        }

        //    //测试程序
//    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
//
//        String text = "Gridspot can link up idle computers instances across the world to provide large scale efforts with the computing power they require at affordable prices 0103 centsCPU hour These Linux instances run Ubuntu inside a virtual machine You are able to bid on access to these instances and specify the requirements of your tasks or jobs When your bid is fulfilled you can start running the instances using SSH anywhere youd like There are grant options available to defray costs for researchers and nonprofits The Gridspot API allows you to manage instances and identify new ones You can list available instances access them and stop the instances if you so choose Each API call requires an API key that can be generated from your account page";
//        String[] wordarr = text.split("\\s+");
//        ArrayList<String> words = new ArrayList<String>();
//        for (int i = 0; i < wordarr.length; i++) {
//            words.add(wordarr[i]);
//        }
//        //移除停用词
//        for(int i = 0; i < words.size(); i++){
//            if(Stopwords.isStopword(words.get(i))){
//                words.remove(i);
//                i--;
//            }
//        }
//        String textremoveStopword = "";
//        for (int i = 0; i < words.size(); i++) {
//            textremoveStopword += words.get(i)+" ";
//        }
//        System.out.println(textremoveStopword);
//    }
    }



}

