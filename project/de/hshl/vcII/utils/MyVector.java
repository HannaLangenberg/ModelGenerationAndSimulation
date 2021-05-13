    package project.de.hshl.vcII.utils;

    /**
     * Vector Class to do calculations with
     */
     public class MyVector {
     public double x, y;

     public MyVector(double x, double y){
        this.x = x;
        this.y = y;
     }

     /*
     * Methods for 2D Vectors (e.g. {2, 5}).
     */
    // Calculates the length of a vector
    public static double length(MyVector vec){
            return Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y, 2));
            }

    // Calculates the distance between two vectors
    public static double distance(MyVector v1, MyVector v2){
       return Math.sqrt(Math.pow(v2.x - v1.x, 2) * Math.pow(v2.y - v1.y, 2));
    }

    // Calculates the dot product. (vector * vector)
    public static double dot(MyVector v1, MyVector v2){
            return (v1.x * v2.x) + (v1.y * v2.y);
            }

    // Calculates the cross product. (vector x vector)
    public static double cross(MyVector v1, MyVector v2){
            return (v1.x*v2.y) - (v1.y*v2.x);
            }

    // Method for subtracting two 2D vectors from each other.
    public static MyVector subtract(MyVector v1, MyVector v2){
            return new MyVector(v1.x-v2.x, v1.y-v2.y);
            }

    // Method for calculating the angle between two 2D vectors.
    public static double angle(MyVector v1, MyVector v2){
        return Math.acos(Math.abs( dot(v1, v2 ) ) / Math.abs( length(v1) * length(v2) ));
    }

    public static MyVector multiply(MyVector vec, double lambda){
       return new MyVector(vec.x * lambda, vec.y * lambda);
    }

    /*
    public static MyVector add(MyVector vec, double lambda){
            return new MyVector(vec.x + lambda, vec.y + lambda);
            }
    */

    public static MyVector add(MyVector v1, MyVector v2){
            return new MyVector(v1.x + v2.x, v1.y + v2.y);
            }

    public static MyVector addMultiple(MyVector vec, MyVector... vectors){
        for(MyVector v : vectors)
        vec = MyVector.add(vec, v);
        return vec;
    }
 }