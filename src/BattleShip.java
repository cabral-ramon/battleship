import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class BattleShip {
    public static char[][] generateMap(int x, int y) {
        if (x == 0 || y == 0) {
            throw new IllegalArgumentException("Map cannot be of dimension 0x0");
        }
        char[][] map = new char[x][y];
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                map[i][j] = ' ';
            }
        }
        return map;
    }

    public static void printMap(char[][] map) {
        if (map.length == 0 || map[0].length == 0) {
            throw new IllegalArgumentException("Map cannot be of dimension 0x0");
        }

        String headerFooter = "";
        for (int k = 0; k < map.length; k++) {
            headerFooter = headerFooter + Integer.toString(k);
        }

        System.out.println("   " + headerFooter + "   ");
        for (int i = 0; i < map.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == '2') {
                    System.out.print(' ');
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println("| " + i);
        }

        System.out.println("   " + headerFooter + "   ");
    }

    public static void printMap(char[][] map, char player, char replaceSymbol) {
        if (map.length == 0 || map[0].length == 0) {
            throw new IllegalArgumentException("Map cannot be of dimension 0x0");
        }

        String headerFooter = "";
        for (int k = 0; k < map.length; k++) {
            headerFooter = headerFooter + Integer.toString(k);
        }

        System.out.println("   " + headerFooter + "   ");
        for (int i = 0; i < map.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == player) {
                    System.out.print(replaceSymbol);
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println("| " + i);
        }

        System.out.println("   " + headerFooter + "   ");
    }

    public static char[][] generateUserShips(char[][] map) {
        Scanner input = new Scanner(System.in);
        int mapLength = map.length;
        for(int i = 1; i < 6; i++) {
            int x;
            int y;
            do {
                System.out.print("Enter X coordinate for your ship " + i +": ");
                x = input.nextInt();
                System.out.print("Enter Y coordinate for your ship " + i + ": ");
                y = input.nextInt();
            } while (( x > mapLength - 1 || y > mapLength - 1) || map[y][x] == '1');
            map[y][x] = '1';
        }
        return map;
    }

    public static char[][] generateUserTestShips(char[][] map) {
        map[1][1] = '1';
        map[2][2] = '1';
        map[3][3] = '1';
        map[4][4] = '1';
        map[5][5] = '1';
        return map;
    }

    public static char[][] generateComputerShips(char[][] map) {
        Random rand = new Random();
        int mapLength = map.length;
        System.out.println("Computer is deploying ships");
        for(int i = 1; i < 6; i++) {
            int x = rand.nextInt(mapLength);
            int y = rand.nextInt(mapLength);
            while(map[x][y] != ' ') {
                x = rand.nextInt(mapLength);
                y = rand.nextInt(mapLength);
            }
            map[x][y] = '2';
            System.out.println(i + ". ship DEPLOYED");
        }
        return map;
    }

    public static boolean validMove(char[][] map, int x, int y, ArrayList<int[]> guesses, char player) {
        if (x >= map.length || y >= map.length) {
            if (player == '1') {
                System.out.println("This guess is out of range");
            }
            return false;
        }
        int[] coords = {y, x};
        if (guesses.contains(coords))   {
            if (player == '1') {
                System.out.println("You have already guessed these coordinates before");
            }
            return false;
        }
        if (map[y][x] == player) {
            if (player == '1') {
                System.out.println("You cannot hit your own ship");
            }
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        char[][] map = generateMap(10,10);
        printMap(map);
//        map = generateUserShips(map);
        map = generateUserTestShips(map);
        printMap(map, '1', '@');
        map = generateComputerShips(map);
        int userShipsCount = 5;
        int computerShipsCount = 5;
        int mapLength = map.length;
        ArrayList<int[]> userGuesses = new ArrayList<int[]>();
        ArrayList<int[]> computerGuesses = new ArrayList<int[]>();
        Scanner input = new Scanner(System.in);
        while(userShipsCount != 0 && computerShipsCount != 0) {
            System.out.println("YOUR TURN");
            int x;
            int y;
            do {
                System.out.print("Enter X coordinate:");
                x = input.nextInt();
                System.out.print("Enter Y coordinate:");
                y = input.nextInt();
            } while (!validMove(map, x, y, userGuesses, '1'));

            if (map[y][x] == ' ') {
                System.out.println("You missed");
                map[y][x] = '-';
            } else {
                System.out.println("Boom! You sunk the ship!");
                map[y][x] ='!';
                computerShipsCount -= 1;
            }
            printMap(map);

            // Computer's turn logic
            Random rand = new Random();
            int computerX = rand.nextInt(mapLength);
            int computerY = rand.nextInt(mapLength);
            while(!validMove(map, computerX, computerY, computerGuesses, '2')) {
                computerX = rand.nextInt(mapLength);
                computerY = rand.nextInt(mapLength);
            }
            if (map[computerY][computerX] == '1') {
                System.out.println("Computer sunk one of your ships");
                map[computerY][computerX] = '#';
                userShipsCount -= 1;
            } else {
                // computer missed
                System.out.println("Computer missed");
                map[computerY][computerX] = '*';
            }
            printMap(map);
        }
        if (userShipsCount == 0) {
            System.out.println("You lost");
        } else {
            System.out.println("You win! Congratulations");
        }
    }
}
