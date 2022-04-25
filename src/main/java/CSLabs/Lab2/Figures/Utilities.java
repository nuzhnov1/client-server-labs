package CSLabs.Lab2.Figures;

@SuppressWarnings("SameParameterValue")
class Utilities {
    static double randomRange(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}
