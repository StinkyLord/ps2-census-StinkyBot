package stinkybot.utils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
public class JoinImage {

        public static File joinImages(String image1, String image2)
        {
            File file = new File("joined.png");
            try {
                URL url1 = new URL(image1);
                URL url2 = new URL(image2);
                BufferedImage img1 = ImageIO.read(url1);
                BufferedImage img2 = ImageIO.read(url2);
                BufferedImage joinedImg = joinBufferedImage(img1, img2);

                boolean success = ImageIO.write(joinedImg, "png", file);
                System.out.println("saved success? "+success);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return file;
        }
        /**
         * join two BufferedImage
         * you can add a orientation parameter to control direction
         * you can use a array to join more BufferedImage
         */

        public static BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2) {

            //do some calculate first
            int offset  = 5;
            int wid = img1.getWidth()+img2.getWidth()+offset;
            int height = Math.max(img1.getHeight(),img2.getHeight())+offset;
            //create a new buffer and draw two image into the new image
            BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = newImage.createGraphics();
            Color oldColor = g2.getColor();
            //fill background
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, wid, height);
            //draw image
            g2.setColor(oldColor);
            g2.drawImage(img1, null, 0, 0);
            g2.drawImage(img2, null, img1.getWidth()+offset, 0);
            g2.dispose();
            return newImage;
        }

}

