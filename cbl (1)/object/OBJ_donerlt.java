package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_donerlt extends SuperObject{

    public OBJ_donerlt() {
        name = "DonerLT";

        try{
            //image = ImageIO.read
            image = ImageIO.read(new File("assets/objects/donerlt.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
