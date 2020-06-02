package app;

import java.util.concurrent.Semaphore;

public class Chip extends Thread {
  
  public boolean switchOn;
  private boolean missL2;
  private boolean busWrite = false;
  private boolean exportL2 = false;
  
  private String chipId = "";
  private String memAddrL2 = "";
  private String data = "";
  private String coreId = ""; 

  private Core core0;
  private Core core1;

  public String[][] L2={{"Block", "State","Owner", "Mem. Address", "Data"},
                        {"0","","","","",""},
                        {"1","","","","",""},
                        {"2","","","","",""},
                        {"3","","","","",""}};
  private String[] lineL2;                      
 
  private static Semaphore mutex = new Semaphore(1);

  //Constructor
  public Chip(String msg, String _chipId, boolean _switchOn){
    super(msg);
    this.chipId = _chipId;
    this.switchOn = _switchOn;
    this.core0 = new Core("Core0", this.chipId, "0");
    this.core1 = new Core("Core1", this.chipId, "1");             
    this.core0.setSwitchOn(this.switchOn);
    this.core1.setSwitchOn(this.switchOn);                       
  }

  //-----------------------------METHODS------------------------------------
  /**
   * Validate if the memory address is the same and if its different of state DI
   * @param memAdd: memory address of the instruction
  */
  public boolean checkMemAddress(String memAdd){
    for (int i=1; i < L2.length; i++) {
        if(!L2[i][1].equals("DI") && memAdd.equals(L2[i][3])){ //Compare is the mem. address is the same
            this.data = L2[i][4];
            return true;
        }
    }
    return false;
  }

  /**
   * Invalid State: Set the state
   * @param memAddr: memory address of the instruction
  */
  public void invalidateCacheL2(String memAddr){
    for (int i=1; i < L2.length; i++) {
      if(memAddr.equals(L2[i][3])){
        L2[i][1] = "DI";
        break;
      }
    }
    this.core0.invalidateCache(memAddr);
    this.core1.invalidateCache(memAddr);
  }

  /**
   * Obtain the data of the instruction and save it in an auxiliary 
   * @param memAddr: memory address of the instruction
   * @return dataAux
  */
  public String getDataAux(String memAddr){
    String dataAux = "";
    for (int i=1; i < L2.length; i++) {
      if(memAddr.equals(L2[i][3])){
        dataAux = L2[i][4];
      }
    }
    return dataAux;
  }

  /**
   * Obtain the data of the instruction 
   * @param memAddr: memory address of the instruction
   * @return data
  */
  private String getDataL2(String memAddr){
    for (int i=1; i < L2.length; i++) {
        if(memAddr.equals(L2[i][3])){
            this.data = L2[i][4];
            return data;
        }
    }
    return this.data;
  }

  /**
   * Obtain the cache line of L2
   * @param memAddr: memory address of the instruction
   * @return lineL2: cache line of L2 
  */
  public String[] getCacheLine(String memAddr) {
    for (int i=1; i < L2.length; i++) {
        if(memAddr.equals(L2[i][3])){
            this.lineL2 = L2[i];
        }
    }
    return this.lineL2;
  }

  /**
   * Obtain the value of the position x from L2
   * @param memAddr: memory address of the instruction
   * @param x: positon in the matrix L2
   * @return value
  */
  public String getValueL2(int x, String memAddr){
    String value = "";
    for (int i=1; i < L2.length; i++) {
      if(memAddr.equals(L2[i][3])){
        value = this.L2[i][x];
      }
    }
    return value;
  }

  /**
   * Put a new value on the position x of L2
   * @param memAddr: memory address of the instruction
   * @param x: position in the matrix L2
   * @param value: value to put in L2
  */
  public void setValueL2(int x, String memAddr, String value){
    for (int i=1; i < L2.length; i++) {
      if(memAddr.equals(L2[i][3])){
        L2[i][x] += value;
        break;
      }
    }
  }

  public void printTable(String[][] L2){
    System.out.println("Cache L2 de: "+this.chipId);
    for (int i=1; i < L2.length; i++) {
      if(L2[i][0].equals("")){
        L2[i][0] = "null";
      }if(L2[i][1].equals("")){
        L2[i][1] = "null";
      }if(L2[i][2].equals("")){
        L2[i][2] = "null";
      }if(L2[i][3].equals("")){
        L2[i][3] = "null";
      }if(L2[i][4].equals("")){
        L2[i][4] = "null";
      }
    }
    for (int i=1; i < L2.length; i++) {
      System.out.println(L2[i][0]+"||"+L2[i][1]+"||"+L2[i][2]+"||"+L2[i][3]+"||"+L2[i][4]);
    }
    if(core0.getSwitchOn()){
      core0.newLog("Cache L2 de: "+this.chipId+"\n"
      +L2[0][0]+"||"+L2[0][1]+"||"+L2[0][2]+"||"+L2[0][3]+"||"+L2[0][4]+"\n"
      +L2[1][0]+"||"+L2[1][1]+"||"+L2[1][2]+"||"+L2[1][3]+"||"+L2[1][4]+"\n"
      +L2[2][0]+"||"+L2[2][1]+"||"+L2[2][2]+"||"+L2[2][3]+"||"+L2[2][4]+"\n"
      +L2[3][0]+"||"+L2[3][1]+"||"+L2[3][2]+"||"+L2[3][3]+"||"+L2[3][4]+"\n"
      +L2[4][0]+"||"+L2[4][1]+"||"+L2[4][2]+"||"+L2[4][3]+"||"+L2[4][4]+"\n");
    }else if(core1.getSwitchOn()){
      core1.newLog("Cache L2 de: "+this.chipId+"\n"
      +L2[0][0]+"||"+L2[0][1]+"||"+L2[0][2]+"||"+L2[0][3]+"||"+L2[0][4]+"\n"
      +L2[1][0]+"||"+L2[1][1]+"||"+L2[1][2]+"||"+L2[1][3]+"||"+L2[1][4]+"\n"
      +L2[2][0]+"||"+L2[2][1]+"||"+L2[2][2]+"||"+L2[2][3]+"||"+L2[2][4]+"\n"
      +L2[3][0]+"||"+L2[3][1]+"||"+L2[3][2]+"||"+L2[3][3]+"||"+L2[3][4]+"\n"
      +L2[4][0]+"||"+L2[4][1]+"||"+L2[4][2]+"||"+L2[4][3]+"||"+L2[4][4]+"\n");
    }
  }

  private void missController(){
    if(core0.getL1miss()){ //CASE 1: L1 miss on core 0
      System.out.println("Search data on L2 from: "+chipId+" - "+core0.getCoreId());
      core0.newLog("Search data on L2 from: "+chipId+" - "+core0.getCoreId());
      this.memAddrL2 = core0.getMemAddr();
      if(!this.checkMemAddress(this.memAddrL2)){
        System.out.println("L2: Data from: "+chipId+" - "+core0.getCoreId()+" not found");
        core0.newLog("L2: Data from: "+chipId+" - "+core0.getCoreId()+" not found");
        this.missL2 = true;
        this.coreId = core0.getCoreId();
        System.out.println("Another L2: Search data from: "+chipId+" - "+core0.getCoreId());
        core0.newLog("Another L2: Search data from: "+chipId+" - "+core0.getCoreId());
        this.printTable(L2);
        while(this.missL2){
          try{
            Thread.sleep(1000); //1 second
          }catch(Exception e){
            System.out.println(e.getMessage());
          }
        }
        if(this.exportL2){
            this.core0.setData(this.getDataL2(this.memAddrL2));
        }else{
          System.out.println("L2: WRITE data from: "+chipId+" - "+core0.getCoreId());
          core0.newLog("L2: WRITE data from: "+chipId+" - "+core0.getCoreId());
          for (int i=1; i < L2.length; i++) {
            if(this.lineL2[0].equals(L2[i][0])){
              L2[i] = this.lineL2;
            }
          }
          this.core0.setData(this.getDataL2(this.memAddrL2));
          this.printTable(L2);
        }  
      }else{
        this.core0.setData(this.getDataL2(this.memAddrL2));
      }
      this.core0.setL1Miss(false);
    }else if(core1.getL1miss()){ //CASE 2: L1 miss on core 1
      this.memAddrL2 = core1.getMemAddr();
      if(!this.checkMemAddress(this.memAddrL2)){
        System.out.println("L2: Data from: "+chipId+" - "+core1.getCoreId()+" not found.");
        core1.newLog("L2: Data from: "+chipId+" - "+core1.getCoreId()+" not found.");
        this.missL2 = true;
        this.coreId = core1.getCoreId();
        System.out.println("MEMORY: Search data: "+chipId+" - "+core1.getCoreId());
        core1.newLog("MEMORY: Search data: "+chipId+" - "+core1.getCoreId());
        while(this.missL2);
        System.out.println("L2: WRITE data from: "+chipId+" - "+core1.getCoreId());
        core1.newLog("L2: WRITE data from: "+chipId+" - "+core1.getCoreId());
        while(this.missL2){
          try{
            Thread.sleep(6000); //6 seconds
          }catch(Exception e){
            System.out.println(e.getMessage());
          }
        }
        this.printTable(L2);
        if(this.exportL2){
            this.core1.setData(this.getDataL2(this.memAddrL2));
        }else{
          System.out.println("L2: WRITE data from: "+chipId+" - "+core1.getCoreId());
          core1.newLog("L2: WRITE data from: "+chipId+" - "+core1.getCoreId());
          for (int i=1; i < L2.length; i++) {
            if(this.lineL2[0].equals(L2[i][0])){
              L2[i] = this.lineL2;
            }
          }
          this.core1.setData(this.getDataL2(this.memAddrL2));
          this.printTable(L2);
        }
      }else{
        this.core1.setData(this.getDataL2(this.memAddrL2));
      }
      this.core1.setL1Miss(false);
    }
  }
  
  private void busController(){
    if(this.core0.getBusWrite()){ //CASE 1: Bus write on core 0.
      String block = Integer.toString(Integer.parseInt(core0.getMemAddr(),2)%4);
      for (int i=1; i < L2.length; i++) {
        if(block.equals(L2[i][0])){
          this.L2[i][1] = "DM";
          this.L2[i][2] = this.chipId+","+core0.getCoreId();
          this.L2[i][3] = core0.getMemAddr();
          this.L2[i][4] = core0.getData();
          break;
        }
      }
      core1.invalidateCache(core0.getMemAddr());
      this.memAddrL2 = core0.getMemAddr();
      this.data = core0.getData();
      this.busWrite = true;
      while(this.busWrite){
        try{
          Thread.sleep(2000); //2 seconds
        }catch(Exception e){
          System.out.println(e.getMessage());
        }
      }
      this.core0.setBusWrite(false);
    }else if(this.core1.getBusWrite()){  //CASE 2: Bus write on core 1
      String block = Integer.toString(Integer.parseInt(core1.getMemAddr(),2)%4);
      for (int i=1; i < L2.length; i++) {
        if(block.equals(L2[i][0])){
          this.L2[i][1] = "DM";
          this.L2[i][2] = this.chipId+","+core1.getCoreId();
          this.L2[i][3] = core1.getMemAddr();
          this.L2[i][4] = core1.getData();
          break;
        }
      }
      core0.invalidateCache(core1.getMemAddr());
      this.memAddrL2 = core1.getMemAddr();
      this.data = core1.getData();
      this.busWrite = true;
      while(this.busWrite){
        try{
          Thread.sleep(2000); //2 seconds
        }catch(Exception e){
          System.out.println(e.getMessage());
        }
      }
      this.core1.setBusWrite(false);
    }
  }

  public void newLog(String msg){
    if(core0.getSwitchOn()){
      core0.newLog(msg);
    }if(core1.getSwitchOn()){
      core1.newLog(msg);
    }
  }

  public void run(){
    this.core0.start();
    this.core1.start();
    while(this.switchOn){
      try {
        mutex.acquire();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      this.busController(); //WRITE
      this.missController(); //READ
      mutex.release();
    }
  }

  //----------------------------GETTERS N SETTERS----------------------------
  public String[][] getL2(){
    return L2;
  }

  public void setL2(String[][] L2) {
    this.L2 = L2;
  }

  public boolean getSwitchOn() {
    return switchOn;
  }

  public void setSwitchOn(boolean switchOn) {
    this.switchOn = switchOn;
  }

  public boolean getMissL2() {
    return missL2;
  }

  public void setMissL2(boolean missL2) {
    this.missL2 = missL2;
  }

  public String getChipId(){
    return this.chipId;
  }

  public void setChipId(String chipId){
    this.chipId = chipId;
  }

  public String getMemAddr(){
    return memAddrL2;
  }

  public void setMemAddr(String memAddrL2){
    this.memAddrL2 = memAddrL2;
  }

  public String getData(){
    return data;
  }

  public void setData(String data){
    this.data = data;
  }

  public boolean getBusWrite() {
    return busWrite;
  }

  public void setBusWrite(boolean busWrite) {
    this.busWrite = busWrite;
  }

  public String[] getLineL2() {
    return lineL2;
  }

  public void setLineL2(String[] lineL2) {
    this.lineL2 = lineL2;
  }

  public String getCoreId() {
    return coreId;
  }

  public boolean getExportL2() {
    return exportL2;
  }

  public void setExportL2(boolean exportL2) {
    this.exportL2 = exportL2;
  }

  public Core getCore0(){
    return core0;
  }
  
  public Core getCore1(){
    return core1;
  }

}