package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Tomato extends SuperObject {

    public OBJ_Tomato() {
        name = "Tomato";

        try {
            // image = ImageIO.read
            image = ImageIO.read(new File("cbl asset/tomato.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
}