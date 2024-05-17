package android.support.v4.util;

import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile
{
  private final File mBackupName;
  private final File mBaseName;
  
  public AtomicFile(File paramFile)
  {
    mBaseName = paramFile;
    mBackupName = new File(paramFile.getPath() + ".bak");
  }
  
  static boolean sync(FileOutputStream paramFileOutputStream)
  {
    if (paramFileOutputStream != null) {}
    try
    {
      paramFileOutputStream.getFD().sync();
      bool = true;
    }
    catch (IOException paramFileOutputStream)
    {
      for (;;)
      {
        boolean bool = false;
      }
    }
    return bool;
  }
  
  public void delete()
  {
    mBaseName.delete();
    mBackupName.delete();
  }
  
  public void failWrite(FileOutputStream paramFileOutputStream)
  {
    if (paramFileOutputStream != null) {
      sync(paramFileOutputStream);
    }
    try
    {
      paramFileOutputStream.close();
      mBaseName.delete();
      mBackupName.renameTo(mBaseName);
      return;
    }
    catch (IOException paramFileOutputStream)
    {
      for (;;)
      {
        Log.w("AtomicFile", "failWrite: Got exception:", paramFileOutputStream);
      }
    }
  }
  
  public void finishWrite(FileOutputStream paramFileOutputStream)
  {
    if (paramFileOutputStream != null) {
      sync(paramFileOutputStream);
    }
    try
    {
      paramFileOutputStream.close();
      mBackupName.delete();
      return;
    }
    catch (IOException paramFileOutputStream)
    {
      for (;;)
      {
        Log.w("AtomicFile", "finishWrite: Got exception:", paramFileOutputStream);
      }
    }
  }
  
  public File getBaseFile()
  {
    return mBaseName;
  }
  
  public FileInputStream openRead()
    throws FileNotFoundException
  {
    if (mBackupName.exists())
    {
      mBaseName.delete();
      mBackupName.renameTo(mBaseName);
    }
    return new FileInputStream(mBaseName);
  }
  
  /* Error */
  public byte[] readFully()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 98	android/support/v4/util/AtomicFile:openRead	()Ljava/io/FileInputStream;
    //   4: astore_1
    //   5: iconst_0
    //   6: istore_2
    //   7: aload_1
    //   8: invokevirtual 102	java/io/FileInputStream:available	()I
    //   11: newarray <illegal type>
    //   13: astore_3
    //   14: aload_1
    //   15: aload_3
    //   16: iload_2
    //   17: aload_3
    //   18: arraylength
    //   19: iload_2
    //   20: isub
    //   21: invokevirtual 106	java/io/FileInputStream:read	([BII)I
    //   24: istore 4
    //   26: iload 4
    //   28: ifgt +9 -> 37
    //   31: aload_1
    //   32: invokevirtual 107	java/io/FileInputStream:close	()V
    //   35: aload_3
    //   36: areturn
    //   37: iload_2
    //   38: iload 4
    //   40: iadd
    //   41: istore 4
    //   43: aload_1
    //   44: invokevirtual 102	java/io/FileInputStream:available	()I
    //   47: istore 5
    //   49: iload 4
    //   51: istore_2
    //   52: iload 5
    //   54: aload_3
    //   55: arraylength
    //   56: iload 4
    //   58: isub
    //   59: if_icmple -45 -> 14
    //   62: iload 4
    //   64: iload 5
    //   66: iadd
    //   67: newarray <illegal type>
    //   69: astore 6
    //   71: aload_3
    //   72: iconst_0
    //   73: aload 6
    //   75: iconst_0
    //   76: iload 4
    //   78: invokestatic 113	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   81: aload 6
    //   83: astore_3
    //   84: iload 4
    //   86: istore_2
    //   87: goto -73 -> 14
    //   90: astore_3
    //   91: aload_1
    //   92: invokevirtual 107	java/io/FileInputStream:close	()V
    //   95: aload_3
    //   96: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	97	0	this	AtomicFile
    //   4	88	1	localFileInputStream	FileInputStream
    //   6	81	2	i	int
    //   13	71	3	localObject1	Object
    //   90	6	3	localObject2	Object
    //   24	61	4	j	int
    //   47	20	5	k	int
    //   69	13	6	arrayOfByte	byte[]
    // Exception table:
    //   from	to	target	type
    //   7	14	90	finally
    //   14	26	90	finally
    //   43	49	90	finally
    //   52	81	90	finally
  }
  
  public FileOutputStream startWrite()
    throws IOException
  {
    if (mBaseName.exists())
    {
      if (mBackupName.exists()) {
        break label88;
      }
      if (!mBaseName.renameTo(mBackupName)) {
        Log.w("AtomicFile", "Couldn't rename file " + mBaseName + " to backup file " + mBackupName);
      }
    }
    try
    {
      for (;;)
      {
        FileOutputStream localFileOutputStream1 = new java/io/FileOutputStream;
        localFileOutputStream1.<init>(mBaseName);
        return localFileOutputStream1;
        label88:
        mBaseName.delete();
      }
    }
    catch (FileNotFoundException localFileNotFoundException1)
    {
      for (;;)
      {
        if (!mBaseName.getParentFile().mkdir()) {
          throw new IOException("Couldn't create directory " + mBaseName);
        }
        try
        {
          FileOutputStream localFileOutputStream2 = new FileOutputStream(mBaseName);
        }
        catch (FileNotFoundException localFileNotFoundException2)
        {
          throw new IOException("Couldn't create " + mBaseName);
        }
      }
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.util.AtomicFile
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */