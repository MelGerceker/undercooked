package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_donerl extends SuperObject {

    public OBJ_donerl() {
        name = "DonerL";

        try {
            // image = ImageIO.read
            image = ImageIO.read(new File("assets/objects/donerl.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
