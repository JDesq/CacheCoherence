package app;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
  
  private String fileName;
  private Logger log;
  private FileHandler logFileHandler;

  public Log(String fileName) {
    this.fileName = fileName;

    try {
      this.log = Logger.getLogger(this.fileName);
      this.logFileHandler = new FileHandler(fileName+".log");
      this.log.addHandler(this.logFileHandler);

      SimpleFormatter formatter = new SimpleFormatter();
      this.logFileHandler.setFormatter(formatter);

    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void newInfo(String msg){
    this.log.info(msg);
  }

}