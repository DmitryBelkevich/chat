package com.hard.clients;

import com.hard.Server;
import com.hard.utils.webSockets.Decoder;
import com.hard.utils.webSockets.Frame;
import com.hard.utils.webSockets.Handshaker;

import java.io.IOException;
import java.net.Socket;

public class WebClient extends Client {
    public WebClient(Server server, Socket socket) {
        super(server, socket);
    }

    @Override
    public void run() {
        Handshaker.handshake(inputStream, outputStream);
        super.run();
    }

    public String read() {
        while (true) {
            try {
                if (inputStream.available() > 0) {
                    byte[] buffer = new byte[inputStream.available()];

                    inputStream.read(buffer, 0, inputStream.available());

                    Frame frame = Frame.parse(buffer);

                    byte[] message = Decoder.decode(frame.getPayLoad(), frame.getMaskingKey());

                    return new String(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String str) {
        Frame frame = new Frame();

        frame.setFin(true);
        frame.setRsv1(false);
        frame.setRsv2(false);
        frame.setRsv3(false);
        frame.setOpCode((byte) 1);
        frame.setMask(false);
        frame.setPayLoadLength((byte) str.getBytes().length);
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
