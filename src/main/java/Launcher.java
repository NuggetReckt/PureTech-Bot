import fr.nuggetreckt.puretech.PureTech;

public class Launcher {
    public static void main(String[] args) {
        try {
            new PureTech();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
