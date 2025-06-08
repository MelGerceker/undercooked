package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Key extends SuperObject {

    public OBJ_Key() {
        name = "Key";

        try {
            // image = ImageIO.read
            image = ImageIO.read(new File("assets/station.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
