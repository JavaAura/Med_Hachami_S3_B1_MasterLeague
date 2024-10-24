package com.repository.impl;

import org.hibernate.SessionFactory;

import com.model.Team;

public class TeamRepositoryImpl  extends RepositoryImpl<Team, Long>  {

    public TeamRepositoryImpl(SessionFactory sessionFactory) {
        super(Team.class);
        this.setSessionFactory(sessionFactory); 
    }
    
}
