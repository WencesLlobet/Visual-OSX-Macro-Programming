package view.UI;

import Constants.TSOConstants;
import logic.*;
import model.MovementDetectorTS;
import model.ObjectsFactoryTS;
import model.tsobject.ObjectTS;
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

        ObjectTS ob = of.build(TSOConstants.REPETITIVECOUNTDOWN_TSOBJID);
        //ObjectTS ob2 = of.build(TSOConstants.SCREENPRINTER_TSOBJID);
        //ObjectTS ob2 = of.build(TSOConstants.CLICKER_TSOBJID);

        //ObjectTS ob3 = of.build(TSOConstants.MOVEMENT_DETECTOR_TSOBJID);

        //ob2.setX(500);


        ScreenCapturer screenCapturer = new ScreenCapturer();
        Thread capturerThread = new Thread(screenCapturer);
        capturerThread.start();
        //((MovementDetectorTS)ob3).registerToCapturer(screenCapturer);


        LogicTicCaller lc = new LogicTicCaller(caller);

        Thread.sleep(2000000000);
        of.storeAllModelsInFile("areconnected.txt");
    }



}