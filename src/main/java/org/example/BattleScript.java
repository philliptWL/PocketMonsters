package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class BattleScript {
    private static final int ineffective = 0;
    private static final int normal = 1;
    private static final int supereffective = 2;

    public static void startBattle(Trainers trainer1, Trainers trainer2, Scanner scanner) {
        selectTeam(trainer1, scanner, initializeRoster());
        selectTeam(trainer2, scanner, initializeRoster());

        String strWinner = "";

        PocketMonsters activeRedPM = selectPM(trainer1, scanner);
        PocketMonsters activeBluePM = selectPM(trainer2, scanner);

        System.out.println("""
                The battle has begun!
                """);

        while (strWinner.isEmpty()) {
            while (activeBluePM.getHP() > 0 && activeRedPM.getHP() > 0) {
                if (activeRedPM.getHP() <= 0) {
                    strWinner = endBattle(trainer1, trainer2);
                    if (strWinner.isEmpty()) {
                        activeRedPM = selectPM(trainer1, scanner);
                    } else {
                        break;
                    }
                }

                takeDamage(activeBluePM, selectMove(trainer1, scanner, activeRedPM));

                if (activeBluePM.getHP() <= 0) {
                    strWinner = endBattle(trainer1, trainer2);
                    if (strWinner.isEmpty()) {
                        activeBluePM = selectPM(trainer2, scanner);
                    } else {
                        break;
                    }
                }

                takeDamage(activeRedPM, selectMove(trainer2, scanner, activeBluePM));

                if (activeRedPM.getHP() <= 0) {
                    strWinner = endBattle(trainer1, trainer2);
                    if (strWinner.isEmpty()) {
                        activeRedPM = selectPM(trainer1, scanner);
                    } else {
                        break;
                    }
                }
            }
        }
        endProgram(trainer1,trainer2,strWinner,scanner);
    }

    private static String endBattle(Trainers trainer1, Trainers trainer2){
        boolean redDefeated = trainer1.getTeam().get(0).getHP() <= 0 && trainer1.getTeam().get(1).getHP() <= 0;
        boolean blueDefeated = trainer2.getTeam().get(0).getHP() <= 0 && trainer2.getTeam().get(1).getHP() <= 0;

        if (redDefeated) {
            return trainer2.getTrainer();
        } else if (blueDefeated) {
            return trainer1.getTrainer();
        } else {
            return "";
        }
    }

    private static void takeDamage(PocketMonsters hitPM, Moves selectedMove){
        int currentHP = hitPM.getHP();
        int damagedHP;
        int appliedDamage = selectedMove.getDamage();


        if (isNonEffective(hitPM,selectedMove)){
            System.out.printf("\n%s doesn't effect %s!\n",selectedMove,hitPM);
        }else{
            if (Math.random() < selectedMove.getAccuracy()) {
                System.out.println("It's a hit!\n");
                if (isSuperEffective(hitPM, selectedMove)) {
                    damagedHP = calcDamage(hitPM, currentHP, appliedDamage,supereffective);
                } else {
                    if (hitPM.getType().equals(selectedMove.getType())) {
                        damagedHP = calcDamage(hitPM, currentHP, appliedDamage,ineffective);
                    } else {
                        damagedHP = calcDamage(hitPM, currentHP, appliedDamage,normal);
                    }
                }
                hitPM.setHP(damagedHP);
            } else {
                System.out.printf("%s missed!\n\n", selectedMove.getMoves());
            }
        }
    }

    private static int calcDamage(PocketMonsters hitPM, int currentHP, int appliedDamage, int modDamage){
        int damagedHP = hitPM.getHP();
        switch (modDamage){
            case 0:
                appliedDamage = appliedDamage / 2;
                damagedHP = currentHP - appliedDamage;
                damagedHP = Math.max(damagedHP,0);
                if (damagedHP > 0) {
                    System.out.printf("""
                                                %s has been hit for %d damage, it's not very effective!
                                                
                                                %s now has %d HP.
                                                
                                                """, hitPM.getPocketMonster(),
                            appliedDamage, hitPM.getPocketMonster(), damagedHP);
                } else {
                    System.out.printf("""
                                                %s has been hit for %d damage, it's not very effective!
                                                
                                                %s fainted!
                                                
                                                """, hitPM.getPocketMonster(),
                            appliedDamage, hitPM.getPocketMonster());
                }
                return damagedHP;
            case 1:
                damagedHP = currentHP - appliedDamage;
                damagedHP = Math.max(damagedHP,0);
                if (damagedHP > 0) {
                    System.out.printf("""
                                                %s has been hit for %d damage!
                                                
                                                %s now has %d HP.
                                                
                                                """, hitPM.getPocketMonster(),
                            appliedDamage, hitPM.getPocketMonster(), damagedHP);
                } else {
                    System.out.printf("""
                                                %s has been hit for %d damage!
                                                
                                                %s fainted!
                                                
                                                """, hitPM.getPocketMonster(),
                            appliedDamage, hitPM.getPocketMonster());
                }
                return damagedHP;
            case 2:
                appliedDamage = appliedDamage * 2;
                damagedHP = currentHP - appliedDamage;
                damagedHP = Math.max(damagedHP,0);
                if (damagedHP > 0) {
                    System.out.printf("""
                                        %s has been hit for %d damage, it's super effective!
                                        
                                        %s now has %d HP.
                                        
                                        """, hitPM.getPocketMonster(),
                            (currentHP - damagedHP), hitPM.getPocketMonster(), damagedHP);
                } else {
                    System.out.printf("""
                                        %s has been hit for %d damage, it's super effective!
                                        
                                        %s fainted!.
                                        
                                        """, hitPM.getPocketMonster(),
                            appliedDamage, hitPM.getPocketMonster());
                }
                return damagedHP;
            default:
                return damagedHP;
        }
    }

    private static boolean isSuperEffective(PocketMonsters hitPM, Moves selectedMove){
        boolean isSuperEffective = false;
        String compareType = hitPM.getType();

        switch (selectedMove.getType()){
            case "Electric":
                isSuperEffective = compareType.equals("Water") || compareType.equals("Flying");
                break;
            case "Fire":
                isSuperEffective = compareType.equals("Grass") || compareType.equals("Bug");
                break;
            case "Flying":
                isSuperEffective = compareType.equals("Bug") || compareType.equals("Grass");
                break;
            case "Ghost", "Dark":
                isSuperEffective = compareType.equals("Ghost") || compareType.equals("Psychic");
                break;
            case "Psychic":
                isSuperEffective = compareType.equals("Fighting") || compareType.equals("Poison");
                break;
            case "Bug":
                isSuperEffective = compareType.equals("Psychic") || compareType.equals("Dark")
                        || compareType.equals("Grass");
                break;
            default:
                break;
        }
        return isSuperEffective;
    }

    private static boolean isNonEffective(PocketMonsters hitPM, Moves selectedMove){
        boolean isNonEffective = false;
        String compareType = hitPM.getType();

        switch (selectedMove.getType()) {
            case "Electric":
                isNonEffective = compareType.equals("Ground");
                break;
            case "Ghost":
                isNonEffective =  compareType.equals("Normal");
                break;
            case "Psychic":
                isNonEffective = compareType.equals("Dark");
                break;
            case "Normal":
                isNonEffective =  compareType.equals("Ghost");
                break;
            default:
                break;
        }
        return isNonEffective;
    }

    private static Moves selectMove(Trainers trainer, Scanner scanner, PocketMonsters activePM){
        int menuInput;
        Moves selectedMove = null;
        int intPM = trainer.getTeam().indexOf(activePM);

        while (selectedMove == null){
            System.out.printf("""
                %s, select a move for %s:
                
                [1] %s | Type: %s | Damage: %d | Accuracy: %d%%
                [2] %s | Type: %s | Damage: %d | Accuracy: %d%%
                
                """,trainer.getTrainer(),trainer.getTeam().get(intPM).getPocketMonster(),
                    trainer.getTeam().get(intPM).getMoveset().get(0).getMoves(),
                    trainer.getTeam().get(intPM).getMoveset().get(0).getType(),
                    trainer.getTeam().get(intPM).getMoveset().get(0).getDamage(),
                    (int)(trainer.getTeam().get(intPM).getMoveset().get(0).getAccuracy() * 100),
                    trainer.getTeam().get(intPM).getMoveset().get(1).getMoves(),
                    trainer.getTeam().get(intPM).getMoveset().get(1).getType(),
                    trainer.getTeam().get(intPM).getMoveset().get(1).getDamage(),
                    (int)(trainer.getTeam().get(intPM).getMoveset().get(1).getAccuracy() * 100));

            menuInput = scanner.nextInt();

            switch (menuInput){
                case 1:
                    selectedMove = trainer.getTeam().get(intPM).getMoveset().get(0);
                    break;
                case 2:
                    selectedMove = trainer.getTeam().get(intPM).getMoveset().get(1);
                    break;
                default:
                    System.out.println("\nPlease select a move.\n");
            }
        }
        System.out.printf("\n%s used %s!\n\n",activePM.getPocketMonster(),selectedMove.getMoves());
        return selectedMove;
    }

    private static PocketMonsters selectPM(Trainers trainer, Scanner scanner) {
        int menuInput;
        PocketMonsters activePM = null;

        while (activePM == null) {
            System.out.printf("""
                %s, select a Pocket Monster.
                
                [1] %s | HP: %d | Moves: %s (%d DMG) , %s (%d DMG) | Type: %s
                [2] %s | HP: %d | Moves: %s (%d DMG) , %s (%d DMG) | Type: %s
                """, trainer.getTrainer(),trainer.getTeam().get(0).getPocketMonster(), trainer.getTeam().get(0).getHP(),
                    trainer.getTeam().get(0).getMoveset().get(0).getMoves(),
                    trainer.getTeam().get(0).getMoveset().get(0).getDamage(),
                    trainer.getTeam().get(0).getMoveset().get(1).getMoves(),
                    trainer.getTeam().get(0).getMoveset().get(1).getDamage(),
                    trainer.getTeam().get(0).getType(),
                    trainer.getTeam().get(1).getPocketMonster(), trainer.getTeam().get(1).getHP(),
                    trainer.getTeam().get(1).getMoveset().get(0).getMoves(),
                    trainer.getTeam().get(1).getMoveset().get(0).getDamage(),
                    trainer.getTeam().get(1).getMoveset().get(1).getMoves(),
                    trainer.getTeam().get(1).getMoveset().get(1).getDamage(),
                    trainer.getTeam().get(1).getType());

            menuInput = scanner.nextInt();

            switch (menuInput) {
                case 1:
                    if(trainer.getTeam().getFirst().getHP() > 0) {
                        activePM = trainer.getTeam().getFirst();
                    }else{
                        System.out.printf("\n%s is out of HP! Select a different Pocket Monster.\n\n",
                                trainer.getTeam().getFirst().getPocketMonster());
                    }
                    break;
                case 2:
                    if (trainer.getTeam().get(1).getHP()>0) {
                        activePM = trainer.getTeam().get(1);
                    }else{
                        System.out.printf("\n%s is out of HP! Select a different Pocket Monster.\n",
                                trainer.getTeam().get(1).getPocketMonster());
                    }
                    break;
                default:
                    System.out.println("\nPlease choose a Pocket Monster to use first for battle.\n");
                    break;
            }
        }
        System.out.printf("\n%s I choose you!\n\n",activePM.getPocketMonster());
        return activePM;
    }

    private static void selectTeam(Trainers trainer,Scanner scanner, ArrayList<PocketMonsters> Roster) {
        ArrayList<PocketMonsters> Team = new ArrayList<>();

        int menuInput;
        String strCount;

        for (int i = 0; i <= 1; i++) {
            if (i == 0) {
                strCount = "first";
            } else {
                strCount = "second";
            }
            while (true) {
                System.out.printf("""
                    
                    %s, select your %s of two Pocket Monsters:
                    
                    """, trainer.getTrainer(), strCount);

                for (int i1 = 0; i1 < Roster.size(); i1++) {
                    System.out.printf("[%d] %s | HP: %d | Moves: %s, %s, | Type: %s | Description: %s | Level: %d\n",i1,
                            Roster.get(i1).getPocketMonster(),Roster.get(i1).getHP(),
                            Roster.get(i1).getMoveset().get(0).getMoves(),Roster.get(i1).getMoveset().get(1).getMoves(),
                            Roster.get(i1).getType(),Roster.get(i1).getDescription(),Roster.get(i1).getLevel());
                }

                if (scanner.hasNextInt()) {
                    menuInput = scanner.nextInt();
                    if (menuInput >= 0 && menuInput < Roster.size()) {
                        PocketMonsters selected = Roster.get(menuInput);
                        Team.add(selected);
                        Roster.remove(menuInput);
                        break;
                    } else {
                        System.out.println("\nInvalid selection. Please enter a number from 0 to " + (Roster.size() - 1)
                                + ".\n");
                    }
                } else {
                    System.out.println("\nInvalid input. Please enter a number.\n");
                    scanner.next();
                }
            }
        }

        trainer.setTeam(Team);

        menuInput = -1;

        while(menuInput!=0){
            System.out.printf("""
                Would you like to give your pocket monster's a nickname?
                
                [1] %s
                [2] %s
                
                [0] No
                """,trainer.getTeam().get(0).getPocketMonster(),trainer.getTeam().get(1).getPocketMonster());
            menuInput = scanner.nextInt();
            scanner.nextLine();

            String strNickName;
            switch (menuInput){
                case 1:
                    System.out.printf("""
                            Please enter a nickname for %s or press ENTER to cancel.
                            """,trainer.getTeam().get(0).getPocketMonster());
                    strNickName = scanner.nextLine();
                    if(!strNickName.isBlank()){
                        System.out.printf("""
                                %s's nickname has been set to %s!
                                """,trainer.getTeam().get(0).getPocketMonster(),strNickName);
                        trainer.getTeam().get(0).setPocketMonster(strNickName);
                    }
                    break;
                case 2:
                    System.out.printf("""
                            Please enter a nickname for %s or press ENTER to cancel.
                            """,trainer.getTeam().get(1).getPocketMonster());
                    strNickName = scanner.nextLine();
                    if(!strNickName.isBlank()){
                        System.out.printf("""
                                %s's nickname has been set to %s!
                                """,trainer.getTeam().get(1).getPocketMonster(),strNickName);
                        trainer.getTeam().get(1).setPocketMonster(strNickName);
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\nPlease choose a menu option.\n");
            }
        }
        heldItem(trainer,scanner);
    }

    private static void heldItem(Trainers trainer, Scanner scanner) {
        int menuInput;
        String strVitalityOrb = "Vitality Orb +15HP";
        String strFuryBelt = "Fury Belt +10DMG to 1 move";
        PocketMonsters selectedPM;
        boolean appliedPowerUp = false;
        boolean optionChoose;

        while (!appliedPowerUp) {
            System.out.printf("""
            Which of your pocket monsters would you like to hold a power-up item?

            [1] %s
            [2] %s

            """, trainer.getTeam().get(0).getPocketMonster(), trainer.getTeam().get(1).getPocketMonster());
            menuInput = scanner.nextInt();

            if (menuInput == 1) {
                selectedPM = trainer.getTeam().get(0);
                optionChoose = false;
            } else if (menuInput == 2) {
                selectedPM = trainer.getTeam().get(1);
                optionChoose = false;
            } else {
                System.out.println("\nInvalid selection.\n");
                continue;
            }

            while (!optionChoose) {
                System.out.printf("""
                Which power-up would you like to give to %s?

                [1] %s
                [2] %s

                [0] Cancel
                """, selectedPM.getPocketMonster(), strVitalityOrb, strFuryBelt);
                menuInput = scanner.nextInt();

                switch (menuInput) {
                    case 1:
                        selectedPM.setHP(selectedPM.getHP() + 15);
                        System.out.printf("\n%s's HP was increased to %d!\n",
                                selectedPM.getPocketMonster(), selectedPM.getHP());
                        appliedPowerUp = true;
                        optionChoose = true;
                        break;
                    case 2:
                        while (true) {
                            System.out.printf("""
                            Which move would you like to empower for %s?

                            [1] %s | DMG: %d | Type: %s
                            [2] %s | DMG: %d | Type: %s

                            [0] Cancel
                            """, selectedPM.getPocketMonster(),
                                    selectedPM.getMoveset().get(0).getMoves(),
                                    selectedPM.getMoveset().get(0).getDamage(),
                                    selectedPM.getMoveset().get(0).getType(),
                                    selectedPM.getMoveset().get(1).getMoves(),
                                    selectedPM.getMoveset().get(1).getDamage(),
                                    selectedPM.getMoveset().get(1).getType());
                            menuInput = scanner.nextInt();
                            switch (menuInput) {
                                case 1:
                                    selectedPM.getMoveset().get(0).setDamage(
                                            selectedPM.getMoveset().get(0).getDamage() + 10);
                                    System.out.printf("\n%s's %s damage was increased to %d!\n\n",
                                            selectedPM.getPocketMonster(),
                                            selectedPM.getMoveset().get(0).getMoves(),
                                            selectedPM.getMoveset().get(0).getDamage());
                                    appliedPowerUp = true;
                                    break;
                                case 2:
                                    selectedPM.getMoveset().get(1).setDamage(
                                            selectedPM.getMoveset().get(1).getDamage() + 10);
                                    System.out.printf("\n%s's %s damage was increased to %d!\n\n",
                                            selectedPM.getPocketMonster(),
                                            selectedPM.getMoveset().get(1).getMoves(),
                                            selectedPM.getMoveset().get(1).getDamage());
                                    appliedPowerUp = true;
                                    break;
                                case 0:
                                    break;
                                default:
                                    System.out.println("\nInvalid selection.\n");
                                    continue;
                            }
                            break;
                        }
                        optionChoose = true;
                        break;
                    case 0:
                        optionChoose = true;
                        break;
                    default:
                        System.out.println("\nInvalid input.\n");
                }
            }
        }
    }

    private static ArrayList<PocketMonsters> initializeRoster(){
        ArrayList<PocketMonsters> Roster = new ArrayList<>();

        Roster.add(createMonster("Chikapu",95,"Electric","Thunderbolt",21,
                0.90, "Electric","Tackle",12,1.0,
                "Normal", "Small and agile electric-mouse PocketMonster",12));
        Roster.add(createMonster("MewThree",100,"Psychic","Shadow Ball",22,
                1.0, "Ghost","Psychic",18,0.90,
                "Psychic", "Ultimate lifeform lab-created psychic PocketMonster",
                40));
        Roster.add(createMonster("Lizchar",110,"Fire","Fire Blast",22,
                0.85, "Fire","Fly",14,0.95,
                "Flying","Winged fire-breathing dragon PocketMonster", 35));
        Roster.add(createMonster("Monsieur Mime",86,"Psychic","Psybeam",20,
                1.0, "Psychic","Sucker Punch",12,1.0,
                "Dark","Psychic humanoid mime PocketMonster",22));
        Roster.add(createMonster("Scytickle",90,"Bug","Pin Missile",18,
                0.85, "Bug","Skitter Smack",14,0.90,
                "Bug","Dual-arm blade bug PocketMonster",15));
        Roster.add(createMonster("Phandread",88,"Ghost","Night Shade",19,
                1.0, "Ghost","Lick",17,0.95,
                "Ghost","Eerie translucent ghost PocketMonster",30));

        return Roster;
    }

    private static PocketMonsters createMonster(String name, int hp, String type, String move1Name, int move1Damage,
                                                double move1Accuracy, String move1Type,
                                                String move2Name, int move2Damage, double move2Accuracy,
                                                String move2Type,String monsterDescription, int monsterLevel) {

        ArrayList<Moves> moves = new ArrayList<>();

        Moves m1 = new Moves(move1Name,move1Damage,move1Accuracy,move1Type);

        Moves m2 = new Moves(move2Name,move2Damage,move2Accuracy,move2Type);

        moves.add(m1);
        moves.add(m2);

        return new PocketMonsters(name,hp,type,moves,monsterDescription,monsterLevel);
    }

    private static void endProgram(Trainers trainer1, Trainers trainer2, String strWinner,Scanner scanner){
        int menuInput;
        while (true) {
            System.out.printf("""
                    %s is victorious!
                    
                    Enter 0 to end program OR 1 to battle again!
                    """, strWinner);
            menuInput = scanner.nextInt();
            if (menuInput == 1) {
                startBattle(trainer1, trainer2, scanner);
                return;
            } else if (menuInput == 0) {
                System.out.println("\nThanks for playing Pocket Monsters!");
                return;
            } else {
                System.out.println("\nInvalid input. Please enter 0 or 1.");
            }
        }
    }
}