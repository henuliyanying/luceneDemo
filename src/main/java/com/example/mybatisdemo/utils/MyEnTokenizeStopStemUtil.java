package com.example.mybatisdemo.utils;

import org.tartarus.snowball.ext.PorterStemmer;

import java.io.*;
import java.util.*;

/**
 * 对英文句子进行 分词、去除停用词、提取词干
 *  分词 用的是 非字符 进行分词，分词后每个单词开头的大写会变小写
 *  提取词干 用的是 snowball
 */
public class MyEnTokenizeStopStemUtil {

    //测试验证
    public static void main(String[] args) {
        List<String> list = enTokenizeStopStem("This is a test! Sing a song bingo,please! They made a trip to Beijing last year");
        System.out.println(""+list);
    }

    public static List<String> enTokenizeStopStem(String enText){
        ArrayList<String> lastList = new ArrayList<>();
        //分词
        List<String> list = enTokenize(enText);
        String word;
        Iterator<String> iter = list.iterator();
        while(iter.hasNext()){
            word = iter.next();
            //去除停用词：判断是不是停用词，是就删除
            if(EnStopwords.isStopword(word)){
                continue;
            }
            //词干提取，提取后为空""或null就删除
            word = enWordStem(word);
            if(word == null || "".equals(word)){
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
    public static List<String> enTokenize(String source){
        ArrayList<String> tokens = new ArrayList();
        StringBuilder buffer  =   new  StringBuilder();
        for  (int i = 0;i < source.length();i++)  {
            char character = source.charAt(i);
            if(Character.isLetter(character)){
                buffer.append(character);
            }else{
                if (buffer.length() > 0){
                    if(Character.isUpperCase(buffer.charAt(0))){
                        buffer.setCharAt(0,Character.toLowerCase(buffer.charAt(0)));
                    }
                    tokens.add(buffer.toString());
                    buffer = new StringBuilder();
                }
            }
        }
        if(buffer.length() > 0){
            if(Character.isUpperCase(buffer.charAt(0))){
                buffer.setCharAt(0,Character.toLowerCase(buffer.charAt(0)));
            }
            tokens.add(buffer.toString());
        }
        return tokens;
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

        protected HashSet m_Words = null;

        protected static EnStopwords m_Stopwords;

        static {
            if (m_Stopwords == null) {
                m_Stopwords = new EnStopwords();
            }
        }
        public EnStopwords() {
            m_Words = new HashSet();
            add("。");
            add("，");
            add("A");
            add("B");
            add("C");
            add("D");
            add("a");
            add("am");
            add("an");
            add("are");
            add("as");
            add("at");
            add("b");
            add("be");
            add("c");
            add("d");
            add("e");
            add("et");
            add("etc");
            add("f");
            add("g");
            add("h");
            add("he");
            add("i");
            add("is");
            add("it");
            add("its");
            add("j");
            add("k");
            add("l");
            add("m");
            add("me");
            add("my");
            add("n");
            add("nd");
            add("no");
            add("not");
            add("o");
            add("oh");
            add("ok");
            add("okay");
            add("p");
            add("q");
            add("s");
            add("t");
            add("the");
            add("to");
            add("us");
            add("uucp");
            add("v");
            add("ve"); //added to avoid words like I've,you've etc.
            add("via");
            add("viz");
            add("vs");
            add("w");
            add("was");
            add("welcome");
            add("x");
            add("y");
            add("yet");
            add("you");
            add("z");
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

        public void clear() {
            m_Words.clear();
        }

        public void add(String word) {
            if (word.trim().length() > 0)
                m_Words.add(word.trim().toLowerCase());
        }

        public boolean remove(String word) {
            return m_Words.remove(word);
        }

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

        public void read(String filename) throws Exception {
            read(new File(filename));
        }

        public void read(File file) throws Exception {
            read(new BufferedReader(new FileReader(file)));
        }

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

        public void write(String filename) throws Exception {
            write(new File(filename));
        }

        public void write(File file) throws Exception {
            write(new BufferedWriter(new FileWriter(file)));
        }
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

        public boolean is(String word) {
            return m_Words.contains(word.toLowerCase());
        }
        public static boolean isStopword(String str) {
            return m_Stopwords.is(str.toLowerCase());
        }
    }
}

