package org.example.message;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class ResponseGenerator {
    private final String request;

    public ResponseGenerator(String request) {
        this.request = request;
    }

    public String response() {
        String command = null;

        if(this.request.isEmpty()) {
            command = "명령어를 입력해주세요.\r\n";
        } else if("bye".equals(this.request.toLowerCase())) {
            command = "좋은 하루 보내세요.\r\n";
        } else {
            command = "입력하신 명령어가 '" + this.request + "'입니까?\r\n";
        }

        return command;
    }

    public static String makeHello() throws UnknownHostException {
        StringBuilder sb = new StringBuilder();
        sb.append("환영합니다. ")
                .append(InetAddress.getLocalHost().getHostName())
                .append("에 접속하셨습니다.\r\n")
                .append("현재 시간은 ")
                .append(new Date().toString())
                .append(" 입니다.\r\n");

        return sb.toString();
    }

    public boolean isClose() {
        return "bye".equals(this.request);
    }
}
