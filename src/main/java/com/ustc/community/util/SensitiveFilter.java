package com.ustc.community.util;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //常量替换符号
    private static final String REPLACEMENT  = "***";

    //初始化根节点
    private TrieNode rootNode = new TrieNode();

    //这个方法在bean实例化之后，调用构造器之后，这个方法就会被调用
    @PostConstruct
    public void init(){
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //字节转化成字符流

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                ){
            String keyword;
            while ((keyword = reader.readLine()) != null){
//                添加前缀树
                this.addKeyword(keyword);
            }

        }catch (IOException e){
            logger.error("加载敏感词文件失败" + e.getMessage());
        }

    }



    //将一个敏感词添加到前缀树
        private void addKeyword(String keyword){
            TrieNode tempNode = rootNode;
            for (int i = 0; i < keyword.length(); i++) {
                char c = keyword.charAt(i);
                TrieNode subNode = tempNode.getSubNode(c);

                if (subNode == null){
                    subNode = new TrieNode();
                    tempNode.addSubNode(c, subNode);
                }

                //指向子节点 下一次循环
                tempNode = subNode;

                //设置结束标志符号
                if (i == keyword.length() - 1){
                    tempNode.setKeywordEnd(true);
                }
            }
        }


    /**
     *
     * @param text 待过滤文本
     * @return     过滤后到文本
     */
        public String filter(String text){
            if (StringUtils.isBlank(text)){
                return null;
            }

            //指针1
            TrieNode tempNode = rootNode;
            //指针2
            int begin = 0;
            //指针3
            int position = 0;
            //结果
            StringBuilder stringBuilder = new StringBuilder();

            while (position < text.length()){
                char c = text.charAt(position);

                //跳过符号
                if (idSymbol(c)){
                    //如果指针1处于根节点，将此符号计入结果，指针2走一步
                    if (tempNode == rootNode){
                        stringBuilder.append(c);
                        begin++;
                    }
                    //无论符号在开头或者中间，指针3向下走一步
                    position++;
                    continue;
                }

                //检查下级节点
                tempNode = tempNode.getSubNode(c);
                if (tempNode == null){
                    //以begin开头的不是敏感词
                    stringBuilder.append(text.charAt(begin));
                    //进入下一个位置
                    position = ++begin;
                    //重新指向根节点
                    tempNode = rootNode;
                }else if (tempNode.isKeywordEnd()){
                    stringBuilder.append(REPLACEMENT);
                    //进入下一位置
                    begin = ++position;
                    //重新指向根 节点
                    tempNode = rootNode;
                }else {
//                    检查下一个字符
                    position++;
                }
            }
            //最后一批字符 记录结果
            stringBuilder.append(text.substring(begin));
            return stringBuilder.toString();
        }


        //判断是否为符号
    private boolean idSymbol(Character c){
           //0x2e80 到 0x9fff 为东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2e80 || c > 0x9fff);
    }


    //前缀树
    private class TrieNode{


        //关键词结束标志符号
        private boolean isKeywordEnd =false;

        //描述子节点 可能多个节点 子节点和字符绑定起来 key是下级字符， value是下级节点
        private Map<Character, TrieNode>  subNodes = new HashMap<>();


        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

//        添加子节点
        public void addSubNode(Character c, TrieNode trieNode){
            subNodes.put(c, trieNode);
        }

    //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }


}
