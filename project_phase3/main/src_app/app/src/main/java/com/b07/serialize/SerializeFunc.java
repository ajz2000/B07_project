package com.b07.serialize;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializeFunc {

  public static void serialize(Serializable x, String location) throws IOException {
    FileOutputStream fileOut = new FileOutputStream(location);
    ObjectOutputStream out = new ObjectOutputStream(fileOut);
    out.writeObject(x);
    out.close();
    fileOut.close();
  }

  public static DataStorage deserialize(String location)
      throws IOException, ClassNotFoundException {
    FileInputStream fileIn = new FileInputStream(location);
    ObjectInputStream in = new ObjectInputStream(fileIn);
    Object z = in.readObject();
    in.close();
    fileIn.close();
    return (DataStorage) z;
  }
}
