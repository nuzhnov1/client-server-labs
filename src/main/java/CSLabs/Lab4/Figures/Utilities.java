package CSLabs.Lab4.Figures;

@SuppressWarnings("SameParameterValue")
class Utilities {
    /**
     * Get a random number from a given range
     * @param min minimum value of range
     * @param max maximum value of range (not included in the range)
     * @return random number between min and max (not included)
     */
    static double randomRange(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}
