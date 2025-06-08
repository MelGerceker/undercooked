package object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_donert extends SuperObject{

    public OBJ_donert() {
        name = "donert";

        try{
            //image = ImageIO.read
            image = ImageIO.read(new File("assets/donert.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}