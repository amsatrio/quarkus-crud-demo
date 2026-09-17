package io.github.amsatrio.modules.hospital.t_customer_wallet_withdraw;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TCustomerWalletWithdrawRepository implements PanacheRepository<TCustomerWalletWithdraw> {

    private static final String TABLE = "t_customer_wallet_withdraw";

    private static final String SELECT_ALL = "SELECT id, customer_id, wallet_default_nominal_id, amount, bank_name, account_number, account_name, otp, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM t_customer_wallet_withdraw";

    public TCustomerWalletWithdraw findById(Long id) {
        return (TCustomerWalletWithdraw) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", TCustomerWalletWithdraw.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<TCustomerWalletWithdraw> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, TCustomerWalletWithdraw.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<TCustomerWalletWithdraw> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, TCustomerWalletWithdraw.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setParameter("limit", pageSize);
        query.setParameter("offset", pageIndex * pageSize);
        return query.getResultList();
    }

    public long countByFilter(String whereClause, Map<String, Object> params) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE " + whereClause;
        var query = getEntityManager()
                .createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return (Long) query.getSingleResult();
    }

    public void insert(TCustomerWalletWithdraw data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, customer_id, wallet_default_nominal_id, amount, bank_name, account_number, account_name, otp, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :customer_id, :wallet_default_nominal_id, :amount, :bank_name, :account_number, :account_name, :otp, :created_by, :created_on, :modified_by, :modified_on, :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("customer_id", data.getCustomerId())
                .setParameter("wallet_default_nominal_id", data.getWalletDefaultNominalId())
                .setParameter("amount", data.getAmount())
                .setParameter("bank_name", data.getBankName())
                .setParameter("account_number", data.getAccountNumber())
                .setParameter("account_name", data.getAccountName())
                .setParameter("otp", data.getOtp())
                .setParameter("created_by", data.getCreatedBy())
                .setParameter("created_on", data.getCreatedOn())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .executeUpdate();
    }

    public void update(TCustomerWalletWithdraw data) {
        String sql = "UPDATE " + TABLE
                + " SET customer_id = :customer_id, wallet_default_nominal_id = :wallet_default_nominal_id, amount = :amount, bank_name = :bank_name, account_number = :account_number, account_name = :account_name, otp = :otp, modified_by = :modified_by, modified_on = :modified_on, deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("customer_id", data.getCustomerId())
                .setParameter("wallet_default_nominal_id", data.getWalletDefaultNominalId())
                .setParameter("amount", data.getAmount())
                .setParameter("bank_name", data.getBankName())
                .setParameter("account_number", data.getAccountNumber())
                .setParameter("account_name", data.getAccountName())
                .setParameter("otp", data.getOtp())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .setParameter("id", data.getId())
                .executeUpdate();
    }

    public void softDelete(Long id, Long userId) {
        String sql = "UPDATE " + TABLE
                + " SET is_delete = true, deleted_by = :userId, deleted_on = :deletedOn"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .setParameter("deletedOn", new java.util.Date())
                .executeUpdate();
    }

    public void hardDelete(Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", id)
                .executeUpdate();
    }

    public static String toColumnName(String fieldName) {
        return switch (fieldName) {
            case "customerId" -> "customer_id";
            case "walletDefaultNominalId" -> "wallet_default_nominal_id";
            case "bankName" -> "bank_name";
            case "accountNumber" -> "account_number";
            case "accountName" -> "account_name";
            case "createdBy" -> "created_by";
            case "createdOn" -> "created_on";
            case "modifiedBy" -> "modified_by";
            case "modifiedOn" -> "modified_on";
            case "deletedBy" -> "deleted_by";
            case "deletedOn" -> "deleted_on";
            case "isDelete" -> "is_delete";
            default -> fieldName;
        };
    }
}
