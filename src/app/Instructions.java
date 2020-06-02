package app;
import java.util.Random;

public class Instructions {
    
    private String instruction;
    private int operand;

    //Constructor
    public Instructions(){
        this.instruction = "";
        this.operand = 0;
    }

    //---------------Methods--------------------
    private int poisson (int lambda){
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;
        do{
            k++;
            p *= Math.random();
        } while (p > L);
        return k-1;
    }
    
    private String getOper(){ //Operands of the instructions, RANDOM
        final int num = this.poisson(1);
        //System.out.println(num);
        this.operand = num;
        if (num == 0){
            return "READ";
        }else if (num == 1){
            return "WRITE";
        }else{
            return "CALC";
        }
    }

    private String getMemAddres(){ // Memory direction of the instructions, RANDOM
        Random r = new Random();
        final int num = r.nextInt(16);
        final String memAddr = Integer.toBinaryString(num);
        if (memAddr.length() == 1){
            return "000"+memAddr;
        }else if (memAddr.length() == 2){
            return "00"+memAddr;
        }else if (memAddr.length() == 3){
            return "0"+memAddr;
        }else{
            return memAddr;
        }
    }

    private String getData(){ //Write instruction data
        final Random r = new Random();
        final int data = r.nextInt(65500);
        String dataStr = Integer.toHexString(data);
        if (dataStr.length() == 1){
            return "000"+dataStr;
        }else if (dataStr.length() == 2){
            return "00"+dataStr;
        }else if (dataStr.length() == 3){
            return "0"+dataStr;
        }else{
            return dataStr;
        }
    }

    public String instGenerator(final String chipId, final String coreId){
        this.instruction = chipId+","+coreId+","+this.getOper();
        if (operand == 0){ //when the operand is READ
            this.instruction += ","+this.getMemAddres();
            return this.instruction;
        }else if (operand == 1){ // when the operand is WRITE
            this.instruction += ","+this.getMemAddres()+","+this.getData();
            return this.instruction;
        }else{ //when the operand is CALC
            return this.instruction;
        }
    }

}