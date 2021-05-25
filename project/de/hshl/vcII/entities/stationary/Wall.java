package project.de.hshl.vcII.entities.stationary;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import project.de.hshl.vcII.utils.MyVector;

/**
 * Wall is an abstract class.
 * It is a Framework for all non-moving (wall-/floor-like) entities that should be a Wall.
 */

public class Wall {
    public static final double DEFAULT_WIDTH = 300, DEFAULT_HEIGHT = 100;

    private Image texture;
    private ImageView viewTexture;
    private Rectangle collision;
    private MyVector posVec;
    private MyVector centerPoint;
    private double e_alpha;
    private double spin;
    private int number;
    private int orientation; //repräsentiert die Orientierung der Wand: 0 → level; 1 → rotateLeft; 2 → rotateRight

    public Wall(){
        texture = new Image(getClass().getResourceAsStream("/img/blocks/BlockNormal.png"));
        collision = new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        viewTexture = new ImageView(texture);
        posVec = new MyVector(0, 0);
        number = 0;
        orientation = 0;
    }

    public void calcCenterPoint(Wall w) {
        if(w.getCollision().getRotate() != 0) {
            double x = w.getCollision().getX() + ( w.getCollision().getWidth() * Math.cos(Math.toRadians(w.getCollision().getRotate()) + w.getCollision().getHeight() * Math.sin(Math.toRadians(w.getCollision().getRotate()))))/2;
            double y = w.getCollision().getY() + ( w.getCollision().getWidth() * Math.sin(Math.toRadians(w.getCollision().getRotate()) *-1 + w.getCollision().getHeight() * Math.cos(Math.toRadians(w.getCollision().getRotate()))))/2;

            setCenterPoint(new MyVector(x,y));
        }
        setCenterPoint(new MyVector(w.getCollision().getX() + w.getCollision().getWidth()/2, w.getCollision().getY() + w.getCollision().getHeight()/2));
    }



    //----Getter-&-Setter-----------------------------------------------------------------------------------------------
    public void setCenterPoint(MyVector centerPoint) {
        this.centerPoint = centerPoint;
    }
    public MyVector getCenterPoint() {
        return centerPoint;
    }

    public Rectangle getCollision(){
        return collision;
    }

    public ImageView getTexture() {
        return viewTexture;
    }

    public void setPosVec(MyVector posVec) {
        this.posVec = posVec;
        viewTexture.setX(posVec.x - collision.getWidth()/2);
        viewTexture.setY(posVec.y - collision.getHeight()/2);
        collision.setX(posVec.x - collision.getWidth()/2);
        collision.setY(posVec.y - collision.getHeight()/2);
        calcCenterPoint(this);
    }
    public MyVector getPosVec() {
        return posVec;
    }

    public void setSpin(double spin) {
        this.spin = spin;
    }
    public double getSpin() {
        return spin;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setE_alpha(double e_alpha) {
        this.e_alpha = e_alpha;
    }
    public double getE_alpha() {
        return e_alpha;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    public int getOrientation() {
        return orientation;
    }

    /*@Override
    public String toString() {
        return "No: " + this.number + ". -- Pos: (" + Math.round(this.posVec.x)+"/"+ Math.round(this.posVec.y)
                + ") -- image: (" + Math.round(Float.parseFloat(this.texture.toString()))
                + ") -- image view: (" + Math.round(Float.parseFloat(this.viewTexture.toString()))
                + ") -- rotation: " + this.spin + " -- bounding box: " + this.collision.toString();
    }*/
}
