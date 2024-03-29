package view.UI;

import Constants.TSOConstants;
import logic.*;
import logic.imagematching.ImageFinderInScreen;
import model.MovementDetectorTS;
import model.ScreenImageFinderTS;
import model.TSObject.*;
import org.junit.Test;
import view.UI.connections.ConectionDisplayer;
import view.UI.connections.ConnectionCableFactory;
import view.UI.screencapturing.ScreenCapturer;

/**
 * Created by quest on 15/3/16.
 */
public class VObjectTSTest {

    //@Test
    public void testSetConnectionFromModel() throws Throwable {

       /* Caller c = new Caller();
        IdGenerator idg = new IdGenerator("testing");
        ConnectionsChecker cc = new ConnectionsChecker();
        ObjectsFactoryTS of = new ObjectsFactoryTS(cc,idg,c);
        ObjectTS ob = of.build(TSOConstants.DELAY_TSOBJID);

        Thread.sleep(10000);*/
    }

    @Test
    public void testSetConnectionFromMouseEvents() throws Throwable {

        BewteenWindowsConnectionMaker bwcm = new BewteenWindowsConnectionMaker();
        ConnectionCableFactory connectionCableFactory = new ConnectionCableFactory();
        PortPanelFactory ppf = new PortPanelFactory(bwcm,connectionCableFactory);
        ConnectionsChecker cc = new ConnectionsChecker();
        Caller caller = new Caller(ppf);
        ConectionDisplayer connectionDisplayer = new ConectionDisplayer(caller,cc, connectionCableFactory);
        bwcm.setCaller(caller,connectionDisplayer);
        IdGenerator idg = new IdGenerator("testing");
        ObjectsFactoryTS of = new ObjectsFactoryTS(cc,idg,caller);


        //ImageFinderInScreen ifis = new ImageFinderInScreen();

       // ObjectTS ob = of.build(TSOConstants.BANG_TSOBJID);
         ObjectTS bang = of.build(TSOConstants.BANG_TSOBJID);
         bang.setY(90);

        ObjectTS obj2 = of.build(TSOConstants.TEXT_MESSAGE_TSOBJID);

       // ObjectTS delay = of.build(TSOConstants.DELAY_TSOBJID);

      //  ObjectTS repetitive = of.build(TSOConstants.REPETITIVECOUNTDOWN_TSOBJID);

      //  ObjectTS ob2 = of.build(TSOConstants.TEXTPRINTER_TSOBJID);
      //  ObjectTS ob21 = of.build(TSOConstants.CLICKER_TSOBJID);
        ObjectTS textmessage = of.build(TSOConstants.TEXT_MESSAGE_TSOBJID);

       //   ObjectTS printer = of.build(TSOConstants.TEXTPRINTER_TSOBJID);


       // ObjectTS sfinder = of.build(TSOConstants.SCREEN_FEATURES_FINDE);


         ObjectTS mdetect = of.build(TSOConstants.MOVEMENT_DETECTOR_TSOBJID);

         mdetect.setY(200);
         ObjectTS ob22 = of.build(TSOConstants.THRESHOLD_TSOBJID);

        ObjectTS ob33 = of.build(TSOConstants.CLICKER_TSOBJID);


        ScreenCapturer screenCapturer = new ScreenCapturer();
        Thread capturerThread = new Thread(screenCapturer);
        capturerThread.start();

        ((MovementDetectorTS)mdetect).registerToCapturer(screenCapturer);

        /*ifis.registerToCapturer(screenCapturer);
        ((ScreenImageFinderTS)sfinder).registerToCapturer(screenCapturer);
        ((ScreenImageFinderTS)sfinder).registerToScreenSearcher(ifis);
*/

        LogicTicCaller lc = new LogicTicCaller(caller);

        Thread.sleep(2000000000);
        of.storeAllModelsInFile("areconnected.txt");
    }
}