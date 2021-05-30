package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.entities.stationary.Wall;

public class Rotation {

    public void rotateLeft(Wall w){
        w.getCollision().setRotate(w.getCollision().getRotate() < 90 ? w.getCollision().getRotate() - 1 : w.getCollision().getRotate());
        w.getTexture().setRotate(w.getTexture().getRotate() < 90 ? w.getTexture().getRotate() - 1 : w.getTexture().getRotate());
        w.setSpin(w.getSpin() - 1);
        determineOrientation(w);
        System.out.println("Left - Spin: " + w.getSpin());
        System.out.println("Left - E: " + w.getE_alpha());

    }

    public void rotateRight(Wall w){
        w.getCollision().setRotate(w.getCollision().getRotate() > -90 ? w.getCollision().getRotate() + 1 : w.getCollision().getRotate());
        w.getTexture().setRotate(w.getTexture().getRotate() > -90 ? w.getTexture().getRotate() + 1 : w.getTexture().getRotate());
        w.setSpin(w.getSpin() + 1);
        determineOrientation(w);
        System.out.println("Right - Spin: " + w.getSpin());
        System.out.println("Right - E: " +  w.getE_alpha());
    }

    private void determineOrientation(Wall w) {
        if (w.getSpin() != 0)
        {
            if(w.getSpin() <= -1) { // LINKS
                w.setOrientation(0);
                w.setE_alpha(Math.abs(w.getSpin()) * -1); //Math.abs should be redundant
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
}
