package com.web.tilotoma.serviceimpl;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import org.springframework.stereotype.Service;

@Service
public class EsslService {
    private ActiveXComponent device;

    public void connect() {
        device = new ActiveXComponent("zkemkeeper.ZKEM.1");
        boolean connected = Dispatch.call(device, "Connect_Net", "192.168.1.201", 4370).getBoolean();
        System.out.println("Connected: " + connected);
    }
}
