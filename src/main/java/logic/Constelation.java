package logic;


import org.opencv.features2d.KeyPoint;

import java.util.Comparator;

public class  Constelation {
    double[][] nearestPoints;
    int nstars;
    int insertedIndexNearestPoints = 0;
    double[] pos;
    PointsEqualator equalator;
    boolean isNotSponged = false;
    float spongeLimit = 0;
    float acumDist = 0;
    FeatureSearchParams params;

    public Constelation(double[] inpos,FeatureSearchParams params){

            pos = inpos;
            nearestPoints = new double[params.nstars][2];
            this.nstars = params.nstars;
            this.spongeLimit = params.spongeLimit;
    }
    public double[] createPoint(double x, double y){
        double[] pos = new double[2];
        pos[0]=x;
        pos[1]=y;
        return pos;
    }

    public Constelation(double[] obsPoint, Object[] modpNeightbours,FeatureSearchParams params) {
        this(obsPoint,params);
        for(Object pointo: modpNeightbours){
            KeyPoint point = (KeyPoint) pointo;
            addOwnPoint(createPoint(point.pt.x,point.pt.y));
        }
        equalator = new PointsEqualator(params.precision);
    }

    public void addOwnPoint(double[] point){
        if(insertedIndexNearestPoints >= nstars){
            System.out.println("adding to much stars");
            return;
        }
        double dx = point[0]-pos[0];
        double dy = point[1]-pos[1];
        acumDist += Math.sqrt(dx*dx+dy*dy);
        nearestPoints[insertedIndexNearestPoints][0] = dx;
        nearestPoints[insertedIndexNearestPoints][1] = dy;
        insertedIndexNearestPoints++;
        if(insertedIndexNearestPoints >= nstars){
            if(acumDist/nstars < spongeLimit){
                isNotSponged = true;
            }
        }
    }

    public float doesItMatch(Constelation constelation){
        double[][] other = constelation.nearestPoints;
        int similarity = 0;
        double[] star;
        if(isNotSponged || constelation.isNotSponged){
            return 0;
        }
        for(int j=0; j < nstars; j++){
            star = this.nearestPoints[j];
            for(int i=0; i < other.length; i++){
                double[] otherstar = other[i];
                if(equalator.compare(star,otherstar) == 0){
                    similarity ++;
                }
            }
        }
        return ((float)similarity)/(nstars*nstars);
    }

}