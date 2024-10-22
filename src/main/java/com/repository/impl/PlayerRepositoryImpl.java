package com.repository.impl;

import org.hibernate.SessionFactory;

import com.model.Player;

public class PlayerRepositoryImpl extends RepositoryImpl<Player, Long>  {

    public PlayerRepositoryImpl(SessionFactory sessionFactory) {
        super(Player.class);
        this.setSessionFactory(sessionFactory); 
    }
}
