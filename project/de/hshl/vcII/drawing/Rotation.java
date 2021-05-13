package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.entities.stationary.Block;

public class Rotation {

    public void rotateLeft(Block b){
        b.getCollision().setRotate(b.getCollision().getRotate() < 90 ? b.getCollision().getRotate() - 1 : b.getCollision().getRotate());
        b.getTexture().setRotate(b.getTexture().getRotate() < 90 ? b.getTexture().getRotate() - 1 : b.getTexture().getRotate());
        b.setSpin(b.getSpin()+1);

        System.out.println("RotateLeft: " + b.getSpin());
    }

    public void rotateRight(Block b){
        b.getCollision().setRotate(b.getCollision().getRotate() > -90 ? b.getCollision().getRotate() + 1 : b.getCollision().getRotate());
        b.getTexture().setRotate(b.getTexture().getRotate() > -90 ? b.getTexture().getRotate() + 1 : b.getTexture().getRotate());
        b.setSpin(b.getSpin()-1);

        System.out.println("RotateRight: " + b.getSpin());
    }
}
