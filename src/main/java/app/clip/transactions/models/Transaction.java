package app.clip.transactions.models;

import app.clip.commons.money.Money;
import app.clip.converters.MoneyAttributeConverter;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "l_id")
    private Long lenderId;

    @Column(name = "b_id")
    private Long borrowerId;

    @Convert(converter = MoneyAttributeConverter.class)
    @Column(name = "amt")
    private Money amt;

    @Column(name = "grp_id")
    private Long associatedGroupId;

    @Column(name = "paid_on")
    private Long paidOn;

    @Column(name = "upd_at")
    private Long updatedAt;

    // for soft deletes
    @Column(name = "active")
    private Boolean isActive;

    public Transaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setAssociatedGroupId(Long associatedGroupId) {
        this.associatedGroupId = associatedGroupId;
    }

    public Money getAmt() {
        return amt;
    }

    public void setAmt(Money amt) {
        this.amt = amt;
    }

    public Long getAssociatedGroupId() {
        return associatedGroupId;
    }

    public Long getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(Long paidOn) {
        this.paidOn = paidOn;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(lenderId, that.lenderId) && Objects.equals(borrowerId, that.borrowerId) && Objects.equals(amt, that.amt) && Objects.equals(associatedGroupId, that.associatedGroupId) && Objects.equals(paidOn, that.paidOn) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lenderId, borrowerId, amt, associatedGroupId, paidOn, updatedAt, isActive);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", lenderId=" + lenderId +
                ", borrowerId=" + borrowerId +
                ", amt=" + amt +
                ", associatedGroupId=" + associatedGroupId +
                ", paidOn=" + paidOn +
                ", updatedAt=" + updatedAt +
                ", isActive=" + isActive +
                '}';
    }

    @PrePersist
    public void prePersist() {
        Long systemTime = System.currentTimeMillis();
        setPaidOn(systemTime);
        setUpdatedAt(systemTime);
    }

    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(System.currentTimeMillis());
    }
}
