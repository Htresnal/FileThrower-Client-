package FileThrowerCL;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class thd_baseFileThread extends thd_baseThread
{
    public void thd_reserveSendFile(String sendFile) throws java.io.IOException
    {
        try{
            inputFileStream.close();
            inputFileStream = new FileInputStream(sendFile);
            inputStreamBuffered.close();
            inputStreamBuffered = new BufferedInputStream(inputFileStream);
        }
        catch (IOException e) {
            throw new IOException("FileConnManager: [ERROR] Unable to close/create send file streams");
        }
    }

    public void thd_reserveRecvFile(String sendFile) throws java.io.IOException
    {
        try{
            inputFileStream.close();
            inputFileStream = new FileInputStream(sendFile);
            inputStreamBuffered.close();
            inputStreamBuffered = new BufferedInputStream(inputFileStream);
        }
        catch (IOException e) {
            throw new IOException("FileConnManager: [ERROR] Unable to close/create send file streams");
        }
    }

    @Override
    public void deInit() throws java.io.IOException
    {
        super.deInit();
        try {
            if (inputFileStream != null) inputFileStream.close();
            if (outputFileStream != null) outputFileStream.close();
        }
        catch(IOException e)
        {
            throw new IOException("FileConnManager: [ERROR] Unable to close file data streams at deInit()");
        }
    }

    @Override
    public void run()
    {
        if (dcm_ConnType == ConnStatus.SERVER)
        {
            System.out.println("DataConnManager: Setting up a server...");
            try
            {
                dcm_serverSocket = new ServerSocket(thd_Port);
                System.out.println("DataConnManager: Server at port "+thd_Port+" has been initialized.");
                System.out.println("DataConnManager: Waiting for an incoming connection...");
                dcm_Socket=dcm_serverSocket.accept();
                System.out.println("DataConnManager: Connection accepted. Initializing streams...");
                outputStream = new DataOutputStream(dcm_Socket.getOutputStream());
                inputStream = new DataInputStream(dcm_Socket.getInputStream());
                inputStreamReader = new BufferedReader(new InputStreamReader(dcm_Socket.getInputStream()));
                System.out.println("DataConnManager: Streams up. Waiting for commands...");
            }
            catch (IOException e)
            {
                try
                {
                    throw e;
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
        else if (dcm_ConnType == ConnStatus.CLIENT)
        {
            System.out.println("DataConnManager: Connecting...");
            try {
                dcm_Socket = new Socket(thd_address_IP, thd_Port);
                System.out.println("DataConnManager: Connected to: "+thd_address_IP+":"+thd_Port);
                outputStream = new DataOutputStream(dcm_Socket.getOutputStream());
                inputStream = new DataInputStream(dcm_Socket.getInputStream());
                inputStreamReader = new BufferedReader(new InputStreamReader(dcm_Socket.getInputStream()));
            }
            catch (IOException e) {
                try
                {
                    throw new IOException("DataConnManager: [ERROR] Unable to connect to: "+thd_address_IP+":"+thd_Port);
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void thd_sendFile() throws java.io.IOException
    {
        System.out.println("FileConnManager: Server have received the file header data. Sending file itself...");
        inputStreamBuffered.read(byteEArr, 0, byteArr.length);
        System.out.println("FileConnManager: Sending file data...");
        outputStream.write(byteEArr, 0, byteArr.length);
        outputStream.flush();
        System.out.println("FileConnManager: File sent.");
    }

    public void thd_recvFile() throws java.io.IOException
    {
        outputFileStream=new FileOutputStream(thd_File);
        outputStreamBuffered = new BufferedOutputStream(outputFileStream);

        inputStream = dcm_Socket.getInputStream();
        while((buffByteArrSize=inputStream.read(byteEArr)) >0){
            outputFileStream.write(byteEArr);
        }
        outputFileStream.close();
    }
}