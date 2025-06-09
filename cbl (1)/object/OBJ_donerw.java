package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_donerw extends SuperObject{

    public OBJ_donerw() {
        name = "DonerW";

        try{
            //image = ImageIO.read
            image = ImageIO.read(new File("assets/donerw.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}