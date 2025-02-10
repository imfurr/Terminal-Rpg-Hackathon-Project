import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class GameManager{
    public static boolean playingGame = true;
    
    //cd "c:\Users\johdo\OneDrive\Desktop\SAMMYJC\project\" ; if ($?) { javac GameManager.java } ; if ($?) { java GameManager }

    //game state stuff
    public static enum GameState{
        walking,
        fighting,
        dead
    }
    public static GameState currState = GameState.walking;

    //player stats
    public static String name;
    public static Vector2 playerPos = new Vector2(1, 1);
    public static double playerHealth;
    public static double playerMaxHealth;
    public static double playerAttack;
    public static double playerDefense;
    public static float difficultyMult;

    public static List<String> outtaBoundsStrings = new ArrayList<>();

    //settings
    public static boolean useNESW = true;

    public static void main(String[] args) throws IOException
    {
        System.out.println("-----------------------------------------------------------------------------");
        playerHealth = StringToFloat(GetLinesInFile("health.txt").get(0));
        playerMaxHealth = StringToFloat(GetLinesInFile("health.txt").get(0));
        playerAttack = StringToFloat(GetLinesInFile("attack.txt").get(0));
        playerDefense = StringToFloat(GetLinesInFile("defense.txt").get(0));
        difficultyMult = StringToFloat(GetLinesInFile("difficulty.txt").get(0));
        outtaBoundsStrings = GetLinesInFile("OutOfBoundsLines.txt");

        System.out.println("Health: " + playerHealth);
        System.out.println("Attack Damage: " + playerAttack);
        System.out.println("Defense: " + playerDefense);
        System.out.println("Difficulty Multiplier : " + difficultyMult);

        useNESW = GetLinesInFile("controls.txt").get(0).equals("NESW");
        
        System.out.println("HI THANKS FOR OUR PLAYING GAME!!");
        System.out.println("-----------------------------------------------------------------------------");
        System.out.print("\033[1m" + "Enter a username: " + "\033[22m");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        name = input;
        System.out.println("Welcome, " + name);

        levelMaps.add(level1Map);
        levelMaps.add(level2Map);
        levelMaps.add(level3Map);
        levelMaps.add(level4Map);

        walkMap.clear();
        for (int i = 0; i < level1Map.size(); i++) {
            walkMap.add(level1Map.get(i));
        }
        levelXMax = level1Map.get(0).length() - 1;
        levelYMax = level1Map.size() - 1;

        System.out.println("-----------------------------------------------------------------------------");
        while(playingGame)
        {
            for (int i = 0; i < walkMap.size(); i++)
            {
                if(i == 0)
                {
                    for (int j = 0; j < walkMap.get(0).length() + 2; j++) {
                        System.out.print("-");
                    }
                    System.out.println("");
                }
                for (int j = 0; j < walkMap.get(i).length(); j++)
                {
                    if(j == 0) System.out.print("|");
                    String tempCurrLetter = Character.toString(walkMap.get(i).charAt(j));
                    if(playerPos.x == j && playerOnArrayPos(walkMap).y == i)
                    {
                        System.out.print("p");
                    } else if(tempCurrLetter.equals("e") || tempCurrLetter.equals("r") || tempCurrLetter.equals("q") || tempCurrLetter.equals("w"))
                    {
                        System.out.print("\u001B[31m" + tempCurrLetter + "\033[0m");
                    } else if(tempCurrLetter.equals("t"))
                    {
                        System.out.print("\033[33m" + "\033[1m" + "t" + "\033[0m" + "\033[22m");
                    } else {
                        System.out.print(Character.toString(walkMap.get(i).charAt(j)));
                    }
                    if(j == walkMap.get(i).length() - 1) System.out.println("|");
                }
                if(i == walkMap.size() - 1)
                {
                    for (int j = 0; j < walkMap.get(0).length() + 2; j++) {
                        System.out.print("-");
                    }
                    System.out.println("");
                }
            }

            System.out.println("-----------------------------------------------------------------------------");

            switch (currState) { //prompt stuff
                case GameState.walking:
                    System.out.print("\033[1m" + "What direction do you wanna go? " + "\033[22m");
                    if(useNESW)
                    {
                        System.out.println("\033[1m" + "(n/e/s/w)" + "\033[22m");
                    } else System.out.println("\033[1m" + "(a/s/w/d)" + "\033[22m");
                    break;
                case GameState.fighting:
                    System.out.println("\033[1m" + "YOU FOUND AN ENEMY!!!! What do you wanna do? (fight/run/instantaneously explode)" + "\033[22m");
                    break;
                case GameState.dead:
                    System.out.println("\033[1m" + "You suck at video games... play again? (" + "\033[32m" + "y" + "\033[37m"+ "/" + "\033[31m" + "n" + "\033[37m" + ")" + "\033[22m");
                    break;
                default:
                    break;
            }
            
            input = scanner.nextLine();
            switch (currState) { //do stuff with prompt
                case walking:
                    WalkLogic(input);
                    CheckLandmarks(input);
                    break;
                case fighting:
                    if(input.equals("instantaneously explode"))
                    {
                        System.out.println("W- why would you do that, you just died...");
                        Die();
                    } else if(input.equals("fight"))
                    {
                        System.out.println("you fought");
                    } else if(input.equals("run"))
                    {
                        System.out.println("they caught up and you died");
                        Die();
                    }  else NotAnOption();
                    /*else if(input.equals("hide"))
                    {
                        System.out.println("they found you and you died");
                        Die();
                    }*/
                    break;
                case dead:
                    if(input.equals("y"))
                    {
                        System.out.println("RESTART TIME");
                        SetupGame();
                    } else if(input.equals("n"))
                    {
                        System.out.println("ok bye");
                        playingGame = false;
                    } else NotAnOption();
                    break;
                default:
                    break;
            }
        }
        //scanner.close();
    }
    public static void SetupGame()
    {
        walkMap.clear();
        for (int i = 0; i < level1Map.size(); i++) {
            walkMap.add(level1Map.get(i));
        }
        playerPos = new Vector2(1, 1);
        levelIndex = 0;
        currState = GameState.walking;
        playerHealth = playerMaxHealth; 
    }
    public static void NotAnOption()
    {
        System.err.println("That's not an option ._.");
    }
    public static void Die()
    {
        currState = GameState.dead;
    }

    public static void WalkLogic(String dir)
    {
        Vector2 walkDir = new Vector2(0, 0);
        if(useNESW)
        {
            if (dir.equals("n")) 
            {
                walkDir = new Vector2(0, 1);
            } else if (dir.equals("e"))
            {
                walkDir = new Vector2(1,0);
            } else if (dir.equals("s")) {
                walkDir = new Vector2(0,-1);
            } else if (dir.equals ("w")) {
                walkDir = new Vector2 (-1, 0);
            } else NotAnOption();
        } else {
            if (dir.equals("w")) 
            {
                walkDir = new Vector2(0, 1);
            } else if (dir.equals("d"))
            {
                walkDir = new Vector2(1,0);
            } else if (dir.equals("s")) {
                walkDir = new Vector2(0,-1);
            } else if (dir.equals ("a")) {
                walkDir = new Vector2 (-1, 0);
            }
        }
       
        //System.out.println("Before: " + playerPos.x + ", " + playerPos.y);
        playerPos.Add(walkDir);
        if(playerPos.x > levelXMax|| playerPos.y > levelYMax || playerPos.x < 0 || playerPos.y < 0 || Character.toString(walkMap.get(playerOnArrayPos(walkMap).y).charAt(playerPos.x)).equals("="))
        {
            System.out.println(outtaBoundsStrings.get((int)((outtaBoundsStrings.size() - 1) * Math.random())));
            playerPos.Sub(walkDir);
        }
        //System.err.println("After: " + playerPos.x + ", " + playerPos.y);
    }
    public static void BackLogic(String dir)
    {
        Vector2 walkDir = new Vector2(0, 0);
        if(useNESW)
        {
            if (dir.equals("s")) 
            {
                walkDir = new Vector2(0, 1);
            } else if (dir.equals("w"))
            {
                walkDir = new Vector2(1,0);
            } else if (dir.equals("n")) {
                walkDir = new Vector2(0,-1);
            } else if (dir.equals ("e")) {
                walkDir = new Vector2 (-1, 0);
            } else NotAnOption();
        } else {
            if (dir.equals("s")) 
            {
                walkDir = new Vector2(0, 1);
            } else if (dir.equals("a"))
            {
                walkDir = new Vector2(1,0);
            } else if (dir.equals("w")) {
                walkDir = new Vector2(0,-1);
            } else if (dir.equals ("d")) {
                walkDir = new Vector2 (-1, 0);
            }
        }
        //System.out.println("Before: " + playerPos.x + ", " + playerPos.y);
        playerPos.Add(walkDir);
    }

    public static enum TileType{
        dirt,
        treasure,
        enemy
    }
    public static TileType currTile = TileType.dirt;
    public static List<String> walkMap = new ArrayList<>();
    public static List<String> level1Map = Arrays.asList(" e= ","   e","    ","   t");
    public static List<String> level2Map = Arrays.asList("         ","  e      ","      e  ", "      t  ","   e     ","         ");
    public static List<String> level3Map = Arrays.asList("te    = rq  ", "e  =  =t=== ", "   =wq= =   ",  "t  =    =e  ");
    public static List<String> level4Map = Arrays.asList("                      =tt","w  ==                 ===","   =e t       =====      ","   =t=   =    =tq        ","   === ===    =====      ","r    = =          =      ","     = ===        =      ","   r     =   t           ","         =               ","    t    =             t ");
    public static List<List<String>> levelMaps = new ArrayList<List<String>>();
    public static int levelXMax;
    public static int levelYMax;
    public static int levelIndex = 0;

    public static void CheckLandmarks(String prevWalkDir)
    {
        Vector2 tempPlayerOnArrayPos = playerOnArrayPos(walkMap);
        String currWalkmapLetter = Character.toString((walkMap).get(tempPlayerOnArrayPos.y).charAt(tempPlayerOnArrayPos.x));
        if(currWalkmapLetter.equals(" "))
        {
            currTile = TileType.dirt;
        } else if(currWalkmapLetter.equals("e") || currWalkmapLetter.equals("r") || currWalkmapLetter.equals("w") || currWalkmapLetter.equals("q") )
        {
            currTile = TileType.enemy;
            currState = GameState.fighting;
            System.out.println("\033[1m" + "FIGHT TIMEEEEEEEEE" + "\033[22m");
            if(currWalkmapLetter.equals("e"))
            {
                FightingEnemy(newEnemy(levelIndex + 1, 75, 15, 3), prevWalkDir);
            } else if(currWalkmapLetter.equals("r"))
            {
                FightingEnemy(newEnemy(levelIndex + 1, 70, 20, 5), prevWalkDir);
            } else if (currWalkmapLetter.equals("w"))
            {
                FightingEnemy(newEnemy(levelIndex + 1, 20, 10, 45), prevWalkDir);
            }else if (currWalkmapLetter.equals("q"))
            {
                FightingEnemy(newEnemy(levelIndex + 1, 20, 30, 30), prevWalkDir);
            } 
        } else if(currWalkmapLetter.equals("t"))
        {
            currTile = TileType.treasure;
            ClearTile(playerOnArrayPos(walkMap));
            playerHealth = playerMaxHealth;
            System.out.println("You found a " + "\033[33m" + "\033[1m" + "treasure" + "\033[0m" + "\033[22m" +  " and replenished all your health!");
        } else System.err.println("\033[31m" + "\033[1m" + "\033[4m" + "SOMETHING WENT TERRIBLY WRONG AND YOU SUCK AT CODING" + "\033[37m" + "\033[22m" + "\033[24m");
        //System.out.println(currTile);
    }
    public static Vector2 playerOnArrayPos(List<String> inputArray)
    {
        Vector2 outputVector = new Vector2(playerPos.x, playerPos.y);
        //if length 3 but player ant 0, should return 2
        outputVector.y = inputArray.size() - 1 - playerPos.y;
        return outputVector;
    }

    public static Enemy newEnemy(double currDifficulty, double baseHealth, double baseDmg, double addedStats)
    {
        double tempDifficultyMult = difficultyMult * ( 1 + (0.2 * currDifficulty));

        //System.out.println("DEBUGGGGG: " + "currDifficulty is " + currDifficulty + ", difficultyMult: " + difficultyMult + "tempDifficultyMult: " + tempDifficultyMult);

        double totalStatAdded = addedStats * Math.random();
        double whatPercentIsHealth = Math.random(); //multipler

        double addedHealth = totalStatAdded * whatPercentIsHealth;
        double addedDmg = totalStatAdded - addedHealth;

        Enemy outputEnemy = new Enemy(baseHealth * tempDifficultyMult + addedHealth, baseDmg * tempDifficultyMult + addedDmg);
        //logic
        return outputEnemy;
    }

   public static void FightingEnemy(Enemy currEnemy, String prevWalkDir)
   {
        Scanner scanner = new Scanner(System.in);
        boolean enemyAlive = true;
        boolean playerAlive = true;
        boolean triedRunning = false;
        System.out.println("You encountered an enemy!");
        System.out.println("Their health is: " + Math.round(currEnemy.health));
        System.out.println("Your health is: " + Math.round(playerHealth));

        while(enemyAlive && playerAlive && !triedRunning)
        {
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("\033[1m" + "What do you wanna do? (fight/run/instantaneously explode)" + "\033[22m");
            String input = scanner.nextLine();
            switch (input) {
                case "fight":
                    if ((currEnemy.damage - playerDefense) >= 0) {
                        playerHealth -= (currEnemy.damage - playerDefense);
                        System.out.println("The enemy dealt " + "\u001B[31m" + Math.round(currEnemy.damage - playerDefense) + " " + "\033[0m" + "damage to you!");
                    } else System.out.println("The enemy dealt " + "\u001B[31m" + "0" + "\033[0m" + " damage to you!");
                    currEnemy.Oww(playerAttack); 
                    if (playerHealth < 0.5) 
                    {
                        System.out.println("You died :(");
                        playerAlive = false;
                        Die();
                    } else if (currEnemy.health < 0.5)
                    {
                        System.out.println("You got them and they died");
                        System.out.println("Your health is " + Math.round(playerHealth));
                        enemyAlive = false;
                        ClearTile(playerOnArrayPos(walkMap));
                        currState = GameState.walking;
                        if (EnemiesCleared()) 
                        {
                            System.out.println("\033[1m" + "\033[4m" + "Level Cleared!" + "\033[22m" + "\033[24m");
                            NextLevel();
                        }
                    } else if (playerHealth >= 0.5 && currEnemy.health >= 0.5)
                    {
                        System.out.println("Their health is now " + Math.round(currEnemy.health));
                        System.out.println("Your health is now " + Math.round(playerHealth));
                    } else 
                    {
                        System.err.println("sammy did something wrong");
                    }
                    break;
                case "run":
                    if ((currEnemy.damage - playerDefense) >= 0)
                    {
                        playerHealth -= (currEnemy.damage - playerDefense);
                        System.out.println("The enemy dealt " + "\u001B[31m" + Math.round(currEnemy.damage - playerDefense) + "\033[0m" + " damage to you but you ran away!");
                    } else System.out.println("The enemy dealt " + "\u001B[31m" + "0" + "\033[0m" + " damage to you but you ran away!");
                    System.out.println("You also gave the enemy time to heal!");

                    if (playerHealth <= 0) playerAlive = false;
                    currState = GameState.walking;
                    triedRunning = true;

                    if(triedRunning && !playerAlive)
                    {
                        System.out.println("You died while running away!");
                        Die();
                        
                    } else if(triedRunning && playerAlive)
                    {
                        BackLogic(prevWalkDir);
                    }
                    break;
                case "hide":
                    
                    break;
                case "instantaneously explode":
                    Die();
                    playerAlive = false;
                    break;
            
                default:
                    break;
            }
        }
        //scanner.close();
   }
   public static void NextLevel()
   {
        walkMap.clear();
        levelIndex++;
        if (levelIndex >= 4) 
        {
            System.out.println("YOU WIN");
            System.out.println("Run end.py to feel awesome!");
            try {
                FileWriter myWriter = new FileWriter("health.txt");
                myWriter.write(String.valueOf(playerHealth));
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            playingGame = false;
        } else 
        {
            for (int index = 0; index < levelMaps.get(levelIndex).size(); index++) {
            walkMap.add(levelMaps.get(levelIndex).get(index));
        }
        levelXMax = levelMaps.get(levelIndex).get(0).length() - 1;
        levelYMax = levelMaps.get(levelIndex).size() - 1;
        System.out.println("You've advanced to Level " + (levelIndex + 1) + "!");
        playerPos = new Vector2(1, 1);
        currState = GameState.walking;
        }
        
   }
   public static void ClearTile (Vector2 mt)
   {    

        String row = walkMap.get(mt.y);
        row = row.substring(0, mt.x) + " " + row.substring(mt.x + 1);
        walkMap.set(mt.y , row);
        /*
        for (int i = 0; i <= walkMap.size(); i++) 
        {
            String row = walkMap.get(i);

            if (mt.x < row.length() && mt.y < walkMap.size())
            {
                if (mt.y == (walkMap.size() - i)) 
                {
                    row = row.substring(0, mt.x) + " " + row.substring(mt.x + 1);
                    walkMap.set((walkMap.size() - i) , row);
                } else System.out.println("WRONGNG");
            } else System.out.println("sometihg went wrong");

    //THE HUGEST INTENSTINES
        }*/
    }
    public static float StringToFloat(String input)
    {
        //System.err.println("Btw this is input: " + input);
            try {
                    return Float.parseFloat(input);
                } catch (NumberFormatException e) {
                    System.err.println("Bro that's not a number.");
                    return -102345910;
                }
    }
    public static List<String> GetLinesInFile(String fileName) throws IOException
    {
        List<String> outputList = new ArrayList<String>();
        Path path = Paths.get(fileName).toAbsolutePath();
        //System.out.println("Path: " + path);
        BufferedReader br = new BufferedReader(new FileReader(path.toString()));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                outputList.add(line);
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            //String everything = sb.toString();
            //outputList.add(everything);
        } finally {
            br.close();
        }
        return outputList;
    }
    public static boolean EnemiesCleared()
    {
        boolean outputBool = true;
        for (int i = 0; i < walkMap.size(); i++)
        {
            String line = walkMap.get(i);
            for (int j = 0; j < line.length(); j++) 
            {
                if (Character.toString(line.charAt(j)).equals("q") || Character.toString(line.charAt(j)).equals("w") || Character.toString(line.charAt(j)).equals("e") || Character.toString(line.charAt(j)).equals("r"))
                {
                    outputBool = false;
                }
            }
        }
        return outputBool;
    }
}