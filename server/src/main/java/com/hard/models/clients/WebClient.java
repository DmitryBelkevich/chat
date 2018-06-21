package com.hard.models.clients;

import com.hard.models.Server;
import com.hard.utils.webSockets.Decoder;
import com.hard.utils.webSockets.Frame;

import java.io.IOException;
import java.net.Socket;

public class WebClient extends Client {
    public WebClient(Server server, Socket socket) {
        super(server, socket);
    }

    public String read() {
        try {
            while (true) {
                if (inputStream.available() > 0) {
                    byte[] buffer = new byte[inputStream.available()];

                    inputStream.read(buffer, 0, inputStream.available());

                    Frame frame = Frame.parse(buffer);

                    byte[] message = Decoder.decode(frame.getPayLoad(), frame.getMaskingKey());

                    return new String(message);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void write(String str) {
        Frame frame = new Frame();

        frame.setFin(true);
        frame.setRsv1(false);
        frame.setRsv2(false);
        frame.setRsv3(false);
        frame.setOpCode((byte) 0x1);
        frame.setMask(false);
        frame.setPayLoadLength(str.getBytes().length);
        frame.setMaskingKey(null);
        frame.setPayLoad(str.getBytes());

        byte[] bytes = frame.asBytes();

        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }
}
