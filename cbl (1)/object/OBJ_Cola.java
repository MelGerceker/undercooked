package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Cola extends SuperObject {

    public OBJ_Cola() {
        name = "Cola";

        try {
            // image = ImageIO.read
            image = ImageIO.read(new File("assets/tiles/cola.png"));
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}