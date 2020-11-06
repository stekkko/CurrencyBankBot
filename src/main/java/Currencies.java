public enum Currencies {
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    CNY("CNY"),
    JPY("JPY");

    private Currencies(String val) {
        this.value = val;
    }

    public static boolean hasValute(String val) {
        for (Currencies cur : Currencies.values()) {
            if (cur.value.equals(val)) return true;
        }
        return false;
    }

    String value;

}
