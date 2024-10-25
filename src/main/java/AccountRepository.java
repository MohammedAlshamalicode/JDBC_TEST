import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class AccountRepository extends AbstractRepository {

    public void createNewAccount(String rekeningNummer) throws SQLException {
        if (isValidRekeningNummer(rekeningNummer)) {
            var sql = """
                INSERT INTO rekeningen (nummer, saldo) VALUES (?, 0)
            """;
            try (var connection = super.getConnection();
                 var statement = connection.prepareStatement(sql)) {
                statement.setString(1, rekeningNummer);
                statement.executeUpdate();
                System.out.println("Account created successfully.");
            } catch (SQLException e) {
                throw new SQLException("Failed to create account: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Invalid account number.");
        }
    }

    private boolean isValidRekeningNummer(String rekeningNummer) {
        if (rekeningNummer.length() != 16 || !rekeningNummer.startsWith("BE")) {
            return false;
        }
        int controlNumber = Integer.parseInt(rekeningNummer.substring(2, 4));
        if (controlNumber < 2 || controlNumber > 98) {
            return false;
        }
        var digits = rekeningNummer.substring(4) + "1114" + controlNumber;
        var number = Long.parseLong(digits);
        return number % 97 == 1;
    }

    public Optional<Rekening> getAccountBalance(String rekeningNummer) throws SQLException {
        var sql = """
            SELECT nummer, saldo
            FROM rekeningen
            WHERE nummer = ?
        """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, rekeningNummer);
            var result = statement.executeQuery();
            return result.next() ? Optional.of(new Rekening(result.getString("nummer"), result.getDouble("saldo"))) : Optional.empty();
        }
    }

    public Optional<Double> getBalanceForUpdate(String rekeningNummer) throws SQLException {
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
