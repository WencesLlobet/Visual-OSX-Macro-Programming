package logic.features.featuresImage;

import logic.features.ClusterImage;
import logic.features.util.ClusterColider;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.KeyPoint;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by quest on 7/4/16.
 */
public class ClusterSimplifiedMat extends FeaturesMat {

    ClusterImage clusterKeyPointsSimplifier;

    int keyPointContactClusteringArea = 2;
    private MatOfKeyPoint defaultKeyPoints;

    public ClusterSimplifiedMat(Mat img) {
        clusterKeyPointsSimplifier = new ClusterImage(img.width(),img.height());
    }

    @Override
    public void setImage(Mat img){
        super.setImage(img);
        ClusterColider.reserveColisions();
        super.extractKeyPoints();
        defaultKeyPoints = keyPoints;
        filterKeypoints();
    }

    private void filterKeypoints(){
        ArrayList<KeyPoint> ans = clusterKeyPointsSimplifier.groupKeyPointsInSingleOnes(keyPoints.toArray(),
                keyPointContactClusteringArea);
        keyPoints = new MatOfKeyPoint();
        keyPoints.fromList(ans);
    }

    public int getKeyPointContactClusteringArea() {
        return keyPointContactClusteringArea;
    }

    public void setKeyPointContactClusteringArea(int keyPointContactClusteringArea) {
        this.keyPointContactClusteringArea = keyPointContactClusteringArea;
    }

    public MatOfKeyPoint getDefaultKeyPoints() {
        return defaultKeyPoints;
    }
}
