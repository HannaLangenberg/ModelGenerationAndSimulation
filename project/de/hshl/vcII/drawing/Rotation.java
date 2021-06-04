package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.entities.stationary.Wall;

public class Rotation {

    public void rotateLeft(Object o){
        if(o instanceof Wall) {
            Wall w = (Wall) o;
            w.getCollision().setRotate(w.getCollision().getRotate() < 90 ? w.getCollision().getRotate() - 1 : w.getCollision().getRotate());
            w.getTexture().setRotate(w.getTexture().getRotate() < 90 ? w.getTexture().getRotate() - 1 : w.getTexture().getRotate());
            w.setSpin(w.getSpin() - 1);
            determineOrientation(w);
            System.out.println("Left - Spin: " + w.getSpin());
            System.out.println("Left - E: " + w.getE_alpha());
        }
        else if(o instanceof Scissors) {
            Scissors s = (Scissors) o;
            s.setRotate(s.getRotate() < 90 ? s.getRotate() - 1 : s.getRotate());
            s.setSpin(s.getSpin() - 1);
            determineOrientation(s);
            s.updateLines();
        }
    }

    public void rotateRight(Object o){
        if(o instanceof Wall) {
            Wall w = (Wall) o;
            w.getCollision().setRotate(w.getCollision().getRotate() > -90 ? w.getCollision().getRotate() + 1 : w.getCollision().getRotate());
            w.getTexture().setRotate(w.getTexture().getRotate() > -90 ? w.getTexture().getRotate() + 1 : w.getTexture().getRotate());
            w.setSpin(w.getSpin() + 1);
            determineOrientation(w);
            System.out.println("Right - Spin: " + w.getSpin());
            System.out.println("Right - E: " +  w.getE_alpha());
        }
        else if(o instanceof Scissors) {
            Scissors s = (Scissors) o;
            s.setRotate(s.getRotate() < 90 ? s.getRotate() + 1 : s.getRotate());
            s.setSpin(s.getSpin() + 1);
            determineOrientation(s);
        }
    }

    private void determineOrientation(Object o) {
        if(o instanceof Wall) {
            Wall w = (Wall) o;
            if (w.getSpin() != 0)
            {
                if(w.getSpin() <= -1) { // LINKS
                    w.setOrientation(0);
                    w.setE_alpha(Math.abs(w.getSpin())); //Math.abs should be redundant
                }
                else if (w.getSpin() >= 1) { // RECHTS
                    w.setOrientation(1);
                    w.setE_alpha(360 - Math.abs(w.getSpin())); //Math.abs should be redundant
                }
            }
            else {
                w.setOrientation(2); // LEVEL
                w.setE_alpha(0);
            }
            System.out.println("Orientation: " + w.getOrientation());
        }
        else if(o instanceof Scissors) {
            Scissors s = (Scissors) o;
            if (s.getSpin() != 0)
            {
                if(s.getSpin() <= -1) { // LINKS
                    s.setOrientation(0);
                    s.setE_alpha(Math.abs(s.getSpin())); //Math.abs should be redundant
                }
                else if (s.getSpin() >= 1) { // RECHTS
                    s.setOrientation(1);
                    s.setE_alpha(360 - Math.abs(s.getSpin())); //Math.abs should be redundant
                }
            }
            else {
                s.setOrientation(2); // LEVEL
                s.setE_alpha(0);
            }
        }
    }
}
