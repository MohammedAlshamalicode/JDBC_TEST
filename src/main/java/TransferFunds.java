import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class TransferFunds extends AbstractRepository {

    public void transferFunds(String vanRekening, String totRekening, double amount) throws SQLException {
        if (!vanRekening.equals(totRekening) && amount > 0) {
            try (var connection = super.getConnection()) {
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);

                var fromBalance = getBalance(vanRekening)
                        .orElseThrow(() -> new IllegalArgumentException("From account not found."));
                var toBalance = getBalance(totRekening)
                        .orElseThrow(() -> new IllegalArgumentException("To account not found."));

                if (fromBalance >= amount) {
                    updateBalance(vanRekening, -amount);
                    updateBalance(totRekening, amount);
                    connection.commit();
                    System.out.println(amount + " € toevoegt van rekeningnummer :"+vanRekening + " to rekeningnummer : "+totRekening);
                } else {
                    connection.rollback();
                    throw new IllegalArgumentException("Insufficient balance.");
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid transfer details.");
        }
    }

    public Optional<Double> getBalance(String rekeningNummer) throws SQLException {
        var sql = """
                SELECT saldo
                FROM rekeningen
                WHERE nummer = ?
                FOR UPDATE
            """;
        try (var connection = super.getConnection();
                var statement = connection.prepareStatement(sql)) {
            statement.setString(1, rekeningNummer);
            var result = statement.executeQuery();
            return result.next() ? Optional.of(result.getDouble("saldo")) : Optional.empty();
        }
    }

    public void updateBalance(String rekeningNummer, double saldo) throws SQLException {
        var sql = """
                UPDATE rekeningen
                SET saldo = saldo + ?
                WHERE nummer = ?
            """;
        try (var connection = super.getConnection();
                var statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, saldo);
            statement.setString(2, rekeningNummer);
            statement.executeUpdate();
        }
    }
}
