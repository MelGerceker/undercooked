package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_cutLettuce extends SuperObject {

    public OBJ_cutLettuce() {
        name = "cutLettuce";

        try {
            // image = ImageIO.read
            image = ImageIO.read(new File("cbl asset/cutLettuce.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
