package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("hiber")
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "HibernateImpl";
    }
}
