package app;

import java.util.concurrent.Semaphore;

public class Core extends Thread {

  private String chipId = "";
  private String coreId = "";
  private String memAddr = "";
  private String typeInstr = "";
  private String data = "";

  private boolean switchOn = false;
  private boolean busRead = false;
  private boolean busWrite = false;
  private boolean L1miss = false;

  public String[][] L1 = {{"Block","Coherence","Mem.Address","Data"}, //Cache memory L1
                          {"0","I","",""},
                          {"1","I","",""}
  };
  
  private static Semaphore mutex = new Semaphore(1);
  
  Instructions instruction = new Instructions(); //instructions generator
  
  private String  nameLog;
  private Log log;


  //Constructor
  public Core(String msg,String _chipId, String _coreId){
    super(msg);
    this.chipId = _chipId;
    this.coreId = _coreId;

    this.nameLog = this.chipId+"-"+this.coreId;
    this.log = new Log(this.nameLog);
    log.newInfo("Prueba");
  }

  //----------------------------------METHODS--------------------------------------------
  /**
    * Split the instruction generated and save on array of strings
    * @param instruction: instruction generated (String)
    * @return  splitInstruction: Parts of the instruction (Array of strings)
  */
  private String[] instructionSplitter(String instruction){
    String[] splitInstruction = instruction.split(",");
    return splitInstruction;
  }

  /**
    * Check if the mem. address of the instruction and the mem. address of L1 are equals
    * @param splitInstr: instruction splitted (Array)
    * @return  boolean: True if the memory addresses are equals
  */
  private boolean equalAddress(String[] splitInstr){
    for (int i=1; i < L1.length; i++){
      if(splitInstr.length > 3){ //Validates if is an instruction READ/WRITE or CALC
        if(splitInstr[3].equals(L1[i][2])){ //Compare is the mem. address is the same
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Invalid State: Set the state
   * @param splitInstr: instruction splitted (Only memory address) 
  */
  public void invalidateCache(String splitInstr){
    for (int i=1; i < L1.length; i++) {
      if(splitInstr.equals(L1[i][2])){
        L1[i][1] = "I"; //Set state: Invalid
      }
    }
  }

  /**
   * Checking and validation of state Invalid (I)
   * @param splitInstr: instruction splitted (Array) 
  */
  private boolean checkStateI(String[] splitInstr){
    for (int i=1; i < L1.length; i++) {
      if(splitInstr.length > 3){ //Validates if is an instruction READ/WRITE or CALC
        if(splitInstr[3].equals(L1[i][2])){ //Compare is the mem. address is the same
          if(L1[i][1].equals("I")){
            return false;
          }
        }
      }
    } 
    return false;
  }

  /**
   *  Write instruction on L1 cache with the bus conection
   * @param memAddress: Memory Address of the request
  */
  private void busWriteL1(String memAddress){
    int newMemAddr = (Integer.parseInt(memAddress,2))%2; // variable newMemAddr are 1 or 2  
    for (int i=1; i < L1.length; i++) {
      if(L1[i][0].equals(Integer.toString(newMemAddr))){ //Compare the cache block with newMemAddr
        L1[i][1] = "S";
        L1[i][2] = memAddress;
        L1[i][3] = this.data;
      }
    }
  }

  /**
   *  Print cache table L1 in terminal and save it in log
   * @param L1: Cache L1
  */
  private void printTable(String[][] L1){
    System.out.println("Cache L1 de: "+this.chipId+"-"+this.coreId);
    for (int i=1; i < L1.length; i++) {
      if(L1[i][0].equals("")){
        L1[i][0] = "null";
      }if(L1[i][1].equals("")){
        L1[i][1] = "null";
      }if(L1[i][2].equals("")){
        L1[i][2] = "null";
      }if(L1[i][3].equals("")){
        L1[i][3] = "null";
      }
    }
    for (int i=1; i < L1.length; i++) {
      System.out.println(L1[i][0]+"||"+L1[i][1]+"||"+L1[i][2]+"||"+L1[i][3]);
    }
    log.newInfo("Cache L1 de: "+this.chipId+"-"+this.coreId+"\n"
    +L1[0][0]+"||"+L1[0][1]+"||"+L1[0][2]+"||"+L1[0][3]+"\n"
    +L1[1][0]+"||"+L1[1][1]+"||"+L1[1][2]+"||"+L1[1][3]+"\n"
    +L1[2][0]+"||"+L1[2][1]+"||"+L1[2][2]+"||"+L1[2][3]);
  }

  private void MSI(String[] splitInstr){
    if(splitInstr[2].equals("WRITE")){ // CASE 1: WRITE
      int i = (Integer.parseInt(splitInstr[3],2))%2 + 1; // variable i are 1 or 2  
      L1[i][1] = "M"; //write on L1 cache in block i. 
      L1[i][2] = splitInstr[3];
      L1[i][3] = splitInstr[4];
      System.out.println("WRITE operation on L1: "+chipId+" - "+coreId);
      log.newInfo("WRITE operation on L1: "+chipId+" - "+coreId);
      this.busWrite = true;
      this.typeInstr = splitInstr[2];
      this.memAddr = splitInstr[3];
      this.data = splitInstr[4];
      while(this.busWrite){
        try{
          Thread.sleep(1000); // 1 second
        }catch(Exception e){
          System.out.println(e.getMessage());
        }
      }
      try{
        Thread.sleep(4000); // 4 seconds
      }catch(Exception e){
        System.out.println(e.getMessage());
      }
      System.out.println("WRITE operation on L1: "+chipId+" - "+coreId+": SUCCESS");
      log.newInfo("WRITE operation on L1: "+chipId+" - "+coreId+": SUCCESS");
    }else if (splitInstr[2].equals("READ")){ //CASE 2: READ
      System.out.println("READ operation on L1: "+chipId+" - "+coreId);
      log.newInfo("READ operation on L1: "+chipId+" - "+coreId);
      if(equalAddress(splitInstr)){ //if find the address
        if(this.checkStateI(splitInstr)){
          this.L1miss = true;
          this.memAddr = splitInstr[3];
          while(this.L1miss){
            try{
              Thread.sleep(1000); // 1 second
            }catch(Exception e){
              System.out.println(e.getMessage());
            }
          }
          this.busWriteL1(this.memAddr);
        }
        try{
          Thread.sleep(1000); //1 second
      }catch(Exception e){
          System.out.println(e.getMessage());
      }
      }else{
        System.out.println("No data on L1: "+chipId+" - "+coreId);
        log.newInfo("No data on L1: "+chipId+" - "+coreId);
        this.L1miss = true;
        this.memAddr = splitInstr[3];
        System.out.println("Search data on L2 from: "+chipId+" - "+coreId);
        log.newInfo("Search data on L2 from: "+chipId+" - "+coreId);
        while(this.L1miss){
          try{
            Thread.sleep(1000); // 1 second
          }catch(Exception e){
            System.out.println(e.getMessage());
          }
        }
        System.out.println("Update with the data read on L1: "+chipId+" - "+coreId);
        log.newInfo("Update with the data read on L1: "+chipId+" - "+coreId);
        this.busWriteL1(this.memAddr);
      }
      System.out.println("READ operation w success on: "+chipId+" - "+coreId);
      log.newInfo("READ operation w success on: "+chipId+" - "+coreId);
    }else if(splitInstr[2].equals("CALC")){ //CASE 3: CALC
      System.out.println("CALC operation: "+chipId+" - "+coreId);
      log.newInfo("CALC operation: "+chipId+" - "+coreId);
      try{
        Thread.sleep(3000); // 3 seconds
      }catch(Exception e){
        System.out.println(e.getMessage());
      }
      System.out.println("CALC operation w success");
      log.newInfo("CALC operation w success");
    }
  } 

  public void newLog(String msg){
    this.log.newInfo(msg);
  }

  public void run(){
    while (this.switchOn){
      printTable(this.L1);
      String instruction = this.instruction.instGenerator(this.chipId, this.coreId);
      System.out.println("New instruction!----------");
      System.out.println(instruction+" to: "+this.chipId+"-"+this.coreId); 
      log.newInfo("New instruction: "+instruction);
      String[] splitInstruction = this.instructionSplitter(instruction);
      try {
        mutex.acquire();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      this.MSI(splitInstruction);
      mutex.release();
      System.out.println("Instruction done!----------");
    }
  }

  //-----------------------------GETTERS N SETTERS--------------------------------------
  //Getters and setters
  public String[][] getL1(){
    return L1;
  }
  public String getCoreId(){
    return coreId;
  }
  public boolean getSwitchOn(){
    return switchOn;
  }
  public void setSwitchOn(boolean switchOn){
    this.switchOn = switchOn;
  }
  public boolean getL1miss(){
    return L1miss;
  }
  public void setL1Miss(boolean L1miss){
    this.L1miss = L1miss;
  }
  public boolean getBusWrite(){
    return busWrite;
  }
  public void setBusWrite(boolean busWrite) {
    this.busWrite = busWrite;
  }
  public boolean getBusRead(){
    return busRead;
  }
  public void setBusRead(boolean busRead){
    this.busRead = busRead;
  }
  public String getMemAddr(){
    return memAddr;
  }
  public void setMemAddr(String memAddr){
    this.memAddr = memAddr;
  }
  public String getTypeInstr(){
    return typeInstr;
  }
  public void setTypeInstr(String typeInstr){
    this.typeInstr = typeInstr;
  }
  public String getData(){
    return data;
  }
  public void setData(String data){
    this.data = data;
  }
}