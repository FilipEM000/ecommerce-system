package entity.computer;

import exception.InvalidRamFormatException;

public record Ram(int capacity, int frequency, int modules) {
    public Ram(int capacity, int frequency, int modules) {
        if (capacity > 0 && (capacity & (capacity - 1)) == 0 && frequency > 0 && modules > 0) {
            this.capacity = capacity;
            this.frequency = frequency;
            this.modules = modules;
        } else {
            throw new InvalidRamFormatException("Niepoprawny format RAMu");
        }
    }
}
