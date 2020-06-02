package app;

import java.util.concurrent.Semaphore;

public class Memory extends Thread {

  public boolean switchOn = true;

  private String data= "";

  private Chip chip0;
  private Chip chip1;

  public String[][] mainMemory ={{"Block", "Owner", "Data"},
                              {"0000","","0000"},
                              {"0001","","0000"},
                              {"0010","","0000"},
                              {"0011","","0000"},
                              {"0100","","0000"},
                              {"0101","","0000"},
                              {"0110","","0000"},
                              {"0111","","0000"},
                              {"1000","","0000"},
                              {"1001","","0000"},
                              {"1010","","0000"},
                              {"1011","","0000"},
                              {"1100","","0000"},
                              {"1101","","0000"},
                              {"1110","","0000"},
                              {"1111","","0000"},
  };

  private static Semaphore mutex = new Semaphore(1);

  //COnstructor
  public Memory (String msg){
    super(msg);
    this.chip0 = new Chip("Chip0", "P0", true); 
    this.chip1 = new Chip("Chip1","P1", true);
  }

  //---------------------------------METHODS-------------------------------------
  
  private void printTable(String[][] Memory){
    System.out.println("Main memory");
    for (int i=1; i < mainMemory.length; i++) {
      if(this.mainMemory[i][1].equals("")){
        this.mainMemory[i][1] = "null";
      }
    }
    for (int i=1; i < mainMemory.length; i++) {
      System.out.println(this.mainMemory[i][0]+"||"+this.mainMemory[i][1]+"||"+this.mainMemory[i][2]);
    }
    if(chip0.getSwitchOn()){
      chip0.newLog("Main Memory:\n"
      +this.mainMemory[0][0]+"||"+this.mainMemory[0][1]+"||"+this.mainMemory[0][2]+"\n"
      +this.mainMemory[1][0]+"||"+this.mainMemory[1][1]+"||"+this.mainMemory[1][2]+"\n"
      +this.mainMemory[2][0]+"||"+this.mainMemory[2][1]+"||"+this.mainMemory[2][2]+"\n"
      +this.mainMemory[3][0]+"||"+this.mainMemory[3][1]+"||"+this.mainMemory[3][2]+"\n"
      +this.mainMemory[4][0]+"||"+this.mainMemory[4][1]+"||"+this.mainMemory[4][2]+"\n"
      +this.mainMemory[5][0]+"||"+this.mainMemory[5][1]+"||"+this.mainMemory[5][2]+"\n"
      +this.mainMemory[6][0]+"||"+this.mainMemory[6][1]+"||"+this.mainMemory[6][2]+"\n"
      +this.mainMemory[7][0]+"||"+this.mainMemory[7][1]+"||"+this.mainMemory[7][2]+"\n"
      +this.mainMemory[8][0]+"||"+this.mainMemory[8][1]+"||"+this.mainMemory[8][2]+"\n"
      +this.mainMemory[9][0]+"||"+this.mainMemory[9][1]+"||"+this.mainMemory[9][2]+"\n"
      +this.mainMemory[10][0]+"||"+this.mainMemory[10][1]+"||"+this.mainMemory[10][2]+"\n"
      +this.mainMemory[11][0]+"||"+this.mainMemory[11][1]+"||"+this.mainMemory[11][2]+"\n"
      +this.mainMemory[12][0]+"||"+this.mainMemory[12][1]+"||"+this.mainMemory[12][2]+"\n"
      +this.mainMemory[13][0]+"||"+this.mainMemory[13][1]+"||"+this.mainMemory[13][2]+"\n"
      +this.mainMemory[14][0]+"||"+this.mainMemory[14][1]+"||"+this.mainMemory[14][2]+"\n"
      +this.mainMemory[15][0]+"||"+this.mainMemory[15][1]+"||"+this.mainMemory[15][2]+"\n"
      +this.mainMemory[16][0]+"||"+this.mainMemory[16][1]+"||"+this.mainMemory[16][2]+"\n");
    }if(chip1.getSwitchOn()){
      chip1.newLog("Main Memory:\n"
      +this.mainMemory[0][0]+"||"+this.mainMemory[0][1]+"||"+this.mainMemory[0][2]+"\n"
      +this.mainMemory[1][0]+"||"+this.mainMemory[1][1]+"||"+this.mainMemory[1][2]+"\n"
      +this.mainMemory[2][0]+"||"+this.mainMemory[2][1]+"||"+this.mainMemory[2][2]+"\n"
      +this.mainMemory[3][0]+"||"+this.mainMemory[3][1]+"||"+this.mainMemory[3][2]+"\n"
      +this.mainMemory[4][0]+"||"+this.mainMemory[4][1]+"||"+this.mainMemory[4][2]+"\n"
      +this.mainMemory[5][0]+"||"+this.mainMemory[5][1]+"||"+this.mainMemory[5][2]+"\n"
      +this.mainMemory[6][0]+"||"+this.mainMemory[6][1]+"||"+this.mainMemory[6][2]+"\n"
      +this.mainMemory[7][0]+"||"+this.mainMemory[7][1]+"||"+this.mainMemory[7][2]+"\n"
      +this.mainMemory[8][0]+"||"+this.mainMemory[8][1]+"||"+this.mainMemory[8][2]+"\n"
      +this.mainMemory[9][0]+"||"+this.mainMemory[9][1]+"||"+this.mainMemory[9][2]+"\n"
      +this.mainMemory[10][0]+"||"+this.mainMemory[10][1]+"||"+this.mainMemory[10][2]+"\n"
      +this.mainMemory[11][0]+"||"+this.mainMemory[11][1]+"||"+this.mainMemory[11][2]+"\n"
      +this.mainMemory[12][0]+"||"+this.mainMemory[12][1]+"||"+this.mainMemory[12][2]+"\n"
      +this.mainMemory[13][0]+"||"+this.mainMemory[13][1]+"||"+this.mainMemory[13][2]+"\n"
      +this.mainMemory[14][0]+"||"+this.mainMemory[14][1]+"||"+this.mainMemory[14][2]+"\n"
      +this.mainMemory[15][0]+"||"+this.mainMemory[15][1]+"||"+this.mainMemory[15][2]+"\n"
      +this.mainMemory[16][0]+"||"+this.mainMemory[16][1]+"||"+this.mainMemory[16][2]+"\n");
    }
  }

  /**
   * Read in memory with the memory address 
   * @param memAddr: memory address of the instruction
   * @param coreId: Id of the core
  */
  private void readMemory(String memAddr, String coreId){
    for (int i=1; i < mainMemory.length; i++){
      if(memAddr.equals(this.mainMemory[i][0])){
        if(this.mainMemory[i][1].equals("P0")){
          this.mainMemory[i][1] += ",P1";
        }else if(this.mainMemory[i][1].equals("P1")){
          this.mainMemory[i][1] += ",P0";
        }else if(this.mainMemory[i][1].equals("")){
          this.mainMemory[i][1] = coreId;
        }
        this.data = this.mainMemory[i][2];
      }
    }
    this.printTable(this.mainMemory);
  }

  /**
   * Write in memory with the memory address 
   * @param memAddr: memory address of the instruction
   * @param data: Data of the instruction
   * @param coreId: Id of the core
  */
  private void writeMemory(String memAddr, String data, String coreId){
    for (int i=1; i < mainMemory.length; i++) {
      if(memAddr.equals(mainMemory[i][0])){
        if(this.mainMemory[i][1].equals("P1")){
          mainMemory[i][1] += ",P0";
        }else if(this.mainMemory[i][1].equals("P0")){
          mainMemory[i][1] += ",P1";
        } else if(this.mainMemory[i][1].equals("")){
          mainMemory[i][1] = coreId;
        }
        mainMemory[i][2] = data; 
      }
    }
    this.printTable(this.mainMemory);
  }

  private void writeController(){
    if(this.chip0.getBusWrite()){ //  CASE 1: For chip 0, if flag busWrite is active
      writeMemory(this.chip0.getMemAddr(), this.chip0.getData(), this.chip0.getChipId());
      this.chip1.invalidateCacheL2(this.chip0.getMemAddr());
      try{
        Thread.sleep(2000); // 2 seconds
      }catch(Exception e){
        System.out.println(e.getMessage());
      }
      this.chip0.setBusWrite(false); //flag busWrite OFF
    }else if(this.chip1.getBusWrite()){ //  CASE 2: For chip 1, if flag busWrite is active
      writeMemory(this.chip1.getMemAddr(), this.chip1.getData(), this.chip1.getChipId());
      this.chip0.invalidateCacheL2(this.chip1.getMemAddr());
      try{
        Thread.sleep(2000); // 1 second
      }catch(Exception e){
        System.out.println(e.getMessage());
      }
      this.chip1.setBusWrite(false); //flag busWrite OFF
    }
  }

  private void cacheController(){
    if(this.chip0.getMissL2()){ // CASE 1: For chip 0, if flag misses is active
      if(chip1.checkMemAddress(chip0.getMemAddr())){ //if memory address on chip 0 and chip1 are equal 
        String Owner = this.chip1.getValueL2(2, this.chip0.getMemAddr());
        int len = Owner.length();
        if(!Owner.substring(len-2).equals(",E")){
          this.chip1.setValueL2(2, chip0.getMemAddr(), ",E");
        }
        this.chip0.setData(this.chip1.getDataAux(this.chip0.getMemAddr()));
        this.chip0.setExportL2(true);
      }else{
        this.readMemory(this.chip0.getMemAddr(),this.chip0.getChipId());
        int block = Integer.parseInt(chip0.getMemAddr(),2)%4;
        String owner = chip0.getChipId()+","+chip0.getCoreId();
        String[] cacheLine = {Integer.toString(block),"DS",owner,chip0.getMemAddr(),this.data};
        this.chip0.setLineL2(cacheLine);
      }
      chip0.setMissL2(false); //flag missL2 off
    }else if(this.chip1.getMissL2()){ // CASE 2: For chip 1, if flag misses is active
      if(chip0.checkMemAddress(chip1.getMemAddr())){
        String Owner = this.chip0.getValueL2(2, this.chip1.getMemAddr());
        int len = Owner.length();
        if(!Owner.substring(len-2).equals(",E")){
          this.chip0.setValueL2(2, chip1.getMemAddr(), ",E");
        }
        this.chip1.setData(this.chip0.getDataAux(this.chip1.getMemAddr()));
        this.chip1.setExportL2(true);
      }else{
        this.readMemory(this.chip1.getMemAddr(),this.chip1.getChipId());
        int block = Integer.parseInt(chip1.getMemAddr(),2)%4;
        String owner = chip1.getChipId()+","+chip1.getCoreId();
        String[] cacheLine = {Integer.toString(block),"DS",owner,chip1.getMemAddr(),this.data};
        this.chip1.setLineL2(cacheLine);
      }
      chip1.setMissL2(false); //flag missL2 off
    }
  }

  public void run(){
    this.chip0.start();
    this.chip1.start();
    while(this.switchOn){
      try {
        mutex.acquire();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      this.writeController();
      this.cacheController();
      mutex.release();
    }
  }

  public String[][] getMemory() {
    return mainMemory;
  }

  public Chip getChip0() {
    return chip0;
  }

  public Chip getChip1() {
    return chip1;
  }
}