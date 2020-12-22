import java.util.Random;
import java.util.Scanner;


public class Main {

    public static final int SIZE = 10;
    public static final int MAXDECKS = 4;

    public static final char HIT   = '\u25a9';
    public static final char WATER = '\u25A2';
    public static final char MISS  = '\u25aa';
    public static final char SEPARATOR = '\u25ab';

    public static void main(String[] args) throws Exception {

        int shots = 55;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n\u224B \u224B \u224B \u224B \u224B Морской бой \u224B \u224B \u224B \u224B \u224B");
        System.out.println("Игра идет по стандартным правилам: ");
        System.out.println("1. 4 вида кораблей: четырехпалубный, два трехпалубных, три двухпалубных и четыре однопалубных;");
        System.out.println("2. Корабли НЕ стоят рядом;");
        System.out.println("3. Для победы необходимо потопить все корабли за 55 ходов.\n");
        System.out.println("Условные обозначения: ");
        System.out.println(WATER + " - нераскрытое поле, " + HIT + " - попадание," + MISS + " - промах.\n");

        int[][] battleField = fillTheFieldWithTheShips();
        printBattleField(battleField);

        while (shots != 0) {
            System.out.println("У вас осталось " + shots + " выстрелов.");
            makeShot(battleField, scanner);
            if (gameIsOver(battleField)) {
                System.out.println("Поздравляю, вы великолепны! Все корабли потоплены!");
                return;
            }
            shots--;
            printBattleField(battleField);
        }
        if (shots == 0 && !gameIsOver(battleField)) {
            System.out.println("К сожалению вы не справились! Попробуйте ещё раз.");
        }
    }

    //Метод заполняет пустое поле кораблями
    private static int[][] fillTheFieldWithTheShips() {

        int[][] battleField = new int[SIZE][SIZE];

        setShip(battleField, MAXDECKS);
        setShip(battleField, MAXDECKS - 1);
        setShip(battleField, MAXDECKS - 1);
        setShip(battleField, MAXDECKS - 2);
        setShip(battleField, MAXDECKS - 2);
        setShip(battleField, MAXDECKS - 2);
        setShip(battleField, MAXDECKS - 3);
        setShip(battleField, MAXDECKS - 3);
        setShip(battleField, MAXDECKS - 3);
        setShip(battleField, MAXDECKS - 3);

        return battleField;
    }

    //Метод устанавливает корабль с указанным количеством палуб в случайное поле
    private static void setShip(int[][] battleField, int decks) {

        boolean shipReady = false;
        Random random = new Random();

        do {
            int x = random.nextInt(SIZE - 1);
            int y = random.nextInt(SIZE - 1);
            int direction = random.nextInt(3);   // значение задается генератором рандомных чисел от 0 до 3.
            // 0 - вверх, 1 - вправо, 2 - вниз, 3 - влево.

            //Проверяем, подходят ли случайные координаты для установки корабля, если не подходят - генерируем значения ещё раз
            if (!readyToSet(x, y, decks, direction, battleField)) {
                continue;
            }
            shipReady = true;
            //В зависимости от направления размещаем корабль
            switch (direction) {
                case 0: {
                    for (int i = decks - 1; i >= 0; i--) {
                        battleField[x - i][y] = 1;
                    }
                    break;
                }
                case 1: {
                    if (y + decks <= SIZE - 1) {
                        for (int i = decks - 1; i >= 0; i--) {
                            battleField[x][y + i] = 1;
                        }
                    }
                    break;
                }
                case 2: {
                    if (x + decks <= SIZE - 1) {
                        for (int i = decks - 1; i >= 0; i--) {
                            battleField[x + i][y] = 1;
                        }
                    }
                    break;
                }
                case 3: {
                    if (y - decks >= 0) {
                        for (int i = decks - 1; i >= 0; i--) {
                            battleField[x][y - i] = 1;
                        }
                    }
                    break;
                }
            }
        } while (!shipReady);
    }

    //Метод, проверяющий возможность установки корабля по указанным координатам и с указанным направлением
    public static boolean readyToSet(int x, int y, int decks, int direction, int[][] battleField) {

        //Если в этих координатах уже есть корабль - возвращаем ложь
        if (battleField[x][y] == 1) return false;

        //В зависимости от направления проверяем наличие кораблей в клетках (в том числе и соседних)
        switch (direction) {
            case 0: {
                if (x - decks < 0) return false;
                for (int i = decks - 1; i >= 0; i--) {
                    if ((battleField[x - i][y] == 1) || (!neighborsIsValid(x - i, y, battleField))) {
                        return false;
                    }
                }
                break;
            }
            case 1: {
                if (y + decks > SIZE - 1) return false;
                for (int i = decks - 1; i >= 0; i--) {
                    if ((battleField[x][y + i] == 1) || (!neighborsIsValid(x, y + i, battleField))) {
                        return false;
                    }
                }
                break;
            }
            case 2: {
                if (x + decks > SIZE - 1) return false;
                for (int i = decks - 1; i >= 0; i--) {
                    if ((battleField[x + i][y] == 1) || (!neighborsIsValid(x + i, y, battleField))) {
                        return false;
                    }
                }
                break;
            }
            case 3: {
                if (y - decks < 0) return false;
                for (int i = decks - 1; i >= 0; i--) {
                    if ((battleField[x][y - i] == 1) || (!neighborsIsValid(x, y - i, battleField))) {
                        return false;
                    }
                }
                break;
            }
        }

        return true;
    }

    //Метод, проверяющий соседние клетки на наличие кораблей
    public static boolean neighborsIsValid(int x, int y, int[][] battleField) {
        //Обработаем угловые значения, для них нужно проверить 3 ближайших соседа
        if (x == 0 && y == 0) {
            if ((battleField[0][1] == 1) || (battleField[1][0] == 1) || (battleField[1][1] == 1)) {
                return false;
            }
        }
        if (x == 0 && y == SIZE - 1) {
            if ((battleField[0][SIZE - 2] == 1) || (battleField[1][SIZE - 1] == 1) || (battleField[1][SIZE - 2] == 1)) {
                return false;
            }
        }
        if (x == SIZE - 1 && y == 0) {
            if ((battleField[SIZE - 1][1] == 1) || (battleField[SIZE - 2][0] == 1) || (battleField[SIZE - 2][1] == 1)) {
                return false;
            }
        }
        if (x == SIZE - 1 && y == SIZE - 1) {
            if ((battleField[SIZE - 2][SIZE - 1] == 1) || (battleField[SIZE - 1][SIZE - 2] == 1) || (battleField[SIZE - 2][SIZE - 2] == 1)) {
                return false;
            }
        }

        //Обработаем пограничные значения
        if (x == 0 && y != 0 && y != SIZE - 1) {
            if ((battleField[x][y - 1] == 1) || (battleField[x][y + 1] == 1) || (battleField[x + 1][y] == 1) || (battleField[x + 1][y - 1] == 1) || (battleField[x + 1][y + 1] == 1)) {
                return false;
            }
        }
        if (y == SIZE - 1 && x != 0 && x != SIZE - 1) {
            if ((battleField[x - 1][y] == 1) || (battleField[x + 1][y] == 1) || (battleField[x - 1][y - 1] == 1) || (battleField[x - 1][y] == 1) || (battleField[x + 1][y - 1] == 1)) {
                return false;
            }
        }
        if (x == SIZE - 1 && y != 0 && y != SIZE - 1) {
            if ((battleField[x - 1][y] == 1) || (battleField[x][y - 1] == 1) || (battleField[x][y + 1] == 1) || (battleField[x - 1][y - 1] == 1) || (battleField[x - 1][y + 1] == 1)) {
                return false;
            }
        }
        if (y == 0 && x != 0 && x != SIZE - 1) {
            if ((battleField[x - 1][y] == 1) || (battleField[x][y + 1] == 1) || (battleField[x + 1][y] == 1) || (battleField[x - 1][y + 1] == 1) || (battleField[x + 1][y + 1] == 1)) {
                return false;
            }
        }

        //Обработаем все остальные случаи
        if (x != 0 && x != SIZE - 1 && y != 0 && y != SIZE - 1) {
            if ((battleField[x - 1][y - 1] == 1) || (battleField[x - 1][y] == 1) || (battleField[x - 1][y + 1] == 1) || (battleField[x][y - 1] == 1) || (battleField[x][y + 1] == 1)
                    || (battleField[x + 1][y - 1] == 1) || (battleField[x + 1][y] == 1) || (battleField[x + 1][y + 1] == 1)) {
                return false;
            }
        }
        return true;
    }

    //Метод выводит в консоль поле боя
    private static void printBattleField(int[][] battleField) {

        System.out.println("   "+SEPARATOR+"а"+SEPARATOR+"б"+SEPARATOR+"в"+SEPARATOR+"г"
                +SEPARATOR+"д"+SEPARATOR+"е"+SEPARATOR+"ж"+SEPARATOR+"з"+SEPARATOR+"и"+SEPARATOR+"к");

        for (int i = 0; i < SIZE; i++) {
            if (i < SIZE - 1) System.out.print(i + 1 + "|  ");
            if (i == SIZE - 1) System.out.print(i + 1 + "| ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(replaceSymbol(battleField[i][j])+ " ");
            }
            System.out.println();
        }
    }

    //Метод, заменяющий числа на символы поля боя
    private static char replaceSymbol(int pos) {

        switch (pos) {
            case 0 :
            case 1 : {
                return WATER;
            }
            case 2 : {
                return HIT;
            }
            case 3 : {
                return MISS;
            }
        }
        return WATER;
    }

    //Метод для осуществления стрельбы по полю
    private static void makeShot(int[][] battleField, Scanner scanner) {

        String input;

        do {
            System.out.println("Введите координаты выстрела в формате: 'x' 'y' (например 'а 3')");
            input = scanner.nextLine().trim();
        } while (!inputCorrect(input));

        String[] parts = input.split(" ");

        int x = Integer.parseInt(parts[1]) - 1;
        int y = getY(parts[0]) - 1;

        if (battleField[x][y] == 0) {
            //miss
            System.out.println("\n           Промах!\n");
            battleField[x][y] = 3;
        }
        if (battleField[x][y] == 1) {
            //hit
            System.out.println("\n       Есть попадание!\n");
            battleField[x][y] = 2;
            //Если попали в корабль - проверим, возможно он полностью уничтожен
            hitNeighborsWhenShipSunk(battleField, x, y);
        }
    }

    //Метод, заменяющий символ, введенный пользователем, на число для массива
    private static int getY (String s) {
        if ((s.equals("а")) || (s.equals("a")) || (s.equals("f"))) return 1;
        if (s.equals("б") || s.equals(",")) return 2;
        if (s.equals("в") || s.equals("d")) return 3;
        if (s.equals("г") || s.equals("u")) return 4;
        if (s.equals("д") || s.equals("l")) return 5;
        if (s.equals("е") || s.equals("t")) return 6;
        if (s.equals("ж") || s.equals(";")) return 7;
        if (s.equals("з") || s.equals("p")) return 8;
        if (s.equals("и") || s.equals("b")) return 9;
        if (s.equals("к") || s.equals("r")) return 10;
        return 0;
    }

    //Метод, проверяющий наличие не потопленных кораблей
    private static boolean gameIsOver(int[][] battleField) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (battleField[i][j] == 1) return false;
            }
        }
        return true;
    }

    //Метод, проверяющий потоплен ли корабль
    private static void hitNeighborsWhenShipSunk(int[][] battleField, int x, int y) {

        boolean shipSunk = true;

        //Проверим, убит ли корабль. Проверяем во всех направлениях. Если встретили '1' - значит корабль жив
        for (int isX = 0; isX <= 1; isX++) {
            for (int direction = -1; direction <= 1; direction += 2) {
                int dx = isX * direction;
                int dy = (1 - isX) * direction;
                for (int step = 0; step < MAXDECKS; step++) {
                    if (x + step * dx < 0 || y + step * dy < 0 || y + step * dy > SIZE - 1 || x + step * dx > SIZE - 1) continue;
                    if (battleField[x + step * dx][y + step * dy] == 0 || battleField[x + step * dx][y + step * dy] == 3) break;
                    if (battleField[x + step * dx][y + step * dy] == 1) {
                        shipSunk = false;
                        break;
                    }
                }
            }
        }

        //если корабль 'убит' - помечаем область вокруг как miss
        if (shipSunk) {
            for (int isX = 0; isX <= 1; isX++) {
                for (int direction = -1; direction <= 1; direction += 2) {
                    int dx = isX * direction;
                    int dy = (1 - isX) * direction;
                    for (int step = 0; step < MAXDECKS; step++) {
                        if (x + step * dx < 0 || y + step * dy < 0 || y + step * dy > SIZE - 1 || x + step * dx > SIZE - 1) continue;
                        if (battleField[x + step * dx][y + step * dy] == 2) {
                            markNeighbors(battleField, x + step * dx, y + step * dy);
                        }
                    }
                }
            }
        }
    }

    //Метод, отмечающий соседние клетки в случае потопления корабля
    private static void markNeighbors(int[][] battleField, int x, int y) {
        if (x - 1 >= 0 && y - 1 >= 0) battleField[x - 1][y - 1] = 3;
        if (x + 1 <= SIZE - 1 && y - 1 >= 0) battleField[x + 1][y - 1] = 3;
        if (x - 1 >= 0 && y + 1 <= SIZE - 1) battleField[x - 1][y + 1] = 3;
        if (x + 1 <= SIZE - 1 && y + 1 <= SIZE - 1) battleField[x + 1][y + 1] = 3;
        if (x - 1 >= 0) {
            if (battleField[x - 1][y] != 2) battleField[x - 1][y] = 3;
        }
        if (y - 1 >= 0) {
            if (battleField[x][y - 1] != 2) battleField[x][y - 1] = 3;
        }
        if (x + 1 <= SIZE - 1) {
            if (battleField[x + 1][y] != 2) battleField[x + 1][y] = 3;
        }
        if (y + 1 <= SIZE - 1) {
            if (battleField[x][y + 1] != 2) battleField[x][y + 1] = 3;
        }
    }

    //Метод проверяет корректность введенных данных пользователем при выстреле
    private static boolean inputCorrect(String input) {

        try {
            String[] parts = input.split(" ");

            int x = Integer.parseInt(parts[1]) - 1;
            int y = getY(parts[0]) - 1;

            if ((x >= 0 && x <= SIZE - 1) && (y >= 0 && y <= SIZE - 1)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
