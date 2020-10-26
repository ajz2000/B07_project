package com.b07.serialize;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A class that handles serialization of an object without checks
 *
 * @author lingf
 */
public class SerializeFunc {

  /**
   * serialize an serializable object to a file to a path
   *
   * @param x the object to be serialized
   * @param location the path of the new file
   * @throws IOException
   */
  public static void serialize(Serializable x, String location) throws IOException {
    FileOutputStream fileOut = new FileOutputStream(location);
    ObjectOutputStream out = new ObjectOutputStream(fileOut);
    out.writeObject(x);
    out.close();
    fileOut.close();
  }

  /**
   * deserialize an object from a file with given path
   *
   * @param location the path of the file
   * @return deserialized object
   * @throws IOException
   * @throws ClassNotFoundException
   */
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
