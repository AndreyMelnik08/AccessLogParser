package ru.stepup.access.log.parser;

public class ResponseCode {

    private int code;
    private boolean erroneousRequest;

    public ResponseCode(int responseCode) {
        this.code = responseCode(responseCode);
        this.erroneousRequest = erroneousRequest(String.valueOf(responseCode));
    }

    private int responseCode(int responseCode) {
        return responseCode;
    }

    private boolean erroneousRequest(String responseCode) {
        if (responseCode.startsWith(("4")) || responseCode.startsWith("5")) {
            return true;
        }
        return false;
    }

    public int getCode() {
        return code;
    }

    public boolean isErroneousRequest() {
        return erroneousRequest;
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                "code=" + code +
                ", erroneousRequest=" + erroneousRequest +
                '}';
    }
}
