package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Wrap extends SuperObject {

    public OBJ_Wrap() {
        name = "Wrap";

        try {
            // image = ImageIO.read
            image = ImageIO.read(new File("assets/tiles/wrap.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}