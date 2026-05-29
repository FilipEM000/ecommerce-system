import entity.computer.Computer;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Computer computer = new Computer(1L, "name", new BigDecimal("100"), 5);

        System.out.println();
    }
}
