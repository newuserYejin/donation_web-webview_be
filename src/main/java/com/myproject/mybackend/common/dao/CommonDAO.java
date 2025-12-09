package com.myproject.mybackend.common.dao;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Component("commonDAO")
public class CommonDAO extends SqlSessionDaoSupport {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Resource(name = "sqlSessionFactory")
    public void setSuperSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    // SELECT ONE
    public <T> T selectOne(String queryId, Object params) {
        return getSqlSession().selectOne(queryId, params);
    }

    // SELECT LIST
    public <T> List<T> selectList(String queryId, Object params) {
        return getSqlSession().selectList(queryId, params);
    }

    public int update(String queryId, Object params) {
        return getSqlSession().update(queryId, params);
    }

    public int insert(String queryId, Object params) {
        return getSqlSession().insert(queryId, params);
    }

    // DELETE
    public int delete(String queryId, Object params) {
        return getSqlSession().delete(queryId, params);
    }
}
