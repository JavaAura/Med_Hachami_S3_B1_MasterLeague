package com.service.impl;

import com.repository.impl.PlayerRepositoryImpl;
import com.service.IPlayerService;

public class PlayerServiceImpl implements IPlayerService {
    private PlayerRepositoryImpl playerRepositoryImpl;
    private PlayerServiceImpl(PlayerRepositoryImpl playerRepositoryImpl){
        this.playerRepositoryImpl = playerRepositoryImpl;
    }

    @Override
    public void doSomething() {
        System.out.println("so");
    }
    
}
