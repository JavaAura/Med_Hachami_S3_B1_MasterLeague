package com.repository.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exception.DataAccessException;
import com.repository.Repository;

public class RepositoryImpl<T, ID extends Serializable> implements Repository<T, ID> {

    private final Logger logger = LoggerFactory.getLogger(RepositoryImpl.class);
    private SessionFactory sessionFactory;
    private Class<T> entityClass;

    public RepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<T> findById(ID id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            T entity = session.get(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (HibernateException e) {
            logger.error("Error finding entity by id: {}", id, e);
            throw new DataAccessException("Error retrieving entity with id: " + id, e);
        }
    }

    
    @Override
    public List<T> findAll() {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM " + entityClass.getName(), entityClass).list();
        } catch (HibernateException e) {
            logger.error("Error finding all entities", e);
            throw new DataAccessException("Error retrieving all entities", e);
        }
    }

    @Override
    public T save(T entity) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(entity);
            return entity;
        } catch (HibernateException e) {
            logger.error("Error saving entity", e);
            throw new DataAccessException("Error saving entity", e);
        }
    }

    @Override
    public void update(T entity) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.update(entity);
        } catch (HibernateException e) {
            logger.error("Error updating entity", e);
            throw new DataAccessException("Error updating entity", e);
        }
    }

    @Override
    public void delete(T entity) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.delete(entity);
        } catch (HibernateException e) {
            logger.error("Error deleting entity", e);
            throw new DataAccessException("Error deleting entity", e);
        }
    }
}
