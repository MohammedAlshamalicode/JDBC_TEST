public class Rekening {

    private String rekeningNummer;
    private double saldo;

    public Rekening(String rekeningNummer, double saldo) {
        this.rekeningNummer = rekeningNummer;
        this.saldo = saldo;
    }

    public String getAccountNumber() {
        return rekeningNummer;
    }

    public void setAccountNumber(String accountNumber) {
        this.rekeningNummer = accountNumber;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Rekening{" +
                "accountNumber='" + rekeningNummer + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
