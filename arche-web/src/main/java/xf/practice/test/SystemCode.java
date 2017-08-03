package xf.practice.test;

public enum SystemCode {
    Groupon(1), Qianggou(2);

    private int code;

    SystemCode(int code) {
        this.code = code;
    }

    public static SystemCode valueOf(int code) {
        switch (code) {
            case 1:
                return Groupon;
            case 2:
                return Qianggou;
            default:
                throw new IllegalArgumentException("" + code);
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
