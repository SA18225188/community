package com.nowcoder.community.entity;

/**
 * 封装分页相关的信息
 */
public class Page {
    //当前页码
    private int current = 1;
    //显示上限
    private int limit = 10;
    //数据总数，计算总页数
    private int rows;
    //查询路径 复用链接 比如最后一页等
    private String path;




    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >=1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /**
     * 获取当前等起始行
     * @return
     */

    public int getOffset(){
        //current * limit -limit
        return (current -1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */

    public int getTotal(){
        //rows /limit

        if (rows % limit == 0){
            return rows / limit;
        }else {
            return rows / limit + 1;
        }
    }

    /**
     * 根据当前页获取起始页码和后两页
     * @return
     */

    public int getFrom(){
        int from = current - 2;
        return from < 1 ? 1 :from;
    }

    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return to > getTotal() ? total : to;
    }

}