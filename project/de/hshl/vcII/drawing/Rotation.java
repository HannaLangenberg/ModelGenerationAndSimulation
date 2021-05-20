package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.entities.stationary.Wall;

public class Rotation {

    public void rotateLeft(Wall w){
        w.getCollision().setRotate(w.getCollision().getRotate() < 90 ? w.getCollision().getRotate() - 1 : w.getCollision().getRotate());
        w.getTexture().setRotate(w.getTexture().getRotate() < 90 ? w.getTexture().getRotate() - 1 : w.getTexture().getRotate());
        w.setSpin(w.getSpin()-1);

        System.out.println("RotateLeft: " + w.getSpin());
    }

    public void rotateRight(Wall w){
        w.getCollision().setRotate(w.getCollision().getRotate() > -90 ? w.getCollision().getRotate() + 1 : w.getCollision().getRotate());
        w.getTexture().setRotate(w.getTexture().getRotate() > -90 ? w.getTexture().getRotate() + 1 : w.getTexture().getRotate());
        w.setSpin(w.getSpin()+1);

        System.out.println("RotateRight: " + w.getSpin());
    }
}
