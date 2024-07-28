package app.clip.splits.models;

import app.clip.commons.Money;
import app.clip.converters.MoneyAttributeConverter;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "splits")
public class Split implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usr_id")
    private Long userId;

    @Column(name = "exp_id")
    private Long expenseId;

    @Convert(converter = MoneyAttributeConverter.class)
    @Column(name = "splt_amt")
    private Money splitAmount;

    @Column(name = "crt_at")
    private Long createdAt;

    @Column(name = "upd_at")
    private Long updatedAt;

    public Split() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public Money getSplitAmount() {
        return splitAmount;
    }

    public void setSplitAmount(Money splitAmount) {
        this.splitAmount = splitAmount;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Split split = (Split) o;
        return Objects.equals(id, split.id) && Objects.equals(userId, split.userId) && Objects.equals(expenseId, split.expenseId) && Objects.equals(splitAmount, split.splitAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, expenseId, splitAmount);
    }

    @Override
    public String toString() {
        return "Split{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", expenseId='" + expenseId + '\'' +
                ", splitAmount=" + splitAmount +
                '}';
    }

    @PrePersist
    public void prePersist() {
        Long systemTime = System.currentTimeMillis();
        setCreatedAt(systemTime);
        setUpdatedAt(systemTime);
    }

    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(System.currentTimeMillis());
    }
}
