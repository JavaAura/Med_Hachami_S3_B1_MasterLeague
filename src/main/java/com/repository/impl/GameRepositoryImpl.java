package com.repository.impl;

import org.hibernate.SessionFactory;

import com.model.Game;

public class GameRepositoryImpl  extends RepositoryImpl<Game, Long>  {

    public GameRepositoryImpl(SessionFactory sessionFactory) {
        super(Game.class);
        this.setSessionFactory(sessionFactory); 

    }
    
}
