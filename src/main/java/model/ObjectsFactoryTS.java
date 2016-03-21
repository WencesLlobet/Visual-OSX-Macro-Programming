package model;

import Constants.TSOConstants;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import logic.Caller;
import logic.ConnectionsChecker;
import logic.IdGenerator;
import model.tsobject.ObjectTS;
import model.tsobject.tsobjectparts.InputConnectionHubTS;
import model.tsobject.tsobjectparts.OutputConnectionHubTS;
import model.tsobject.tsobjectparts.Port;
import view.VObjectTS;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by quest on 17/3/16.
 */
public class ObjectsFactoryTS {

    private final Caller caller;
    ConnectionsChecker connectionChecker;
    IdGenerator idGenerator;
    public ObjectsFactoryTS(ConnectionsChecker cc, IdGenerator idGen, Caller caller) {
        this.connectionChecker = cc;
        this.idGenerator = idGen;
        this.caller = caller;
    }

    public ObjectTS setConectionHubs(ObjectTS obj) throws Throwable{
        InputConnectionHubTS input = new InputConnectionHubTS(this.connectionChecker);
        OutputConnectionHubTS output = new OutputConnectionHubTS(this.connectionChecker);
        obj.setInputsHub(input);
        obj.setOutputsHub(output);
        return obj;
    }

    public ObjectTS build(String type ) throws Throwable {
        ObjectTS newObj = null;

        if( type.startsWith(TSOConstants.DELAY_TSOBJID) ){
            newObj = createDelayTS();
        }
        if( type.startsWith(TSOConstants.SWITCH_TSOBJID) ){
            newObj = createSwitchTS();
        }
        newObj.setId(idGenerator.getNextId(newObj));
        newObj.registerToMvc(caller);

        refreshUiAfter300Ms();
        return newObj;
    }

    private ObjectTS createSwitchTS() throws Throwable {
        ObjectTS newObj;
        newObj = new SwitchTS();
        newObj = setConectionHubs(newObj);
        newObj.getOutputsHub().setPorts(generatePorts(Arrays.asList(
                TSOConstants.MANY,
                TSOConstants.MBANG
        )));
        newObj.getInputsHub().setPorts(generatePorts(Arrays.asList(
                TSOConstants.MANY,
                TSOConstants.MANY
        )));
        newObj.setW(90);
        newObj.setH(60);
        newObj.setX(800);
        newObj.setY(300);
        return newObj;
    }

    private ObjectTS createDelayTS() throws Throwable {
        ObjectTS newObj;
        newObj = new DelayTS();
        newObj = setConectionHubs(newObj);
        newObj.getOutputsHub().setPorts(generatePorts(Arrays.asList(
                TSOConstants.MANY
        )));
        newObj.getInputsHub().setPorts(generatePorts(Arrays.asList(
                TSOConstants.MANY,
                TSOConstants.MINT
        )));
        newObj.setW(90);
        newObj.setH(161);
        newObj.setX(300);
        newObj.setY(300);
        return newObj;
    }

    private ArrayList<Port> generatePorts(List<String> anymsg) {
        ArrayList<Port> ports = new ArrayList<Port>();
        for( String type : anymsg){
            ports.add(new Port(type));
        }
        return ports;
    }

///--------------------------------- SERIALIZATION

    public void storeAllModelsInFile(String file) throws Exception{
        String string = getStringsFromModel();
        System.out.println(string);
        writeIntoFile(string,file);
    }

    private void writeIntoFile(String string, String file) throws Exception {
        PrintStream out = new PrintStream(new FileOutputStream(file));
        out.print(string);
    }


    private String getStringsFromModel() throws Exception{
        ArrayList<ObjectTS> list = this.caller.getModelsInArray();
        String modelJson = serializeModel(list);
        return modelJson;

    }

    private String serializeModel(ArrayList<ObjectTS> list) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonInString = mapper.writeValueAsString(list);
        return jsonInString;
    }

    //// -_----------------- read

    public void loadFromFile(String file) throws Exception{
        String json =  readJsonFromFile(file);
        ObjectTS[] arrayList = deserializeObjectsTs(json);
        for(ObjectTS obj : arrayList){
            obj.registerToMvc(caller);
        }
        refreshUiAfter300Ms();
    }

    private void refreshUiAfter300Ms() {
        class Refresher implements Runnable {
            private final Collection<VObjectTS> views;

            public Refresher(Collection<VObjectTS> e){
                this.views = e;
            }
            public void run() {
                try{
                    Thread.sleep(300);
                    for(VObjectTS view : views){
                        view.resetSize();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
        Thread myThread = new Thread(new Refresher(this.caller.getViews()));
        myThread.start();
    }

    private String readJsonFromFile(String file) throws Exception{
        String content = new String(Files.readAllBytes(Paths.get(file)));
        return content;
    }

    private ObjectTS[] deserializeObjectsTs(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        final InjectableValues.Std injectableValues = new InjectableValues.Std();
        injectableValues.addValue("cc", this.connectionChecker);
        injectableValues.addValue("caller", this.caller);
        mapper.setInjectableValues(injectableValues);
        ObjectTS[] result = mapper.readValue(jsonString,ObjectTS[].class);
        return result;
    }

}
