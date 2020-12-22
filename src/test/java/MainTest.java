import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


class MainTest {
    public static int[][] battleField;

    @BeforeAll
    public static void before() {
        battleField = new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
    }
    @Test
    public void readyToSetTest() {
        //Проверим корректность работы с границей поля
        assertThat(Main.readyToSet(2, 1, 1, 0, battleField), is(equalTo(false)));
        //Проверим корректность работы с соседним кораблем
        assertThat(Main.readyToSet(2, 1, 4, 2, battleField), is(equalTo(false)));
        //Проверим успешную проверку на установку корабля
        assertThat(Main.readyToSet(5, 5, 4, 2, battleField), is(equalTo(true)));
    }

    @Test
    public void neighborsIsValidTest() {
        assertThat(Main.neighborsIsValid(1, 1, battleField), is(equalTo(false)));
        assertThat(Main.neighborsIsValid(4, 1, battleField), is(equalTo(false)));
        assertThat(Main.neighborsIsValid(3, 3, battleField), is(equalTo(true)));
    }

}