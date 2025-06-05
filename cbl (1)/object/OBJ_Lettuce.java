package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Lettuce extends SuperObject {

    public OBJ_Lettuce() {
        name = "Lettuce";

        try {
            // image = ImageIO.read
            image = ImageIO.read(new File("cbl asset/marul.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
