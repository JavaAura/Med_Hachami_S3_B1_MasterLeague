package com;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.model.Game;
import com.model.Player;
import com.service.IGameService;
import com.service.IPlayerService;

public class Application {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        IPlayerService playerService = (IPlayerService) context.getBean("playerService");

        IGameService gameService = (IGameService) context.getBean("gameService");

        Game game = new Game();
        game.setName("UCF");
        // game.setDifficulty(2);

        boolean isGameAdded = gameService.addGame(game);
        System.out.println("Game added: " + isGameAdded);

       

        // Step 7: Fetch all games
        System.out.println("All Games:");
        gameService.getAllGames().forEach(g -> System.out.println(g.getName()));

       


        // Player player = new Player();
        // player.setName("Ali");
        // player.setAge(19);

        // boolean result = playerService.addPlayer(player);

        // if (result) {
        //     System.out.println("Player added successfully.");
        // } else {
        //     System.out.println("Failed to add player.");
        // }

        // try {
        //     displayAllPlayers(playerService);
        // } catch (Exception e) {
        //     e.printStackTrace(); 
        // }
    }

    private static void displayAllPlayers(IPlayerService playerService) throws Exception {
        List<Player> players = playerService.getAllPlayers();

        System.out.println("List of players:");
        for (Player player : players) {
            System.out.println("ID: " + player.getId() + ", Name: " + player.getName() + ", Age: " + player.getAge());
        }
    }
}
